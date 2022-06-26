sample1 <- c(5, 15, 30, 50, 100)

uniformz <- function(n) {
  U <- runif(n, min = 0, max = 1)
  V <- runif(n, min = 0, max = 1)
  
  z1 <- sqrt(-2*log(U)) * cos(2*pi*V)
  z2 <- sqrt(-2*log(V)) * sin(2*pi*U)

  return(list(z1, z2))
}

uniformND <- function(allz, n, sam) {
  split.screen(c(2, 3))
  for (i in 1:5) {
    screen(i)
    hist(allz[[i]], probability = TRUE, las = 1, xlab = paste0("N: ", sam[i]), main = paste0("Z", n))
    lines(seq(-4, 4, length = 100), dnorm(seq(-4, 4, length = 100)), type = "l", col = "blue")
  }
  close.screen(all.screens = TRUE)
}

uniformScatter <- function(allz1, allz2, sam) {
  for (i in seq(5)) {
    data <- cbind(allz1[[i]], allz2[[i]])
    plot(formula = allz1[[i]] ~ allz2[[i]], data = data, main = paste0("N: ", sam[i]), 
         xlab = "z2", ylab = "z1", las = 1, col = "blue", pch = 19)
    
    pairs(data, col = "blue", las = 1, labels = c("z1 ", "z2"), main = paste0("N: ", sam[i]))
  }
}

allz1 <- list()
allz2 <- list()

for (i in 1:length(sample1)) {
  zs <- uniformz(sample1[i])
  allz1[i] <- zs[1]
  allz2[i] <- zs[2]
}

uniformND(allz1, 1, sample1)
uniformND(allz2, 2, sample1)
uniformScatter(allz1, allz2, sample1)

cor.test(allz1[[1]], allz2[[1]])
cor.test(allz1[[2]], allz2[[2]])
cor.test(allz1[[3]], allz2[[3]])
cor.test(allz1[[4]], allz2[[4]])
cor.test(allz1[[5]], allz2[[5]])