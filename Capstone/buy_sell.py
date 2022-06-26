from __future__ import print_function
import argparse

import pandas as pd
import numpy as np

import torch
import torch.nn as nn
import torch.optim as optim

from model import classification as buy_model
from model import hybrid as sell_model
from test import class_test as buy_test
from test import hybrid_test as sell_test

parser = argparse.ArgumentParser()
parser.add_argument('--path', type=str, default="./data/", help='')
parser.add_argument('--link', type=str, 
    default='http://203.252.121.221:8000/get_data_all/', help='')
parser.add_argument('--buy_param_path', type=str, 
    default="./buy_classification", help='')
parser.add_argument('--sell_param_path', type=str, 
    default="./sell_classification", help='')
parser.add_argument('--hybrid_param_path', type=str, 
    default="./hybrid_model", help='')
                    
parser.add_argument('--batch_size', type=int, default=1, help='')
parser.add_argument('--input_size', type=int, default=20, help='')
parser.add_argument('--input_size2', type=int, default=22, help='')
parser.add_argument('--window_size', type=int, default=15, help='')

parser.add_argument('--class_hidden_size', type=int, default=32, help='')
parser.add_argument('--hybrid_hidden_size', type=int, default = 75, help='')  # TODO size 50, 75, 100

parser.add_argument('--middle_size1', type=int, default=8, help='')
parser.add_argument('--middle_size2', type=int, default=4, help='')

parser.add_argument('--output_size', type=int, default=2, help='')
parser.add_argument('--value_output_size', type=int, default=1, help='')
parser.add_argument('--date_output_size', type=int, default=2, help='')

parser.add_argument('--device', type=str, metavar='cuda', default='cuda')
args = parser.parse_args()

# buy_model = buy_model(args)
# buy_model.load_state_dict(torch.load(args.buy_param_path))
# buy_model.to(args.device)
# buy_model.eval()

# sell_model = sell_model(args)
# sell_model.load_state_dict(torch.load(args.sell_param_path))
# sell_model.to(args.device)
# sell_model.eval()

# hybrid_model = hybrid(args)
# hybrid_model.load_state_dict(torch.load(args.hybrid_param_path))
# hybrid_model.to(args.device)
# hybrid_model.eval()

buy_reuslt = buy_test(args, buy_model)
sell_result = sell_test(args, sell_model)

# hybrid_result= hybrid_test(args, hybrid_model, args.path)
# codes = list(map(lambda x:str(int(x)).zfill(6), codes))

print('complete!')
result = pd.DataFrame({'buy': buy_reuslt, 'sell': sell_result})
result.to_csv('result.csv', index=False)