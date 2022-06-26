# compare two dots
x <- seq(-10, 10, length.out = 10000)

# f(x)
y <- x^3 - 2.5*sin(x) - 1

xlim1 <- 1.51778
xlim2 <- 1.51780

ylim1 <- -0.002
ylim2 <- 0.002

func <- "y = x^3 - 2.5*sin(x) - 1"

plot(x, y, xlim = c(xlim1, xlim2), ylim = c(ylim1, ylim2), type = "l", col = "Blue", main = func)
abline(v = 0, lty = 1)
abline(h = 0, lty = 1)

# absolute error
c1 <- 1.517789                                           # False: 1.517787         Secant: 1.517787        Bisec: 1.517784
f1 <- 1.418756e-05                                       # False: -4.440892e-16    Secant: -1.152314e-10   Bisec: -1.629232e-05
points(c1, f1, pch = 20, cex = 1.5, col = 'red')

# relative error
c2 <- 1.517787                                           # False: 1.51777          Secant: 1.517787        Bisec: 1.517787
f2 <- -4.440892e-16                                      # False: -0.0001093708    Secant: -4.440892e-16   Bisec: 8.881784e-16
points(c2, f2, pch = 20, cex = 1.5, col = 'green')

## make data frame to compare every methods
num_iteration <- c(16, 53, 7, 9, 999, 27, 6, 10)
approx_root <- c(1.517784, 1.517787, 1.517787, 1.517787, 1.517787, 1.51777, 1.517789, 1.517787)
function_value <- c(-1.629232e-05, 8.881784e-16, -1.152314e-10, -4.440892e-16, -4.440892e-16, -0.0001093708, 1.418756e-05, -4.440892e-16)
df <- data.frame(num_iteration, approx_root, function_value)

rownames(df) = c("Bisec_Absolute", "Bisec_Relative", "Secant_Absolute", "Secant_Relative", 
                 "Falsepos_Absolute", "Falsepos_Relative", "Newton_Absolute", "Newton_Relative")

df1 <- df[c(order(abs(df$function_value))), ]
df2 <- df[c(order(df$num_iteration)), ]
df3 <- df[c(order(abs(df$function_value), df$num_iteration)), ]