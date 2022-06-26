## simpson method with absolute error
simpson <- function(f, a, b, eps = 1e-06) {
  n = 4
  s1 = ((b-a) / (3*n)) * (f(0) + 4*f(1) + f(2))
  repeat {
    h <- (b-a) / n
    
    x <- seq.int(a, b, length.out = n+1)
    x <- x[-1]
    x <- x[-length(x)]
    
    s2 <- (h/3)*(f(a) + 4*sum(f(x[seq.int(1, length(x), 2)])) + 2*sum(f(x[seq.int(2, length(x), 2)])) + f(b))
    
    if (abs(s2-s1) < eps)                                # compare absolute error
      break
    
    s1 <- s2
    n <- n + 2
  }
  print(n)
  print(s2)
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

simpson(f, 0, 3)
integrate(f, 0, 3)