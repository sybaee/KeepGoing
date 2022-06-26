# set.seed(1207)                                    # to reproduce the same result

# Hyper parameters (known)
param <- c(0, 1, 1, 1)
#param <- c(4, 1, 3, 3)
#param <- c(1, 4, 6, 2)
#param <- c(1, 4, 2, 6)

ab <- c(1, 6)
#ab <- c(3, 3)
#ab <- c(6, 1)

m0 <- param[1]
t0 <- param[3]

m1 <- param[2]
t1 <- param[4]

a <- ab[1]
b <- ab[2]

tb0 <- rnorm(1, m0, sqrt(t0))
tb1 <- rnorm(1, m1, sqrt(t1))
tphi <- rinvgamma(1, shape = a, rate = b)

trueA <- tb1
trueB <- tb0
trueSd <- sqrt(tphi)
sampleSize <- 100

# Create independent x-values 
x <- rnorm(n, 0, 1)
# Create dependent values according to ax + b + N(0,sd)
y <-  trueA*x + trueB + rnorm(n = sampleSize, mean = 0, sd = trueSd)

# Find the full conditional distributions and posterior marginal distributions
likelihood <- function(param) {
  a = param[1]
  b = param[2]
  sd = param[3]
  
  pred = a*x + b
  singlelikelihoods = dnorm(y, mean = pred, sd = sd, log = T)
  sumll = sum(singlelikelihoods)
  
  return(sumll)   
}

# Prior distribution
prior <- function(param) {
  a = param[1]
  b = param[2]
  sd = param[3]
  aprior = dunif(a, min = 0, max = 10, log = T)
  bprior = dnorm(b, sd = 5, log = T)
  sdprior = dunif(sd, min = 0, max = 30, log = T)
  
  return (aprior + bprior + sdprior)
}

posterior <- function(param) { return (likelihood(param) + prior(param)) }

######## Metropolis algorithm ################
proposalfunction <- function(param) { return (rnorm(3, mean = param, sd = c(0.1, 0.5, 0.3))) }

run_metropolis_MCMC <- function(startvalue, iterations) {
  chain = array(dim = c(iterations+1, 3))
  chain[1, ] = startvalue
  for (i in 1:iterations) {
    proposal = proposalfunction(chain[i, ])
    
    probab = exp(posterior(proposal) - posterior(chain[i, ]))
    if (runif(1) < probab) {
      chain[i+1, ] = proposal
    }
    
    else {
      chain[i+1, ] = chain[i, ]
    }
  }
  return (chain)
}

startvalue = c(tb1, tb0, sqrt(tphi))
chain = run_metropolis_MCMC(startvalue, 10000)

burnIn = 5000
acceptance = 1 - mean(duplicated(chain[-(1:burnIn), ]))

### Summary
par(mfrow = c(2, 3))
hist(chain[-(1:burnIn), 1], nclass = 30, main = "Posterior of a", xlab = "True value = red line" )
abline(v = mean(chain[-(1:burnIn), 1]))
abline(v = trueA, col = "red" )
hist(chain[-(1:burnIn), 2], nclass = 30, main = "Posterior of b", xlab = "True value = red line")
abline(v = mean(chain[-(1:burnIn), 2]))
abline(v = trueB, col = "red" )
hist(chain[-(1:burnIn), 3], nclass = 30, main = "Posterior of sd", xlab = "True value = red line")
abline(v = mean(chain[-(1:burnIn), 3]))
abline(v = trueSd, col = "red" )

plot(chain[-(1:burnIn), 1], type = "l", main = "Chain values of a", xlab = "True value = red line")
abline(h = trueA, col = "red" )
plot(chain[-(1:burnIn), 2], type = "l", main = "Chain values of b", xlab = "True value = red line")
abline(h = trueB, col = "red" )
plot(chain[-(1:burnIn), 3], type = "l", main = "Chain values of sd", xlab = "True value = red line")
abline(h = trueSd, col = "red" )

### For comparison
summary(lm(y~x))