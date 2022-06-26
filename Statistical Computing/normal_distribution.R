sample1 <- c(10, 20, 30, 50, 100)
eps <- c(0, 0.1, 0.2)

compNorm <- function(sam, eps) {
  res.mean = res.mean10 = res.mean20 = res.median = res.sd = c()
  total.mean = total.mean10 = total.mean20 = total.median = total.sd = list()
  
  p <- 0
  m <- 200
  for (i in 1:length(sam)) {
    for (j in 1:length(eps)) {
      for (k in 1:m) {
        n1 <- (1-eps[j]) * sam[i]                     # (1 - eps) * 100%  -> N(0, 1)   standard normal distribution
        n2 <- eps[j] * sam[i]                         # (100 * eps)%      -> N(0, 100) normal distribution
        
        x1 <- rnorm(n1, mean = 0, sd = 1)
        x2 <- rnorm(n2, mean = 0, sd = 10)
        
        x <- append(x1, x2)
        
        res.mean[k] <- mean(x)                        # sample mean
        res.mean10[k] <- mean(x, trim = 0.1)          # 10% trimmed mean
        res.mean20[k] <- mean(x, trim = 0.2)          # 20% trimmed mean
        res.median[k] <- median(x)                    # median
        res.sd[k] <- sd(x)                            # standard deviation
      }
      p <- p + 1
      total.mean[[p]] <- res.mean; total.mean10[[p]] <- res.mean10; total.mean20[[p]] <- res.mean20
      total.median[[p]] <- res.median; total.sd[[p]] <- res.sd
      
      boxplot(res.mean, res.mean10, res.mean20, res.median, res.sd, 
              main = paste0("N: ", sam[i], " & eps: ", eps[j]),
              col = c(2, 3, 4, 5, 6),
              names = c("Mean", "10% T.Mean", "20% T.Mean", "Median", "S.d"), las = 1)
    }
  }
  
  boxplot(total.mean[[1]], total.mean[[2]], total.mean[[3]], total.mean[[4]], total.mean[[5]],
          total.mean[[6]], total.mean[[7]], total.mean[[8]], total.mean[[9]], total.mean[[10]],
          total.mean[[11]], total.mean[[12]], total.mean[[13]], total.mean[[14]], total.mean[[15]],
          main = "Mean", col = c(2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6),
          names = c("10/0", "10/0.1", "10/0.2", "20/0", "20/0.1", "20/0.2",
                    "30/0", "30/0.1", "30/0.2", "50/0", "50/0.1", "50/0.2",
                    "100/0", "100/0.1", "100/0.2"), las = 1)
  
  boxplot(total.mean10[[1]], total.mean10[[2]], total.mean10[[3]], total.mean10[[4]], total.mean10[[5]],
          total.mean10[[6]], total.mean10[[7]], total.mean10[[8]], total.mean10[[9]], total.mean10[[10]],
          total.mean10[[11]], total.mean10[[12]], total.mean10[[13]], total.mean10[[14]], total.mean10[[15]],
          main = "10% Trimmed Mean", col = c(2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6),
          names = c("10/0", "10/0.1", "10/0.2", "20/0", "20/0.1", "20/0.2",
                    "30/0", "30/0.1", "30/0.2", "50/0", "50/0.1", "50/0.2",
                    "100/0", "100/0.1", "100/0.2"), las = 1)
  
  boxplot(total.mean20[[1]], total.mean20[[2]], total.mean20[[3]], total.mean20[[4]], total.mean20[[5]],
          total.mean20[[6]], total.mean20[[7]], total.mean20[[8]], total.mean20[[9]], total.mean20[[10]],
          total.mean20[[11]], total.mean20[[12]], total.mean20[[13]], total.mean20[[14]], total.mean20[[15]],
          main = "20% Trimmed Mean", col = c(2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6),
          names = c("10/0", "10/0.1", "10/0.2", "20/0", "20/0.1", "20/0.2",
                    "30/0", "30/0.1", "30/0.2", "50/0", "50/0.1", "50/0.2",
                    "100/0", "100/0.1", "100/0.2"), las = 1)
  
  boxplot(total.median[[1]], total.median[[2]], total.median[[3]], total.median[[4]], total.median[[5]],
          total.median[[6]], total.median[[7]], total.median[[8]], total.median[[9]], total.median[[10]],
          total.median[[11]], total.median[[12]], total.median[[13]], total.median[[14]], total.median[[15]],
          main = "Median", col = c(2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6),
          names = c("10/0", "10/0.1", "10/0.2", "20/0", "20/0.1", "20/0.2",
                    "30/0", "30/0.1", "30/0.2", "50/0", "50/0.1", "50/0.2",
                    "100/0", "100/0.1", "100/0.2"), las = 1)
  
  boxplot(total.sd[[1]], total.sd[[2]], total.sd[[3]], total.sd[[4]], total.sd[[5]],
          total.sd[[6]], total.sd[[7]], total.sd[[8]], total.sd[[9]], total.sd[[10]],
          total.sd[[11]], total.sd[[12]], total.sd[[13]], total.sd[[14]], total.sd[[15]],
          main = "Standard Deviation", col = c(2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6),
          names = c("10/0", "10/0.1", "10/0.2", "20/0", "20/0.1", "20/0.2",
                    "30/0", "30/0.1", "30/0.2", "50/0", "50/0.1", "50/0.2",
                    "100/0", "100/0.1", "100/0.2"), las = 1)
  
}

compNorm(sample1, eps)