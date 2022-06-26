# Example 1

# After one week in Las Vegas and still zero Ferrari's in your garage, you decide it is time to start using your data analytical superpowers.
# Before doing a first analysis, you decide to first collect the winnings and losses for the last week
# For poker_vector:
# - On Monday you won $140
# - Tuesday you lost $50
# - Wednesday you won $20
# - Thursday you lost $120
# - Friday you won $240

# For roulette_vector:
# - On Monday you lost $24
# - Tuesday you lost $50
# - Wednesday you won $100
# - Thursday you lost $350
# - Friday you won $10

# Poker winnings from Monday to Friday
Poker_vector = c(140, -50, 20, -120, 240)
# Roulette winnings from Monday to Friday
roulette_vector = c(-24, -50, 100, -350, 10)

# give a name to poker_vector
names(poker_vector) = c('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday')
names(roulette_vector) = c('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday')

# Calculating total winnings
# Now, you want to find out the following type of information:

# How much has been your overall profit or loss per day of the week?
total_daily = poker_vector + roulette_vector

# Have you lost money over the week in total?
total_poker = sum(Poker_vector)
total_roulette = sum(roulette_vector)
total_week = total_poker + total_roulette

# Define new variable based on a selection
poker_wednesday = poker_vector[3]
# Define new variable based on a selection
poker_midweek = poker_vector[c(2, 3, 4)]
# Define new variable based on a selection
roulette_selecltion_vector = roulette_vector[2:5]

# Are you winnig / losing money on poker or on roulette?

# What days of the week did you make money on poker?
selection_vector = poker_vector > 0

# Select from poker_vector theses days
poker_winning_days = poker_vector[selection_vector]

# To get the answers, you have to do arithmetic calculations on vectors

#--------------------------------------------------------------------------------------------------------------------------------------------

# Example 2

# Box office Star Wars: In Millions! The first element: US. Second element: Non-US
new_hope = c(460.998007, 314.4)
empire_strikes = c(290.475067, 247.9)
return_jedi = c(309.306177, 165.8)

# Add your code below to construct the matrix
star_wars_matrix = matrix(c(new_hope, empire_strikes, return_jedi), nrow = 3, byrow = TRUE)

# Similar to vectors, you can add names for the rows and the columns of a matrix
colnames(star_wars_matrix) = c("US", "non-US")
rownames(star_wars_matrix) = c("A New Hope", "The Empire Strikes Back", "Return of the Jedi")

# Box office Star Wars: In Millions (!) Construct matrix containing first trilogy box office:
box_office_all = c(461, 314.4, 290.5, 247.9, 309.3, 165.8)
movie_names = c("A New Hope", "The Empire Strikes Back", "Return of the Jedi")
col_titles = c("US", "non-US")
star_wars_matrix = matrix(box_office_all, nrow = 3, byrow = TRUE, dimnames = list(movie_names, col_titles))

# rowSums() convenientyl calculates the totals for each row of a matrix
worldwide_vector = rowSums(star_wars_matrix)

# Bind the new variable worldwide_vector as a column to star_wars_matrix
all_wars_matrix = cbind(star_wars_matrix, worldwide_vector)

# Box office Star Wars: In Millions (!) Construct matrix containing second trilogy box office:
box_office_all2 = c(474.5, 552.5, 310.7, 338.7, 380.3, 468.5)
movie_names2 = c("The Phantom Menace", "Attack of the Clones", "Revenge of the Sith")
col_titles2 = c("US", "non-US")
star_wars_matrix2 = matrix(box_office_all2, nrow = 3, byrow = TRUE, dimnames = list(movie_names2, col_titles2))

# rowSums() convenientyl calculates the totals for each row of a matrix
worldwide_vector2 = rowSums(star_wars_matrix2)

# Combine both Star Wars trilogies in one matrix
all_wars_matrix = rbind(star_wars_matrix, star_wars_matrix2)

# Print box office Star Wars: In Millions (!) for 2 trilogies
total_revenue_vector = colSums(all_wars_matrix)

movie_revenue_vector = rowSums(all_wars_matrix)

# the arithmetic mean
non_us_all = mean(star_wars_matrix[, 2])
non_us_some = mean(star_wars_matrix[1:2, 2])

ticket_prices_matrix = matrix(c(5, 5, 6, 6, 7, 7), nrow = 3, byrow = TRUE, dimnames = list(movie_names, col_titles))
visitors = star_wars_matrix / ticket_prices_matrix

average_us_visitor = mean(visitors[, 1])
average_non_us_visitor = mean(visitors[, 2])

#--------------------------------------------------------------------------------------------------------------------------------------------

# Example 3

# The function factor() will encode the vector as a factor
gender_vector = c("Male", "Female", "Female", "Male", "Male")

factor_gender_vector = factor(gender_vector)

animals_vector = c("Elephant", "Giraffe", "Donkey", "Horse")
temperature_vector = c("High", "Low", "High", "Low", "Medium")

factor_animals_vector = factor(animals_vector)
factor_temperature_vector = factor(temperature_vector, order = TRUE, levels = c("Low", "Medium", "High"))

# R allows you to change the names of factor levels with the function levels()
survey_vector = c("M", "F", "F", "M", "M")
factor_survey_vector = factor(survey_vector)
levels(factor_survey_vector) = c("Male", "Female")

summary(survey_vector)
summary(factor_survey_vector)

# Ordered factors
speed_vector = c("Fast", "Slow", "Slow", "Fast", "Ultra-fast")
factor_speed_vector = factor(speed_vector, ordered = TRUE, levels = c("Slow", "Fast", "Ultra-fast"))

summary(factor_speed_vector)

# Is data analyst 2 faster than data analyst 5?
compare_them = factor_speed_vector[2] > factor_speed_vector[5]

#--------------------------------------------------------------------------------------------------------------------------------------------

# Example 4

# Have a quick look at your data
head(mtcars)
tail(mtcars)

# Investigate the structure of the mtcars dataset
str(mtcars)

# Creating a data frame

# Construct a data frame that describes the main characteristics of 8 planets in our solar system
planets = c("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune");
type = c("Terrestrial planet", "Terrestrial planet", "Terrestrial planet", "Terrestrial planet", "Gas giant", "Gas giant", "Gas giant", "Gas giant")
diameter = c(0.382, 0.949, 1, 0.532, 11.209, 9.449, 4.007, 3.883)
rotation = c(58.64, -243.02, 1, 1.03, 0.41, 0.43, -0.72, 0.67)
rings = c(FALSE, FALSE, FALSE, FALSE, TRUE, TRUE, TRUE, TRUE)

# Create the data frame:
planets_df = data.frame(planets, type, diameter, rotation, rings)

# Check the structure of planets_df
str(planets_df)

# with the help of square brackets[]
closest_planets_df = planets_df[1:3, ]
furthest_planets_df = planets_df[6:8, ]

# use of the variable name
furthest_planets_diameter = planets_df[3:8, "diameter"]

# use the $-sign to look up all the elements of the variable behind the sign
# Create the rings_vector
rings_vector = planets_df$rings

# Select the information on planets with rings:
planets_with_rings_df = planets_df[rings_vector, ]

# Planets smaller than earth:
small_planets_df = subset(planets_df, planets_df$diameter < 1)

# What is the correct ordering based on the planets_df$diameter variable?
positions = order(planets_df$diameter, decreasing = TRUE)

# Create new 'ordered' data frame:
largest_first_df = planets_df[positions, ]