import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

class Kernel_Density:
    def __init__(self, kernel='gaussian', bandwidth=0.2):
        self.kernel = kernel
        self.h = bandwidth

    def fit(self, X):
        self.X = X
        self.N = X.shape[0]
        self.D = X.shape[1] 
    
        return self

    def estimator(self, X):       
        pdfs = np.zeros(X.shape[0])

        for i in range(pdfs.shape[0]):
            sigma = 0

            for xn in self.X:
                sigma += self.gaussian_kernel((X[i] - xn) / self.h)

            pdfs[i] = sigma / (self.N * np.power(self.h, self.D))

        return pdfs

    def gaussian_kernel(self, x):
        return np.power(2 * np.pi, - self.D / 2) * np.exp(- np.dot(x.T, x) / 2)
    
    def kernel_pdf(self, x):
        pdf = (1 / self.N) * np.sum(
            np.power(2 * np.pi * self.h**2, - self.D / 2) * np.exp(-((x - self.X)**2) / (2 * (self.h**2))))

        return pdf


if __name__ == "__main__":
    plt.style.use('seaborn-whitegrid')

    df = pd.read_csv('weight-height.csv')

    df_height_all = df.Height
    df_height_male = df.loc[df.Gender == 'Male', 'Height']
    df_height_female = df.loc[df.Gender == 'Female', 'Height']

    df_weight_all = df.Weight
    df_weight_male = df.loc[df.Gender == 'Male', 'Weight']
    df_weight_female = df.loc[df.Gender == 'Female', 'Weight']

    dic_dfs = {
        'Height All': df_height_all,
        'Height Male': df_height_male,
        'Height Female': df_height_female,
        'Weight All': df_weight_all,
        'Weight Male': df_weight_male,
        'Weight Female': df_weight_female
    }

    bin_counts = [50, 100, 200, 300]
    bandwidths =  [0.1, 0.5, 0.8, 1.0]
    # bandwidths = [1.0, 1.5, 1.8, 2.0]
    check_pdfs = False

    dict_pdfs = {}
    for xlabel, df in dic_dfs.items():
        print(xlabel)
        dict_pdfs[xlabel] = {}
        save_pdfs = True
        for bin_count in bin_counts:
            param = 'bins = {}'.format(bin_count)
            
            X = np.array(df).flatten()
            x_grid = np.linspace(min(X), max(X), len(X))
            mu = (1 / len(X)) * np.sum(X)
            sigma = np.sqrt((1 / len(X)) * np.sum((X - mu)**2))
            MLE = np.power(2 * np.pi * sigma**2, - 1 / 2) * np.exp(- (x_grid - mu)**2 / (2 * sigma**2))

            plt.figure(figsize=(10, 6))    
            plt.title('Density Estimation: ' + param, pad=10, fontsize=15)

            for bw in bandwidths:
                print(bin_count, bw)
                if save_pdfs:
                    kde = Kernel_Density(bandwidth=bw).fit(X[:, np.newaxis])
                    pdfs = kde.estimator(x_grid[:, np.newaxis]) # same with [kde.kernel_pdf(x) for x in x_grid]

                    dict_pdfs[xlabel][bw] = pdfs
                
                if check_pdfs:
                    print('kde smoothing: {}'.format(pdfs))
                    print('log-likelihood: {}'.format(np.log(pdfs)))

                # Kernel Density Estimation
                plt.plot(x_grid, dict_pdfs[xlabel][bw], label='bw={0}'.format(bw), alpha=0.5, linewidth=2)
            
            # Histogram
            plt.hist(X, bins=np.linspace(X.min(), X.max(), bin_count), 
                    density=True, ec='gray', fc='gray', alpha=0.4)
            # Gaussian Distribution with MLE
            plt.plot(x_grid, MLE, linewidth=2, label='GD') 
            plt.xlabel(xlabel, labelpad=10, fontsize=13)
            plt.legend(loc='best')
            plt.savefig('DensityEstimation_{}_{}.jpg'.format(xlabel, bin_count))
            plt.close()
            
            save_pdfs = False
            for bw in bandwidths:
                plt.figure(figsize=(10, 6))    
                plt.title('Density Estimation: ' + 'bins = {}, bw = {}'.format(bin_count, bw), pad=10, fontsize=15)
                # Histogram
                plt.hist(X, bins=np.linspace(X.min(), X.max(), bin_count), 
                        density=True, ec='gray', fc='gray', alpha=0.4)
                # Kernel Density Estimation
                plt.plot(x_grid, dict_pdfs[xlabel][bw], label='KDE', alpha=0.5, linewidth=2)
                # Gaussian Distribution with MLE
                plt.plot(x_grid, MLE, label='GD', alpha=0.5, linewidth=2)
                plt.xlabel(xlabel, labelpad=10, fontsize=13)
                plt.legend(loc='best')
                plt.savefig('DensityEstimation_{}_{}_{}.jpg'.format(xlabel, bin_count, bw))
                plt.close()