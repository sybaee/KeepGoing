import torch
import torch.nn as nn
import torch.optim as optim

from data import FSIterator
from sklearn.metrics import recall_score, f1_score, precision_score

VALUE = 0
DATE = 1

def class_test(args, model):
    with torch.no_grad():
        test_iter = FSIterator(args)
        result = []

        for input, _ in test_iter:
            output = model(input)
            output = output[:, -1, :] # B, Len, 2

            # find max index from output ([32])
            result.extend(torch.max(output.data, 1)[1].cpu().numpy())

    return result

def hybrid_test(args, model):
    with torch.no_grad():
        test_iter = FSIterator(args)
        sell_output = []

        for input, normalize_factor in test_iter:
            output = model(input)
            output[VALUE] = output[VALUE][:, -1, :]
            output[DATE] = output[DATE][:, -1, :]
            
            max_output = torch.max(output[DATE].data, 1)[1]
            sell_output.extend(max_output.cpu().numpy())
            
    return sell_output



