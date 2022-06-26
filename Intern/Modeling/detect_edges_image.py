# USAGE
# python detect_edges_image.py --edge-detector hed_model --image images/guitar.jpg

# import the necessary packages
import numpy as np
import argparse
import cv2
import os

# for AutoBoundingBox
import csv
import time
import json
import imutils

from glob import glob
from object_detection import AutoBoundingBox

# construct the argument parser and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-d", "--edge-detector", type=str, required=True,
	help="path to OpenCV's deep learning edge detector")
ap.add_argument("-i", "--image", type=str, required=True,
	help="path to input image")
args = vars(ap.parse_args())

class CropLayer(object):
	def __init__(self, params, blobs):
		# initialize our starting and ending (x, y)-coordinates of
		# the crop
		self.startX = 0
		self.startY = 0
		self.endX = 0
		self.endY = 0

	def getMemoryShapes(self, inputs):
		# the crop layer will receive two inputs -- we need to crop
		# the first input blob to match the shape of the second one,
		# keeping the batch size and number of channels
		(inputShape, targetShape) = (inputs[0], inputs[1])
		(batchSize, numChannels) = (inputShape[0], inputShape[1])
		(H, W) = (targetShape[2], targetShape[3])

		# compute the starting and ending crop coordinates
		self.startX = int((inputShape[3] - targetShape[3])/2)
		self.startY = int((inputShape[2] - targetShape[2])/2)
		self.endX = self.startX + W
		self.endY = self.startY + H

		# return the shape of the volume (we'll perform the actual
		# crop during the forward pass
		return [[batchSize, numChannels, H, W]]

	def forward(self, inputs):
		# use the derived (x, y)-coordinates to perform the crop
		return [inputs[0][:, :, self.startY:self.endY, self.startX: self.endX]]

def cropBbox(image):
    # get Bbox and crop
    abb = AutoBoundingBox(confidence=0.85, verbose=True)
    coordinates, __ = abb.bound_img(image, visual=True)

    coordinates = json.loads(coordinates)
    crop_set = list()

    for bbox in coordinates:
        x = bbox['bounding_box']['x']
        y = bbox['bounding_box']['y']
        w = bbox['bounding_box']['width']
        h = bbox['bounding_box']['height']
        crop_set.append((y, y+h, x, x+w))

    return crop_set

def autoCanny(image, sigma=0.33):
	# compute the median of the single channel pixel intensities
	v = np.median(image)

	# apply automatic Canny edge detection using the computed median
	lower = int(max(0, (1.0-sigma) * v))
	upper = int(min(255, (1.0+sigma) * v))
	edged = cv2.Canny(image, lower, upper)

	# return the edged image
	return edged

def findCoordinate(val, coor):
    operations = [min, max]
    if val == 0:
        op = max
        coor1 = coor - 20; coor2 = coor - 10; coor3 = coor - 5    
    else:
        op = min
        coor1 = coor + 40; coor2 = coor + 20; coor3 = coor + 10   

    if op(coor3, val) == coor3:
        if op(coor2, val) == coor2:
            if op(coor1, val) == coor1: point = coor1
            else: point = coor2

        else: point = coor3

    else: point = coor
    
    return point

# load our serialized edge detector from disk
print("[INFO] loading edge detector...")
protoPath = os.path.sep.join([args["edge_detector"],
	"deploy.prototxt"])
modelPath = os.path.sep.join([args["edge_detector"],
	"hed_pretrained_bsds.caffemodel"])
net = cv2.dnn.readNetFromCaffe(protoPath, modelPath)

# register our new layer with the model
cv2.dnn_registerLayer("Crop", CropLayer)

# load the input image
image = cv2.imread(args["image"])
(h, w) = image.shape[:2]
bbox_set = cropBbox(args["image"])

for i in range(len(bbox_set)):
	y = bbox_set[i][0]; yh = bbox_set[i][1]
	x = bbox_set[i][2]; xw = bbox_set[i][3]

	y_coor = findCoordinate(0, y)
	yh_coor = findCoordinate(h, yh)    
	x_coor = findCoordinate(0, x)
	xw_coor = findCoordinate(w, xw)

	bbox_img = image[y_coor : yh_coor, x_coor : xw_coor]
	
	'''
	gray = cv2.cvtColor(bbox_img, cv2.COLOR_BGR2GRAY)
	blurred = cv2.GaussianBlur(gray, (3, 3), 0)

	# apply Canny edge detection using a wide threshold, tight
	# threshold, and automatically determined threshold
	print("[INFO] performing Canny edge detection...")
	auto = autoCanny(blurred)
	'''

	# construct a blob out of the input image for the Holistically-Nested
	# Edge Detector
	(bH, bW) = bbox_img.shape[:2]
	blob = cv2.dnn.blobFromImage(bbox_img, scalefactor=1.0, size=(bW, bH),
		mean=(104.00698793, 116.66876762, 122.67891434),
		swapRB=False, crop=False)

	# set the blob as the input to the network and perform a forward pass
	# to compute the edges
	print("[INFO] performing Holistically-nested edge detection...")
	net.setInput(blob)
	hed = net.forward()
	hed = cv2.resize(hed[0, 0], (bW, bH))
	hed = (255 * hed).astype("uint8")
	cv2.imwrite('images/sample.jpg', cv2.cvtColor(hed, cv2.COLOR_GRAY2RGB))