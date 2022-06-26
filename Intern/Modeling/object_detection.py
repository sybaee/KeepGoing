import os
import csv
import time
import json
import imutils

import numpy as np

import cv2

class AutoBoundingBox:
    def __init__(self,
                 confidence=0.5,
                 threshold=0.3,
                 verbose=False):
        self.confidence = confidence
        self.threshold = threshold

        # Load COCO class labels
        yolo_path = 'yolo-coco'
        labels_path = os.path.sep.join([yolo_path, 'coco.names'])
        self.LABELS = open(labels_path).read().strip().split('\n')

        # Initialize a list of colors to represent each possible class label
        np.random.seed(42)
        self.COLORS = np.random.randint(0, 255,
                                        size=(len(self.LABELS), 3), dtype="uint8")

        # Derive the paths to the YOLO weights and model configuration
        weights_path = os.path.sep.join([yolo_path, "yolov3.weights"])
        config_path = os.path.sep.join([yolo_path, 'yolov3.cfg'])

        # Load our YOLO object detector trained on COCO dataset (80 classes)
        if verbose:
            print("[AutoBoundingBox] Loading YOLO from disk...")
        self.net = cv2.dnn.readNetFromDarknet(config_path, weights_path)

        # Determine only the output layer names that we need from YOLO
        self.ln = self.net.getLayerNames()
        self.ln = [self.ln[i[0] - 1] for i in self.net.getUnconnectedOutLayers()]

    # Bounding box for single image or frame.
    # Return bounding box information (location, size),
    # confidence (i.e., probability) and class ID
    def bound_box(self, image, width, height):
        # Construct a blob from the input frame and then perform a forward
        # pass of the YOLO object detector, giving us our bounding boxes
        # and associated probabilities
        blob = cv2.dnn.blobFromImage(image, 1 / 255.0, (416, 416),
                                     swapRB=True, crop=False)
        self.net.setInput(blob)
        layer_outputs = self.net.forward(self.ln)

        # Initialize our lists of detected bounding boxes, confidences,
        # and class IDs, respectively
        boxes = []
        confidences = []
        class_ids = []

        # Loop over each of the layer outputs
        for output in layer_outputs:
            # Loop over each of the detections
            for detection in output:
                # Extract the class ID and confidence (i.e., probability)
                # of the current object detection
                scores = detection[5:]
                class_id = np.argmax(scores)
                confidence = scores[class_id]

                # Filter our weak predictions by ensuring the detected
                # probability is greater than the minimum probability
                if confidence > self.confidence:
                    # Scale the bounding box coordinates back relative to
                    # the size of the image, keeping in mind that YOLO actually
                    # returns the center (x, y)-coordinates of the bounding box
                    # followed by the boxes' width and height
                    box = detection[0:4] * np.array([width, height, width, height])
                    (cx, cy, w, h) = box.astype("int")

                    # Use the center (x, y)-coordinates to derive the top
                    # and left corner of the bounding box
                    x = int(cx - (w / 2))
                    y = int(cy - (h / 2))

                    # Update our list of bounding box coordinates,
                    # confidences, and class IDs
                    boxes.append([x, y, int(w), int(h)])
                    confidences.append(float(confidence))
                    class_ids.append(class_id)

        return boxes, confidences, class_ids

    # Construct bounding box from image
    def bound_img(self,
                  input_path,
                  verbose=False,
                  visual=False):
        image = cv2.imread(input_path)
        (H, W) = image.shape[:2]

        start = time.time()
        boxes, confidences, class_ids = self.bound_box(image, W, H)
        end = time.time()

        # Show timing information on bounding box
        if verbose:
            print("[AutoBoundaryBox] This image took {:6f} seconds".format(end - start))

        # Apply non-maxima suppression to suppress weak, overlapping bounding boxes
        idx = cv2.dnn.NMSBoxes(boxes, confidences,
                               self.confidence, self.threshold)

        # Ensure at least one detection exists
        boxes_json = []
        if len(idx) > 0:
            # Loop over the indexes we are keeping
            for i in idx.flatten():
                # Extract the bounding box coordinates
                x, y, w, h = boxes[i]

                # Construct json list
                json_elem = {
                    "bounding_box": {
                        "x": x,
                        "y": y,
                        "width": w,
                        "height": h
                    },
                    "classification": {
                        "code": self.LABELS[class_ids[i]],
                        "attributes": []
                    }
                }
                boxes_json.append(json_elem)

                if visual:
                    # Draw a bounding box rectangle and label on the image
                    color = [int(c) for c in self.COLORS[class_ids[i]]]
                    cv2.rectangle(image, (x, y), (x + w, y + h), color, 2)
                    # text = "{}: {:4f}".format(self.LABELS[class_ids[i]], confidences[i])
                    # cv2.putText(image, text, (x, y - 5), cv2.FONT_HERSHEY_SIMPLEX,
                    #             0.5, color, 2)

        if visual:
            save_name = 'image_box/' + os.path.basename(input_path)
            cv2.imwrite(save_name, image)

        # if visual:
        #     # Show the output image
        #     cv2.imshow("Image", image)
        #     cv2.waitKey(0)

        return json.dumps(boxes_json), len(boxes_json)

    def bound_video(self,
                    input_path,
                    output_path,
                    detect_rate=1,
                    verbose=False,
                    visual=False):
        # Open video file stream
        vs = cv2.VideoCapture(input_path)
        writer = None
        (W, H) = (None, None)
        frame_count = -1

        # Get total number of frame in video stream
        try:
            prop = cv2.cv.CV_CAP_PROP_FRAME_COUNT if imutils.is_cv2() \
                else cv2.CAP_PROP_FRAME_COUNT
            total = int(vs.get(prop))
            if verbose:
                print("[AutoBoundingBox] {} total frames in video".format(total))
        except all:
            print("[AutoBoundingBox] could not determine # of frames in video")
            print("[AutoBoundingBox] no approx. completion time can be provided")
            total = -1

        # Open csv file to write
        path_avi = os.path.splitext(output_path + os.path.basename(input_path))[0] + '.avi'
        path_csv = os.path.splitext(path_avi)[0] + '.csv'
        file_csv = open(path_csv, 'w', newline='')
        writer_csv = csv.writer(file_csv)

        # Loop over frames from the video file stream
        while True:
            # Read the next frame from the file
            (grabbed, frame) = vs.read()

            # if the frame was not grabbed, then we have reached the end
            # of the stream
            if not grabbed:
                break

            # If the frame dimensions are empty, grab them
            if W is None or H is None:
                (H, W) = frame.shape[:2]

            # Skip detecting when current frame is not detect target
            frame_count += 1
            if frame_count % detect_rate:
                continue

            start = time.time()
            boxes, confidences, class_ids = self.bound_box(frame, W, H)
            end = time.time()

            # Apply non-maxima suppression to suppress weak, overlapping
            # bounding boxes
            idx = cv2.dnn.NMSBoxes(boxes, confidences,
                                   self.confidence, self.threshold)

            # Ensure at least one detection exists
            if len(idx) > 0:
                # Loop over the indexes we are keeping
                for i in idx.flatten():
                    # Extract the bounding box coordinates
                    x, y, w, h = boxes[i]

                    # Draw a bounding box rectangle and label on the frame
                    color = [int(c) for c in self.COLORS[class_ids[i]]]
                    cv2.rectangle(frame, (x, y), (x + w, y + h), color, 2)
                    text = "{}: {:4f}".format(self.LABELS[class_ids[i]],
                                              confidences[i])
                    cv2.putText(frame, text, (x, y - 5),
                                cv2.FONT_HERSHEY_SIMPLEX, 0.5, color, 2)

            # Save box information with frame number
            for box in boxes:
                writer_csv.writerow([frame_count] + box)

            # Check if the video writer is None
            if visual:
                if writer is None:
                    # Initialize video writer
                    fcc = cv2.VideoWriter_fourcc(*"MJPG")
                    writer = cv2.VideoWriter(path_avi, fcc, 30,
                                             (frame.shape[1], frame.shape[0]), True)

                    # Some information on processing single frame
                    if total > 0 and verbose:
                        elapsed = (end - start)
                        print("[AutoBoundingBox] single frame took {:4f} seconds".format(elapsed))
                        print("[AutoBoundingBox] estimated total time to finish: {:4f}".format(
                            elapsed * total / detect_rate))

                # Write the output frame to disk
                writer.write(frame)

        # Release the file pointers
        if verbose:
            print("[AutoBoundingBox] cleaning up...")
        writer.release()
        vs.release()
        file_csv.close()


if __name__ == '__main__':
    from glob import glob

    image_list = glob('ImageBounding/image/*')
    bb = AutoBoundingBox(verbose=True)

    cnt = 0
    for img in image_list:
        _, length = bb.bound_img(img, visual=True)
        cnt += length

    print(cnt)
    # json_list = bb.bound_img("image/frame_11000_11000.jpg", verbose=True, visual=True, save=True)
    # with open("output/living_room.txt", 'w') as jf:
    #     list_p = json.loads(json_list)
    #     json.dump(list_p, jf, indent=4)