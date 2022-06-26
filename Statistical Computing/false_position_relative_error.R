## false position method with relative error
falsepos <- function (f, x0, x1, num = 1000, eps_x = 1e-05, eps_y = 1e-05) {
  i = 0
  x2 = 0
  err = nerr = rerror = 0
  err <- abs(x0-x1)                                   # initialize err value
  rerror <- err
  while ((rerror > eps_x) & (i < num)) {                # compare relative error
    x2 = x1 - f(x1)*(x0-x1) / (f(x0)-f(x1))
    if (f(x2)*f(x0) < 0)
      x1 = x2

    else {
      x0 = x1
      x1 = x2
    }
    i = i + 1
    
    if (x0-x1 != 0) {
      nerr <- abs(x0-x1)
      rerror <- abs((nerr-err) / nerr)                # calculate relative error
      err <- nerr
    }
    
    else
      break
  }
  print(i-1)
  print(x1)                                             # this is approximate root
  print(f(x1))
  if (abs(f(x1)) < eps_y) print("finding root is successful")
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

falsepos(f, 0, 3)