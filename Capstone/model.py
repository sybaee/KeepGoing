import torch
import torch.nn as nn

class classification(nn.Module):
    def __init__(self, args):
        super(classification, self).__init__()
        self.input_size = args.input_size
        self.batch_size = args.batch_size
        self.hidden_size = args.class_hidden_size
        self.output_size = args.output_size

        self.middle_size1 = args.middle_size1 # 8
        self.middle_size2 = args.middle_size2 # 4
        
        self.hidden = self.initHidden()
        self.rnn = nn.LSTM(self.input_size, self.hidden_size, batch_first=True)
        
        self.fc1 = nn.Linear(self.hidden_size, self.middle_size1)  # linear 1
        self.fc2 = nn.Linear(self.middle_size1, self.middle_size2) # linear 2
        self.fc3 = nn.Linear(self.middle_size2, self.output_size)  # linear 3
        
        self.relu = nn.ReLU()
        self.softmax = nn.Softmax(dim=self.output_size)

    def forward(self, input):
        # B: batch, W: window_len
        output, __ = self.rnn(input, self.hidden) # B, W, H
        output = self.relu(self.fc1(output))      # B, W, M1
        output = self.relu(self.fc2(output))      # B, W, M2
        output = self.fc3(output)                 # B, W, 2 (output)

        # output_prob = self.softmax(output) # get probabilities

        return output

    def initHidden(self):
        hidden = torch.zeros(2, 1, self.batch_size, self.hidden_size, 
            requires_grad=True).to('cuda')

        return (hidden[0], hidden[1])

class hybrid(nn.Module):
    def __init__(self, args):
        super(hybrid, self).__init__()
        self.input_size = args.input_size
        self.batch_size = args.batch_size
        self.hidden_size = args.hybrid_hidden_size
        self.value_output_size = args.value_output_size
        self.date_output_size = args.date_output_size

        self.middle_size1 = args.middle_size1 # 50
        self.middle_size2 = args.middle_size2 # 30
        
        self.hidden = self.initHidden()
        self.rnn = nn.LSTM(self.input_size, self.hidden_size, batch_first=True)
        
        self.fc1 = nn.Linear(self.hidden_size, self.middle_size1)  # linear 1
        self.fc2 = nn.Linear(self.middle_size1, self.middle_size2) # linear 2

        self.value_lastFC = nn.Linear(self.middle_size2, self.value_output_size)
        self.date_lastFC = nn.Linear(self.middle_size2, self.date_output_size)
        
        self.relu = nn.ReLU()

    def forward(self, input):
        # B: batch, W: window_len
        output, __ = self.rnn(input, self.hidden) # B, W, H
        output = self.relu(self.fc1(output))      # B, W, M1
        output = self.fc2(output)                 # B, W, M2

        value_output = self.value_lastFC(output)
        date_output = self.date_lastFC(output)

        outputs = [value_output, date_output]

        return outputs

    def initHidden(self):
        hidden = torch.zeros(2, 1, self.batch_size, self.hidden_size, 
            requires_grad=True).to('cuda')

        return (hidden[0], hidden[1])