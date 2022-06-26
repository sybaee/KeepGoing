## make data frame to compare every methods
num_iteration <- c(12, 26, 54, 1552, 1)
approx_area <- c(0.8290362, 0.8290361, 0.8290412, 0.8290361, 0.8290361)
df <- data.frame(num_iteration, approx_area)

rownames(df) = c("Trapezoid_Absolute", "Trapezoid_Relative", "Simpson_Absolute", "Simpson_Relative", "Integrate")

df1 <- df[c(order(abs(df$approx_area))), ]
df2 <- df[c(order(df$num_iteration)), ]
df3 <- df[c(order(abs(df$approx_area), df$num_iteration)), ]