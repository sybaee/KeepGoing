## secant method with relative error
secant <- function (f, x1, x2, num = 1000, eps_x = 1e-05, eps_y = 1e-05) {
  i = 0
  err = nerr = rerror = 0
  err <- abs(x2 - x1)                                   # initialize err value
  rerror <- err
  while ((rerror > eps_x) & (i < num)) {                # compare relative error
    c = x2 - f(x2)*(x2-x1) / (f(x2)-f(x1))
    x1 = x2
    x2 = c
    i = i + 1
    
    if (x2 - x1 != 0) {
      nerr <- abs(x2-x1)
      rerror <- abs((nerr-err) / nerr)                # calculate relative error
      err <- nerr
    }
    
    else
      break
  }
  print(i-1)
  print(x2)                                             # this is approximate root
  print(f(x2))
  if (abs(f(x2)) < eps_y) print("finding root is successful")
  else print("finding root is fail")
}

f <- function(x) { x^3 - 2.5*sin(x) - 1 }

x <- seq(-10, 10, length.out = 10000)
y <- f(x)
plot(x, y, xlim = c(-1, 3), ylim = c(-3, 10), type = "l", col = "Blue", 
     main = "y = x^3 - 2.5*sin(x) - 1")
abline(v = -1:5, col = "Gray", lty = 3)
abline(v = 0, lty = 1)
abline(h = 0, lty = 1)

secant(f, 1, 3)