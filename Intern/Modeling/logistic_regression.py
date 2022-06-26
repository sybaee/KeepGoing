import neptune
neptune.init('aolovely/sandbox')
neptune.create_experiment(name='Logistic Regression')

import pandas as pd
import numpy as np
import seaborn as sns
import matplotlib.pyplot as plt
plt.style.use('seaborn-whitegrid')

import statsmodels.api as sm

from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split

def trinary_labeling(df, rate1, rate2):
    df['LABEL'] = df['RATE_WeekDiff'].apply(
        lambda x: 1 if x >= rate1 else (0 if x >= rate2 else -1))

    label = np.array(df['LABEL'])
    print('Total count: ' + str(len(label)))
    
    label_count = []
    label_count.append(np.where(label == -1)[0].shape[0])
    print('Down count: ' + str(label_count[0]))
    
    label_count.append(np.where(label == 0)[0].shape[0])
    print('Stay count: ' + str(label_count[1]))
        
    label_count.append(np.where(label == 1)[0].shape[0])
    print('Up count: ' + str(label_count[2]))

    return df, label_count

if __name__ == "__main__":
    df_sales = pd.read_csv('./data/df_sales.csv')

    rate1 = 0.25
    rate2 = -0.1
    df_sales, label_count = trinary_labeling(df_sales, rate1, rate2)

    figure_tf = False

    if figure_tf:
        sns.stripplot(x="DEMAND_WeekSum", y="LABEL", data=df_sales,
                    jitter=True, orient='h', order=[-1, 0, 1])
        
        plt.legend(label_count)
        plt.savefig('./figure/Demand_{}_{}_dist.png'.format(rate1, rate2))

        sns.stripplot(x="RATE_WeekDiff", y="LABEL", data=df_sales,
                    jitter=True, orient='h', order=[-1, 0, 1])
        
        plt.legend(label_count)
        plt.savefig('./figure/Label_{}_{}_dist.png'.format(rate1, rate2))

    X = df_sales.iloc[:, :-3]
    Y = df_sales['LABEL']

    X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.2)

    print('\ntrain starts')
    log_clf = LogisticRegression().fit(X_train, Y_train) 
    print(log_clf.score(X_test, Y_test))