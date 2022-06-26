import pdb
import numpy as np

import torch

"""constant"""
END = 0
H_E = 1
HIGH = 2
L_E = 3
LOW = 4
MA5 = 5
MA10 = 6
S_E = 7
START = 8
TURNOVER = 9
Y = 10
XnY_NUM = 11

INDEX = 0
RINDEX = 1
INDEX_NUM = 2

START_FROM = 2 # cut the front of data 

class FSIterator:
    def __init__(self, filename, args):
        self.fps = []
        kinds = ['END', 'H_E', 'HIGH', 'L_E', 'LOW', 
                 'MA5', 'MA10', 'S_E', 'START', 'TURNOVER']
        self.batch_size = args.batch_size
        self.window_size = args.window_size
        
        for i in range(len(kinds)): # read file
            self.fps.append(open(filename + '/' + kinds[i] + '.csv', 'r'))
        
        self.windows = [[] for _ in range(XnY_NUM)]
        self.index_windows = [[] for _ in range(INDEX_NUM)] # index, rindex 

        self.start_index = 0
        self.touch_end = False

        i = 0
        while True: # until file ends
            # i += 1
            # if i > 10000: break

            lines = []
            for i in range(len(self.fps)):
                lines.append(self.fps[i].readline()) # read line (one rapid stock)
            
            if '' == lines[0]: break # last file

            seqs = []
            for i in range(len(lines)):
                seqs.append([float(s) for s in lines[i].split(',')])

            same = True # to check error in data file
            num = sum(~np.isnan(seqs[0]))
            for i in range(len(seqs)): # check the size of data. line length should be same
                if sum(~np.isnan(seqs[i])) != num:
                    same = False
            
            if same:
                windows_ = [] # find zero division, checking in windows_
                try:
                    for i in range(len(seqs)):
                        # START_FROM: adjust intro day (use intro day, or just rapid stock)
                        windows_.append(self.windowing(seqs[i][START_FROM:]))

                    for i in range(len(windows_)):
                        self.windows[i] += windows_[i]

                    ywindow, index, rindex \
                        = self.YnIndex_windowing(seqs[END][START_FROM:])
                    self.windows[Y] += ywindow
                    self.index_windows[INDEX] += index
                    self.index_windows[RINDEX] += rindex

                except ZeroDivisionError:
                    # when? TURNOVER
                    pass
        
    def __iter__(self):
        return self

    def __next__(self): # pass x_data, y_data, index_data, rindex_data
        start_index = self.start_index
        device = torch.device('cuda')

        if self.touch_end:
            raise StopIteration

        touch_index = len(self.windows[END]) # small frag
        if start_index+self.batch_size > touch_index:
            start_index = touch_index - self.batch_size
            self.touch_end = True

        x_data, y_data, index_data, rindex_data = self.prepare_data(start_index)
        x_data = torch.tensor(x_data).type(torch.float32).to(device)
        y_data = torch.tensor(y_data).type(torch.LongTensor).to(device)

        self.start_index += self.batch_size

        """
            x_data      : [32, 15, 20] (Batch, Window, Factors)
            y_data      : [32, 1] 0, 1
            index_data  : batch size 32
            rindex_data : batch size 32
        """

        return x_data, y_data, index_data, rindex_data

    def reset(self):
        for fp in self.fps:
            fp.seek(0)

    def windowing(self, line): # X value windowing with normalize
        line_window = []
        window_len = self.window_size + 1 # 15
        
        for index in range(self.getSeq_len(line[:-1]) - window_len):
            x = line[index : index+window_len]
            a = [(item-x[0]) / x[0] for item in x] # normalize
            line_window.append(a)

        return line_window

    def YnIndex_windowing(self, line): # return index, rindex and y value
        line_window = []
        i_line_window = []
        r_line_window = []

        window_len = self.window_size + 1 # 15 + 1
        for index in range(self.getSeq_len(line[:-1]) - window_len):
            line_window.append([line[-1]])
            i_line_window.append(index)
            r_line_window.append(self.getSeq_len(line[:-1]) - window_len - index - 1)

        return line_window, i_line_window, r_line_window 

    def getSeq_len(self, row): # retrun non-nans numbers in row
        """
        returns: count of non-nans (integer)
        adopted from: M4rtni's answer in stackexchange
        """
        return np.count_nonzero(~np.isnan(row))

    def prepare_data(self, start_index):
        # make delta and change the dimension for model, cut bathsize length data
        PRE_STEP = 1
        seq_x = [[] for _ in range(XnY_NUM-1)] # -1 is to remove y 
        seq_xd = [[] for _ in range(XnY_NUM-1)] # -1 is to remove y 
        seq_index = [[] for _ in range(INDEX_NUM)] # index, rindex 
        
        for i in range(len(self.windows)-1):
            seq_x[i] = np.array(self.windows[i][
                start_index : start_index+self.batch_size]) 
        
        seq_y = np.array(self.windows[Y][
            start_index : start_index+self.batch_size])

        for i in range(len(self.index_windows)):
            seq_index[i] = np.array(self.index_windows[i][
                start_index : start_index+self.batch_size])

        for i in range(len(self.windows)-1):
            seq_xd[i] = np.array(seq_x[i][:, 1:] - seq_x[i][:, :-1])
        
        x_data = []
        try:
            # seq_x -> 10, B, 16
            # 10, 30, 15 -> batch * daylen * inputdim(2)
            for i in range(10):
                x_data.extend([seq_x[i][:, 1:], seq_xd[i][:, :]])

            x_data = np.transpose(np.array(x_data), [1, 2, 0])

        except:
            print('error 2')

        y_data = seq_y
        
        index_data = seq_index[0]
        rindex_data = seq_index[1]

        return x_data, y_data, index_data, rindex_data


if __name__ == "__main__":
    import argparse

    parser = argparse.ArgumentParser()
    parser.add_argument('--batch_size', type=int, default=32, help='')
    parser.add_argument('--window_size', type=int, default=15, help='')
    args = parser.parse_args()
    
    filename = '../data/0723/classification/test'
    train_iter = FSIterator(filename, args)
    
    for input, target, index, rindex in train_iter: # for debugging
        # pdb.set_trace()
        pass