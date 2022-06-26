import six.moves.cPickle as pickle
import gzip
import os
import numpy as np
#import scipy.misc
from PIL import Image
from matplotlib import pyplot as plt

def load_data(dataset):
    # Download the MNIST dataset if it is not present
    data_dir, data_file = os.path.split(dataset)
    if data_dir == "" and not os.path.isfile(dataset):
        # Check if dataset is in the data directory
        new_path = os.path.join(
            os.path.split(__file__)[0], dataset)
        if os.path.isfile(new_path) or data_file == 'mnist.pkl.gz':
            dataset = new_path

    if not os.path.isfile(dataset) and data_file == 'mnist.pkl.gz':
        from six.moves import urllib
        origin = 'http://www.iro.umontreal.ca/~lisa/deep/data/mnist/mnist.pkl.gz'
        print('Downloading data from %s' % origin)
        urllib.request.urlretrieve(origin, dataset)

    print('... loading data')

    # Load the dataset
    with gzip.open(dataset, 'rb') as f:
        try:
            train_set, valid_set, test_set = pickle.load(f, encoding='latin1')
        except:
            train_set, valid_set, test_set = pickle.load(f)
            
    # train_set, valid_set, test_set format: tuple(input, target)
    # input is a numpy.ndarray of 2 dimensions (a matrix)
    # where each row corresponds to an example. target is a
    # numpy.ndarray of 1 dimension (vector) that has the same length as
    # the number of rows in the input. It should give the target
    # to the example with the same index in the input.

    return train_set, valid_set, test_set

def perform_data(dataset):
    eig_vals, eig_vecs = np.linalg.eig(dataset)
    for i in range(10):
        print('Eigen vector {}: {}'.format(i+1, eig_vecs[:, i]))
    
    plt.plot(range(100), eig_vals[:100])
    plt.show()

def calculate_PCA(data, samples, classes=None):
    exit = []

    mean = np.mean(data, axis=0)
    mean_centering = data - mean
    
    cov = np.cov(mean_centering, rowvar=0)
    
    eig_vals, eig_vecs = np.linalg.eig(np.mat(cov))
    eig_vals_sorted = np.argsort(-eig_vals)
    
    for num_eigen in samples:
        eig_vals_filtered = eig_vals_sorted[:num_eigen]
        eig_vecs_filtered = eig_vecs[:, eig_vals_filtered]
        
        data_matrix = mean_centering * eig_vecs_filtered
        reconstructed = (data_matrix*eig_vecs_filtered.T) + mean
        exit.append((num_eigen, reconstructed.real, eig_vecs_filtered.T.real))
        if classes is not None:
            for i in classes:
                mean_class = np.mean(classes[i], axis=0)
                mean_centering_class = classes[i] - mean_class
                data_matrix_class = mean_centering_class * eig_vecs_filtered
                reconstructed_class = (data_matrix_class*eig_vecs_filtered.T) + mean_class
                exit.append((num_eigen, reconstructed_class.real, None))

    return exit

if __name__ == '__main__':
    train_set, val_set, test_set = load_data('mnist.pkl.gz')

    train_x, train_y = train_set
    val_x, val_y = val_set
    test_x, test_y = test_set
    
    print(train_x.shape) # (50000, 784)
    print(train_y.shape) # (50000, )
    
    # for i in range(100):
    #     tmp_img = train_x[i].reshape((28,28))*255.9
    #     samp_img = Image.fromarray(tmp_img.astype(np.uint8))
    #     samp_img.save('test'+str(i)+'.jpg')
    #     print(train_y[i])

    mean_img = np.mean(train_x, axis=0)
    var_img = np.var(train_x, axis=0)
    
    print(train_x)
    print(np.sum(train_x <= 0.1))
    print(np.sum(train_x >= 0.9))
    print(mean_img.shape)

    plt.imshow(mean_img.reshape((28, 28))*255.9, cmap='gray')
    plt.savefig('mean.jpg')
    
    plt.imshow(var_img.reshape((28, 28))*255.9, cmap='gray')
    plt.savefig('variance.jpg')

    cov = np.cov(train_x.T)
    print(cov)
    print(cov.shape)

    eig_vals, eig_vecs = np.linalg.eig(cov)   
    for i in range(10):
        print('Eigen vector {}: {}'.format(i+1, eig_vecs[:, i]))
        plt.imshow(eig_vecs[i].reshape((28, 28))*255.9, cmap='gray')
        plt.savefig('eig_vec_' + str(i) + '.jpg')
        # eig_vec_img = eig_vecs[i].reshape((28, 28))*255.9
        # eig_vec_img = Image.fromarray(eig_vec_img.astype(np.uint8))
        # eig_vec_img.save('eig_vec_' + str(i) + '.jpg')
    
    plt.close()

    plt.plot(range(100), eig_vals[:100])
    plt.savefig('eig_vals.jpg')