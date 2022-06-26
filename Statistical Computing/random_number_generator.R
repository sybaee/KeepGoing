# Random number generators in R

# 1. Generate 10 random numbers from a uniform distribution on [0,10]. Use R to find the maximum and minimum values of x

# uniform distribution, follows the same pattern as the normal distribution above
data = runif(10, min = 0, max = 10)
hist(data, probability = TRUE, col = gray(0.9), main = "uniform distribution on [0, 10]")

# 2. Generate 10 random normal numbers with mean 5 and standard deviation 5 (normal(5,5)).

# n random normal deviates with mean m and standard deviation sd
data = rnorm(10, mean = 5, sd = 5)
hist(data, probability = TRUE, col = gray(0.9), main = "normal distribution")

# How many are less than 0?
num = length(which(data < 0)) # = sum(data < 0)

# 3. Generate 100 random normal numbers with mean 100 and standard deviation 10.
data = rnorm(100, mean = 100, sd = 10)
hist(data, probability = TRUE, col = gray(0.9), main = "normal distribution")

# How many are 2 standard deviations from the mean (smaller than 80 or bigger than 120)?
num = length(which(data < 80)) + length(which(data > 120))
# sum(x < 80 | x > 120)

# 4. Toss a fair coin 50 times. How many heads do you have?

# binomial distribution where size is the sample size and prob is the probability of a heads (pi)
# rbinom(number of experiments, number of observations per experiment, probability of success)
heads <- rbinom(1, 50, 0.5)

x <- rbinom(50, 1, 0.5)
sum(x == 1)

# prob of 25 or less heads of fair coin out of 50 flips
prob = pbinom(25, 50, 0.5)

# 5. Roll a die 100 times. How many 6's did you see?
dices = rbinom(1, 100, 1/6)

# 6. Select 6 numbers from a lottery containing 49 balls. What is the largest number? What is the smallest? 
data = sample(1:49, 6, replace = FALSE)

min(data)
max(data)

# 7. For normal(0,1), find a number z* solving P(z*) = 0.05 (use qnorm)

# normal quantile, value at the p percentile of normal distribution
z = qnorm(0.05, lower.tail = FALSE) # lower.tail -> to the right 
pnorm(z) # = 0.05

# 8. For normal(0,1), find a number z* solving P(-z* < Z < z*) = .05 (use qnorm and symmetry)
qnorm(0.475, lower.tail = FALSE) # = z*
qnorm(0.525)

# 9. How much area (probability) is to the right of 1.5 for a normal(0,2)
pnorm(1.5, 0, 2, lower.tail = FALSE)

# 10. Make a histogram of 100 exponential numbers with mean 10.
x = rexp(100, 1/10)
hist(x, probability = TRUE, col = gray(0.9), main = "exponential mean = 10")
curve(dexp(x, 1/10), add = T)

# Estimate the median. Is it more or less than the mean?
median(x) # less than mean

# 11. Can you figure out what this R command does? rnorm(5, mean = 0, sd = 1:5)
rnorm(5, mean = 0, sd = 1:5)
hist(x, probability = TRUE, col = gray(0.9), main = "normal distribution")

# 12. Use R to pick 5 cards from a deck of 52. Did you get a pair or better? Repeat until you do. How long did it take?
suits <- rep(c("Club", "Spade", "Diamond", "Hearts"), each = 13)
value <- rep(c("Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"), times = 4)
cards <- paste(value, "of", suits)

sample(cards, size = 5, replace = FALSE)

#---------------------------------------------------------------------------------------------------------------------------------------

# case I: mu=0, sigma=0

runs <- 3000
sample <- c(4, 16, 25, 30, 50, 1100)
mu_1 <- 0
sigma_1 <- 0
alpha <- 0.05 # 100 * (1 - alpha) % = 95%

