library(MASS)
library(xtable)
library(invgamma)
library(HDInterval)

# set.seed(1207)                                    # to reproduce the same result

# Find the full conditional distributions and posterior marginal distributions
rb0cond <- function(y, x, b1, phi, t0, m0) {
  grid <- seq(-10, 10, .001)
  
  p <- numeric(length=length(grid))
  for (i in 1:length(p)) {
    p[i] <- (-(1/(2*phi)) * sum((y-(grid[i] + b1*x))^2)) + (-(1/(2*t0)) * (grid[i]-m0)^2)
  }
  
  draw <- sample(grid, size = 1, prob = exp(1 - p/max(p)))
  
  return(draw)
}

rb1cond <- function(y, x, phi, t1, m1, b0) {
  grid <- seq(-10, 10, .001)
  
  p <- numeric(length = length(grid))
  for (i in 1:length(p)) {
    p[i] <- (-(1/(2*phi)) * sum((y-(b0 + grid[i]*x))^2)) + (-(1/(2*t1)) * (grid[i]-m1)^2)
  }
  
  draw <- sample(grid, size = 1, prob = exp(1 - p/max(p)))
  
  return(draw)
}

# Hyper parameters (known)
#param <- c(0, 1, 1, 1)
#param <- c(4, 1, 3, 3)
#param <- c(1, 4, 6, 2)
param <- c(1, 4, 2, 6)

#ab <- c(1, 6)
#ab <- c(3, 3)
ab <- c(6, 1)

m0 <- param[1]
t0 <- param[3]

m1 <- param[2]
t1 <- param[4]

a <- ab[1]
b <- ab[2]

n <- 100
x <- rnorm(n, 0, 1)

tb0 <- rnorm(1, m0, sqrt(t0))
tb1 <- rnorm(1, m1, sqrt(t1))
tphi <- rinvgamma(1, shape = a, rate = b)
tb0; tb1; tphi;

y <- rnorm(n, tb0+tb1*x, sqrt(tphi))

# Use Gibbs
iter <- 1000
burnin <- 101
phi <- b0 <- b1 <- numeric(iter)
phi[1] <- b0[1] <- b1[1] <- 6

for (i in 2:iter) {
  phi[i] <- rinvgamma(1, shape = (n/2+a), rate = 0.5 * sum((y - (b0[i-1] + b1[i-1]*x))^2) + b)
  b0[i] <- rb0cond(y, x, b1[i-1], phi[i], t0, m0)
  b1[i] <- rb1cond(y, x, phi[i], t1, m1, b0[i])
}

# Get graph
par(mfrow = c(2,2), oma = c(0,0,2,0))
plot(phi[burnin:iter], type = 'l'); abline(h = tphi, col = 'red')
plot(b0[burnin:iter], type = 'l'); abline(h = tb0, col = 'red')
plot(b1[burnin:iter], type = 'l'); abline(h = tb1, col = 'red')
title(main = paste0("(mu0, mu1, v0, v1): ", param[1], ", ", param[2], ", ", param[3], ", ", param[4], 
                    "  (a, b): ", ab[1], ", ", ab[2]), outer = T, line = -2)

z <- kde2d(b0, b1, n = 50)
plot(b0, b1, pch = 19, cex = 0.4)
contour(z, drawlabels = FALSE, nlevels = 10, col = 'red', add = TRUE)

num1 <- round(hdi(b0[burnin:iter], credMass=0.95)["lower"], 2)
num2 <- round(hdi(b0[burnin:iter], credMass=0.95)["upper"], 2)
hist(b0[burnin:iter], main = paste0("95% HPD: ", num1, " ~ ", num2), col = "grey", border = "white")
abline(v = mean(b0[burnin:iter]), col = 'red')

num3 <- round(hdi(b1[burnin:iter], credMass = 0.95)["lower"], 2)
num4 <- round(hdi(b1[burnin:iter], credMass = 0.95)["upper"], 2)
hist(b1[burnin:iter], main = paste0("95% HPD: ", num3, " ~ ", num4), col = "grey", border = "white")
abline(v = mean(b1[burnin:iter]), col = 'red')

num5 <- round(hdi(phi[burnin:iter], credMass = 0.95)["lower"], 2)
num6 <- round(hdi(phi[burnin:iter], credMass = 0.95)["upper"], 2)
hist(phi[burnin:iter], main = paste0("95% HPD: ", num5, " ~ ", num6), col = "grey", border = "white")
abline(v = mean(phi[burnin:iter]), col = 'red')
title(main = paste0("(mu0, mu1, v0, v1): ", param[1], ", ", param[2], ", ", param[3], ", ", param[4], 
                    "  (a, b): ", ab[1], ", ", ab[2]), outer = T, line = 0)