## trapezoid method with relative error
trapezoid <- function(f, a, b, eps = 1e-06) {
  n = 1
  err = nerr = rerror = 0
  s1 = ((b-a) / 2) * (f(a)+f(b))                       # R(0, 0)
  repeat {
    sigma <- 0
    h <- (b-a) / 2^n
    for (k in 1:2^(n-1)) {
      x <- a + (2*k - 1) * h
      sigma <- sigma + f(x)
    }
    s2 <- (1/2)*s1 + h*sigma
    
    nerr <- s2 - s1
    if (n > 1)
      rerror <- abs((nerr-err) / nerr)                   # calculate relative error
    else
      rerror <- abs(nerr)
      
    if (rerror < eps | n > 25) 
      break
    
    err <- nerr
    s1 <- s2
    n <- n + 1
  }
  print(n)                                                 # the number of iteration
  print(s2)                                                # approximate area
}

e <- exp(1)
f <- function(x) { e^-x / (0.5 + 4*x^3) }

x <- seq(-10, 10, length.out = 10000)
y <- f(x)
plot(x, y, xlim = c(-3, 3), ylim = c(-10, 10), type = "l", col = "Blue", 
     main = "y = e^-x / (0.5 + 4*x^3)")
abline(v = -1:5, col = "Gray", lty = 3)
abline(v = 0, lty = 1)
abline(h = 0, lty = 1)

trapezoid(f, 0, 3)
integrate(f, 0, 3)