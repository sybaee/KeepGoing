compare_pois <- function(n, lam) {
  plot(0, 0, las = 1, type = "n", xlim = c(0, 10), ylim = c(0, 12), 
       ylab = "densitiy", xlab = paste0("N: ", n), main = "Poisson distribution")
  
  m <- 10000
  xbars = hats = zs = matrix(NA, nrow = length(lam), ncol = m)
  
  for (i in 1:length(lam)) {
    for (k in 1:m) {
      x <- rpois(n, lam[i])                                           # poisson n samples distribution
      xbars[i, k] <- mean(x)                                          # sample mean
      hats[i, k] <- sd(x)                                             # sample standard deviation
      zs[i, k] <- (xbars[i, k]-lam[i]) / (hats[i, k]/sqrt(n))         # standard normalization
    }
    
    #print(xbars)                                                     # 10000 sample mean distribution
    #hist(xbars, freq = FALSE, xlab = paste0("λ: ", lam[i]), main = "Poisson distribution", las = 1) 
    
    lines(density(xbars[i, ]), lty = 1, col = i, lwd = 2)
  }
  legend("topright", legend = c("λ: 0.1", "λ: 0.5", "λ: 1", "λ: 3", "λ: 5"), 
         col = c(1, 2, 3, 4, 5), lty = 1, lwd = 2)
  return(zs)
}

pois_SND <- function(n, zs) {
  split.screen(c(2, 3))                                     
  for (i in seq(5)) {
    screen(i)
    hist(zs[i, ], probability = TRUE, las = 1, xlab = " ", main = paste0("N: ", n, " & ","λ: ", lambda1[i]))
    lines(seq(-4, 4, length = 100), dnorm(seq(-4, 4, length = 100)), type = "l", col = "blue")
  }
  close.screen(all.screens = TRUE)
}

sample1 <- c(5, 10, 15, 30, 40, 50, 100)
lambda1 <- c(0.1, 0.5, 1, 3, 5)

for (n in sample1) {
  zs <- compare_pois(n, lambda1)
  pois_SND(n, zs)
}