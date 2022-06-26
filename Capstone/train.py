import pandas as pd
import numpy as np

import torch
import torch.nn as nn
import torch.optim as optim

from data import FSIterator
from sklearn.metrics import recall_score, f1_score, precision_score

eval_list = ['TP', 'TN', 'FP', 'FN']

def train_main(args, model, train_path, criterion, optimizer):
    iloop = 0
    current_loss = 0
    all_losses = []

    train_iter = FSIterator(train_path, args)

    for input, target, _, _ in train_iter:
        loss = train(args, model, input, target, optimizer, criterion)
        current_loss += loss

        if (iloop+1) % args.logInterval == 0:
            print('%d %.4f' % (iloop+1, current_loss / args.logInterval))
            all_losses.append(current_loss / args.logInterval)
            current_loss = 0

        iloop += 1

def train(args, model, input, target, optimizer, criterion):
    model = model.train()
    optimizer.zero_grad()
    output, _ = model(input)

    output = output[:, -1, :]
    loss = criterion(output.squeeze(), target.squeeze())
    loss.backward()
    optimizer.step()

    return loss.item()

def index_collect(args, output, target, index, criterion):
    loss = dict.fromkeys(list(set(index)), 0)
    separated_output = dict.fromkeys(list(set(index)), None)
    separated_target = dict.fromkeys(list(set(index)), None)
    num = dict.fromkeys(list(set(index)), 0)

    i_result = torch.max(output.data, 1)[1] # find max index from output ([32])
    for i in range(len(index)):
        i_loss = criterion(output.squeeze()[i:i+1], target.squeeze()[i:i+1])
        loss[index[i]] += i_loss.tolist()

        try:
            separated_output[index[i]].append(i_result.tolist()[i])
            separated_target[index[i]].append(target.squeeze().tolist()[i])
            num[index[i]] += 1

        except:
            separated_output[index[i]] = [i_result.tolist()[i]]
            separated_target[index[i]] = [target.squeeze().tolist()[i]]
            num[index[i]] += 1

    confusion_matrix = get_confusion(separated_output, separated_target, index)
    total_loss = criterion(output.squeeze(), target.squeeze())

    return total_loss, confusion_matrix, num

def get_confusion(predict, target, r_index):
    matrix = dict.fromkeys(list(set(r_index)))

    for index in set(r_index):
        matrix[index] = {'TP': 0, 'TN': 0, 'FP': 0, 'FN': 0}
    
        p = np.array(predict[index]) 
        t = np.array(target[index]) 

        matrix[index]['TP'] = np.sum((p == t) * t)        # target: 1  predict = 1
        matrix[index]['TN'] = np.sum((p == t) * (t== 0))  # target: 0  predict = 0
        matrix[index]['FP'] = np.sum((p != t) * p)        # target: 0  predict = 1
        matrix[index]['FN'] = np.sum((p != t) * (p == 0)) # target: 1  predict = 0

    return matrix

def performance(i_confusion_matrix, i_num):
    i_accuracy, i_recall, i_precision, i_f1 = {}, {}, {}, {}
    accuracy, recall, precision, f1 = 0, 0, 0, 0
    
    index_num = len(i_num.keys())
    for i in range(index_num):
        TP = i_confusion_matrix[i]['TP']
        TN = i_confusion_matrix[i]['TN']
        FP = i_confusion_matrix[i]['FP']
        FN = i_confusion_matrix[i]['FN']

        i_accuracy[i] = (TP+TN) / (TP+TN+FP+FN)
        accuracy += i_accuracy[i] 
        i_recall[i] = TP / (TP+FN)
        recall += i_recall[i] 

        i_precision[i] = TP / (TP+FP)
        precision += i_precision[i] 

        i_f1[i] = 2 * (i_precision[i]*i_recall[i]) / (i_precision[i]+i_recall[i])
        f1 += i_f1[i] 

    accuracy /= index_num
    recall /= index_num
    precision /= index_num
    f1 /= index_num

    scores = [accuracy, recall, precision, f1]
    i_scores = [i_accuracy, i_recall, i_precision, i_f1]

    return scores, i_scores

def test(args, model, test_path, criterion):
    loss = 0

    i_num, i_confusion_matrix = {}, {}
    r_num , r_confusion_matrix = {}, {}
    output_probs = []

    model = model.eval()

    with torch.no_grad():
        iloop = 0
        test_iter = FSIterator(test_path, args)

        for input, target, index, rindex in test_iter: # model in here
            output, output_prob = model(input)
            output = output[:, -1, :]
            output_prob = output_prob[:, -1, :][:, 1].tolist()

            r_loss, _r_confusion_matrix, _r_num = index_collect(
                args, output, target, rindex, criterion)

            _, _i_confusion_matrix, _i_num = index_collect(
                args, output, target, index, criterion)

            for i in set(rindex):
                try:
                    for j in eval_list:
                        r_confusion_matrix[i][j] += _r_confusion_matrix[i][j]
                    r_num[i] += _r_num[i]

                except:
                    r_confusion_matrix[i] = {'TP': 0, 'TN': 0, 'FP': 0, 'FN': 0}
                    for j in eval_list:
                        r_confusion_matrix[i][j] = _r_confusion_matrix[i][j]

                    r_num[i] = _r_num[i]
        
            for i in set(index):
                try:
                    for j in eval_list:
                        i_confusion_matrix[i][j] += _i_confusion_matrix[i][j]
                    i_num[i] += _i_num[i]

                except:
                    i_confusion_matrix[i] = {'TP': 0, 'TN': 0, 'FP': 0, 'FN': 0}
                    for j in eval_list:
                        i_confusion_matrix[i][j] = _i_confusion_matrix[i][j]

                    i_num[i] = _i_num[i]
            
            loss += r_loss
            iloop += 1
            output_probs.extend(output_prob)

        loss /= iloop
        result, i_result = performance(i_confusion_matrix, i_num)
        _, r_result = performance(r_confusion_matrix, r_num)
    
    return loss, result, r_result, i_result, output_probs