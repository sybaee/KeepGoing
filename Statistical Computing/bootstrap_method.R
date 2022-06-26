# set.seed(1207)                                  # to reproduce the same result

# Find the bootstrap sample means and 95% bootstrap confidence intervals
bootstrap <- function(sample.data, B) {
  boot.dist <- vector(length = B)                 # sample size   
  N <- length(sample.data)
  
  for (i in 1:B) {
    # Generate 5, 10, 15, 30, 100 random samples from N(0, 1)
    boot.sample  <- sample(x = sample.data, size = N, replace = TRUE)
    boot.dist[i] <- mean(boot.sample)             # bootstrap sample mean
  }
  
  boot.dist = sort(boot.dist)
  
  return(boot.dist)
}

# Compare with the usual sample mean and 95% confidence intervals
B <- 10000
N <- c(5, 10, 15, 30, 100)

for (i in N) {
  sample.data <- rnorm(i, mean = 0, sd = 1)
  boot.dist <- bootstrap(sample.data, B)
  
  hist(boot.dist, prob = TRUE, main = "Bootstrap", 
       xlab = paste0("Sample size: ", i, " with N(0, 1)"), col = "grey", border = "white")
  
  cat(paste0("\n\nSample size: ", i))
  cat("\nBootstrap mean\n")
  cat(mean(boot.dist))
  
  cat("\nUsual sample mean\n")
  cat(mean(sample.data))
  
  cat("\nBootstrap 95% confidence intervals\n")
  cat(quantile(boot.dist, c(0.025, 0.975)))

  cat("\nUsual sample 95% confidence intervals\n")
  cat(c(mean(sample.data) - 1.96*sd(sample.data)/sqrt(i), 
        mean(sample.data) + 1.96*sd(sample.data)/sqrt(i)))
}