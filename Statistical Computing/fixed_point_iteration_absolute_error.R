## fixed point iteration
fixedpt <- function(f, x0, num = 20, eps = 1e-05) {     # x0 = starting value
  i <- 1
  x1 <- x0
  p <- numeric(num)                                     # array to save all new x1
  while (i <= num) {
    x1 <- f(x0)
    p[i] <- x1
    i <- i + 1
    print(x1)
    
    if (abs(x1-x0) < eps)                               # compare absolute error
      break
    
    x0 <- x1
  }
  print(i-1)
  return(p[1:(i-1)])
}

f <- function(x) { x^3 - 2.5*sin(x) - 1 }

x <- seq(-10, 10, length.out = 10000)
y <- f(x)
plot(x, y, xlim = c(-3, 10), ylim = c(-3, 10), type = "l", col = "Blue", 
     main = "y = x^3 - 2.5*sin(x) - 1")
abline(v = -1:5, col = "Gray", lty = 3)
abline(v = 0, lty = 1)
abline(h = 0, lty = 1)

fixedpt(f, 3)