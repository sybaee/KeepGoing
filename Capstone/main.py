from __future__ import print_function
import os
import sys
import time
import math
import argparse

import pandas as pd
import numpy as np

import torch
import torch.nn as nn
import torch.optim as optim

from model import RNN
from train import train_main, test

import neptune

"""argument setting"""
parser = argparse.ArgumentParser()
parser.add_argument('--logInterval', type=int, default=100, help='')

parser.add_argument('--trainPath', type=str,
                    default='../data/0723/classification/train', help='')
parser.add_argument('--testPath', type=str,
                    default='../data/0723/classification/test', help='')
parser.add_argument('--validPath', type=str,
                    default='../data/0723/classification/valid', help='')

parser.add_argument('--max_epochs', type=int, default=100, help='')
parser.add_argument('--batch_size', type=int, default=32, help='')
parser.add_argument('--input_size', type=int, default=20, help='')
parser.add_argument('--hidden_size', type=int, default=32, help='')
parser.add_argument('--middle_size1', type=int, default=8, help='')
parser.add_argument('--middle_size2', type=int, default=4, help='')
parser.add_argument('--window_size', type=int, default=15, help='')
parser.add_argument('--output_size', type=int, default=2, help='')

parser.add_argument('--patience', type=int, default=5, help='')
parser.add_argument('--optim', type=str, default='Adam')
parser.add_argument('--lr', type=float, metavar='LR', default=0.001,
                    help='learning rate (no default)')
parser.add_argument('--device', type=str, metavar='cuda', default='cuda')

parser.add_argument('--saveModel', type=str, default='bestmodel', help='')
args = parser.parse_args()

"""neptune setting"""
neptune.init('Finset/sandbox')
neptune.create_experiment(name='classificaton',
                          params={"trainpath": args.trainPath,
                                  "testpath": args.testPath,
                                  "validpath": args.validPath,
                                  "max_epochs": args.max_epochs,
                                  "batch_size": args.batch_size,
                                  "window_size": args.window_size,
                                  "hidden_size": args.hidden_size,
                                  "output_size": args.output_size,
                                  "patience": args.patience,
                                  "optim": args.optim,
                                  "learning rage": args.lr},
                          description='classification - weighted loss (10)',
                          tags=['classification'])
neptune.append_tag(time.strftime('%Y-%m-%d %H:%M:%S', 
                   time.localtime(time.time())))
neptune.append_tag("0723data")

if __name__ == "__main__":   
    # prepare data
    batch_size = args.batch_size
    n_epoches = args.max_epochs
    device = torch.device(args.device)

    # set up model
    print('Set model')
    model = RNN(args).to(device)

    # define loss
    print('Set loss and Optimizer')
    criterion = nn.CrossEntropyLoss()

    # define optimizer
    optimizer = 'optim.' + args.optim
    optimizer = eval(optimizer)(model.parameters(), lr=args.lr)

    print('Train start')
    best_loss = -1.0
    bad_counter = 0

    eNum = 0

    loss, _, _, _, _ = test(args, model, args.validPath, criterion)
    neptune.log_metric('valid loss', loss)

    for ei in range(n_epoches):
        print('Epoch: ' + str(ei+1))
        eNum += 1

        # train
        train_main(args, model, args.trainPath, criterion, optimizer)

        # valid test
        loss, result, r_result, i_result, _ = test(
            args, model, args.validPath, criterion)
        
        neptune.log_metric('valid loss', loss)
        print('valid loss : {}'.format(loss.tolist()))

        if loss < best_loss or best_loss < 0:
            print('find best')
            best_loss = loss
            bad_counter = 0

            torch.save(model.state_dict(), args.saveModel)
            
        else:
            bad_counter += 1

        if bad_counter > args.patience:
            print('Early Stopping')
            break

    print('-----------------test-----------------')
    loss, result, r_result, i_result, output_probs = test(
        args, model, args.testPath, criterion)

    # to check correlation with peak values
    pd.DataFrame(output_probs).to_csv(
        'output_probs.csv', header=False, index=False)
    print("test loss : {:.2f}".format(loss.tolist()))
    print("test accuracy : {:.2f}".format(result[0]))
    print("test recall : {:.2f}".format(result[1]))
    print("test precision : {:.2f}".format(result[2]))
    print("test f1 : {:.2f}".format(result[3]))
    print("Output per window is saved in neptune")

    neptune.log_metric('test loss', loss)
    for i in range(len(i_result[0].keys())):
        neptune.log_metric('accuracy',  i_result[0][i])
        neptune.log_metric('recall',  i_result[1][i])
        neptune.log_metric('precision',  i_result[2][i])
        neptune.log_metric('f1',  i_result[3][i])

    for i in range(len(r_result[0].keys())):
        neptune.log_metric('r_accuracy',  r_result[0][i])
        neptune.log_metric('r_recall',  r_result[1][i])
        neptune.log_metric('r_precision', r_result[2][i])
        neptune.log_metric('r_f1',  r_result[3][i])