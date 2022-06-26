## bisection method with absolute error
bisec <-function (f, a, b, num = 10, eps = 1e-05) {
    h = abs(b-a) / num
    i = 0
    j = 0
    a1 = b1 = 0; c =1
    while (i <= num) {
        a1 = a + i*h
        b1 = a1 + h
        if (f(a1) == 0) {                     # a1 = root
            print(a1)
            print(f(a1))
        }

        else if (f(b1) == 0) {                # b1 = root
            print(b1)
            print(f(b1))
        }

        else if (f(a1)*f(b1) < 0) {
            repeat {
                if (abs(b1-a1) < eps)         # compare absolute error
                  break

                x <- (a1+b1) / 2
                if (f(a1)*f(x) < 0) 
                  b1 <- x

                else a1 <- x
                c = c + 1
            }
            print(c)                          # the number of iteration
            j = j + 1
            print((a1+b1) / 2)                # this is approximate root
            print(f((a1+b1) / 2))
        }
        i = i + 1
    }

    if (j == 0) print("finding root is fail")
    else print("finding root is successful")
  }

f <- function(x) { x^3 - 2.5*sin(x) - 1 }

x <- seq(-10, 10, length.out = 10000)
y <- f(x)
plot(x, y, xlim = c(-1, 3), ylim = c(-3, 10), type = "l", col = "Blue", 
     main = "y = x^3 - 2.5*sin(x) - 1")
abline(v = -1:5, col = "Gray", lty = 3)
abline(v = 0, lty = 1)
abline(h = 0, lty = 1)

bisec(f, 0, 3)