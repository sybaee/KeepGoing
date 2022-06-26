import time
import requests
import pandas as pd

def get_RT_data(link):
    start_time = time.time()
    print('it starts now')
    f = requests.get(link)
    
    stock_data = pd.DataFrame(f.text)
    stock_data['S_E'] = stock_data['Open'] / stock_data['Adj Close']
    stock_data['L_E'] = stock_data['Low'] / stock_data['Adj Close']
    stock_data['H_E'] = stock_data['High'] / stock_data['Adj Close']
    stock_data['MA5'] = stock_data['Adj Close'].rolling(window=5).mean()
    stock_data['MA10'] = stock_data['Adj Close'].rolling(window=10).mean()
    
    stock_data = stock_data.dropna().reset_index(drop=True)
    stock_data.drop(columns=['Date', 'Time'], inplace=True)

    print('it took', time.time() - start_time)

    return stock_data