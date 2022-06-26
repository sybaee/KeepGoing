Conf.uniform <- function (n, nt, a, b) { # n=sample size, nt=number of relications
    par(pin = c(6,3))
      trial = rep(1, nt) # trial=0 if mu is not in the CI, trial=1 if mu is in the CI
      t.val <- qt(0.975, n-1)
      
      x <- matrix (runif(n*nt, a, b), nrow = nt)
      xbar <- apply (x, 1, mean)
      xvar <- apply (x, 1, var)
      limit <- t.val * sqrt(xvar/n)
      xbar <- xbar - mu
      trial[abs(xbar) > limit] = 0
      trial <- cumsum(trial) / 1:nt
      plot(trial, type = "l", title = "LLN")
      abline(0.95, 0, col = "red")
}

Conf.uniform(20, 1000, 0, 1)