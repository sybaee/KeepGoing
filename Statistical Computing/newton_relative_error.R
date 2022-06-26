## newton method with absolute error
newton <- function(f, x0, num = 20, eps = 1e-05) {      # x0 = starting value
  h <- 0.001
  i <- 1
  x1 <- x0
  p <- numeric(num)                                     # array to save all new x1
  err = nerr = rerror = 0
  err <- abs(x0-x1)                                     # initialize err value
  rerror <- err
  while (i <= num) {
    df.dx <- (f(x0+h)-f(x0)) / h
    x1 <- (x0 - (f(x0)/df.dx))
    p[i] <- x1
    i <- i + 1
    
    if (x0-x1 != 0) {
      nerr <- abs(x0-x1)
      rerror <- abs((nerr-err) / nerr)                  # calculate relative error
      err <- nerr
    }
    
    else
      break
    
    x0 <- x1
  }
  print(i-1)
  return(p[1:(i-1)])
}

f <- function(x) { x^3 - 2.5*sin(x) - 1 }

x <- seq(-10, 10, length.out = 10000)
y <- f(x)
plot(x, y, xlim = c(-1, 3), ylim = c(-3, 10), type = "l", col = "Blue", 
     main = "y = x^3 - 2.5*sin(x) - 1")
abline(v = -1:5, col = "Gray", lty = 3)
abline(v = 0, lty = 1)
abline(h = 0, lty = 1)

p <- newton(f, 1)
x <- p[length(p)-1]

x
f(x)