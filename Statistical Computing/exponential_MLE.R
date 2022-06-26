# set.seed(1207)
sample1 <- c(5, 15, 30, 50, 100)

f_mle <- function(x, lambda) {
  sum(-dexp(x = x, rate = lambda, log = TRUE))
}

for (n in sample1) {
  print(paste0("n = ", n))
  
  p <- rexp(n, 0.1)                                 # mean = (0.1) * n
  
  print(paste0("1/x_bar = ", 1/mean(p)))
  
  hist(p, breaks = 10, main = paste0("Histrogram of rexp(", n, ", 1)"))

  MLE <- optimize(f = f_mle, x = p, interval = c(0, n-1))$minimum
  print(paste0("MLE = ", MLE))
  print(" ")
}