# n = 4, 16, 25, 30, 50, 1100
for (n in sample) {
  in_inter <- 0
  print(paste0("n =", n))
  print(paste0("sigma =", sigma_1))
  
  for (i in 1:runs) {
    x = rnorm(n, mu_1, sigma_1)
    xb <- mut + sigma_1 / sqrt(n)
    conf_limit_up = xb + (qnorm(alpha/2, lower.tail = FALSE)*sigma_1/sqrt(n))
    conf_limit_dn = xb - (qnorm(alpha/2, lower.tail = FALSE)*sigma_1/sqrt(n))
    in_interval = (mu_1 <= conf_limit_up & mu_1 >= conf_limit_dn)
    
    if (in_interval) in_inter <- in_inter 1
  }
  #get diff from theorical 95% norm
  experi <- in_inter / runs
  print(paste0("prob:", experi * 100))
}

# case II: mu=0 variance=1, 10
mu_2 <- 0
var_21 <- 1
var_22 <- 10
sigma_21 <- sqrt(var_21)
sigma_22 <- sqrt(var_22)
sample1 <- c(4, 16, 25)
n_2_2 <- c(30, 50, 100)
in_inter <- 0
n <- 4
experi <- c()

# 4, 16, 25 and sigma_21
for (n in sample1) {
  in_inter <- 0
  print(paste0("n =", n))
  print(paste0("sigma =", sigma_21))
  
  for (i in 1:runs) {
    x = rt(n, n-1)
    conf_limit_up = mean(x) + qt(alpha/2, lower.tail = FALSE)*sigma_21/sqrt(n)
    conf_limit_dn = mean(x) - qt(alpha/2, lower.tail = FALSE)*sigma_21/sqrt(n)
    in_interval = mu_2 <= conf_limit_up & mu_2 >= conf_limit_dn
    if (in_interval) in_inter <- in_inter + 1
  }
  
  experi <- in_inter / runs
  print(paste0("prob: ", experi*100))
}

# 30, 50, 100 and sigma_21
for (j in n_2_2) {
  in_inter <- 0
  print(paste0("n =", j))
  print(paste0("sigma =", sigma_21))

  for (i in 1:runs) {
    x = rt(j, j-1)
    conf_limit_up = mean(x) + qnorm(1-alpha/2)*sigma_21/sqrt(j)
    conf_limit_dn = mean(x) - qnorm(1-alpha/2)*sigma_21/sqrt(j)
    in_interval = mu_2 <= conf_limit_up & mu_2 >= conf_limit_dn
    if (in_interval) in_inter <- in_inter + 1
  }
  
  experi <- in_inter / runs
  print(paste0("prob: ", experi*100))
}

# 4, 16, 25 and sigma_21
for (j in n_2_1) {
  in_inter <- 0
  print(paste0("n =", j))
  print(paste0("sigma =", sigma_22))

  for (i in 1:runs) {
    x = rt(j, j-1)
    conf_limit_up = mean(x) + qnorm(1-alpha/2)*sigma_22/sqrt(j)
    conf_limit_dn = mean(x) - qnorm(1-alpha/2)*sigma_22/sqrt(j)
    in_interval = mu_2 <= conf_limit_up & mu_2 >= conf_limit_dn
    if (in_interval) in_inter <- in_inter + 1
  }
  
  experi <- in_inter / runs
  print(paste0("prob: ", experi*100))
}

# 30, 50, 100 and sigma_21
for (j in n_2_2) {
  in_inter <- 0
  print(paste0("n =", j))
  print(paste0("sigma =", sigma_22))

  for (i in 1:runs) {
    x = rt(j, j-1)
    conf_limit_up = mean(x) + qnorm(1-alpha/2)*sigma_22/sqrt(j)
    conf_limit_dn = mean(x) - qnorm(1-alpha/2)*sigma_22/sqrt(j)
    in_interval = mu_2 <= conf_limit_up & mu_2 >= conf_limit_dn
    if (in_interval) in_inter <- in_inter + 1
  }
  
  experi <- in_inter / runs
  print(paste0("prob: ", experi*100))
}