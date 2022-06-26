# Implement an interactive MIPS code
# to calculate either of sum, max or min of the list depending on keyboard input

# 4 different keyboard inputs in an infinite loop until ‘q’
# (e.g., ‘x’, ‘s’, ‘s’, ‘n’, ‘s’, ‘x’, ‘q’)

# 's’ -> you should print out the sum with a proper message (like "The sum is 232")
# 'x’ -> you should print out the max
# 'n’ -> you should print out the min
# 'q’ -> you should quit the loop


  .data

  # Declare array
  digit:  .word 10, 12, 23, 28, 7, 39, 10, 11, 23, 12, 3, 4, 5, 1, 34, 17, 0, 5, 24

  # Length of array
  length:  .word 19 # the length of the digit list

  # Message
  Message: .asciiz "\n\nHere's [10, 12, 23, 28, 7, 39, 10, 11, 23, 12, 3, 4, 5, 1, 34, 17, 0, 5, 24]\nEnter the character you want to calculate\n's' for sum or 'x' for max or 'n' for min or 'q' for quit: "

  # Print form
  sumform:  .asciiz "\nsum is "
  maxform:  .asciiz "\nmax is "
  minform:  .asciiz "\nmin is "

  # Declare input
  input:  .space 1024

  .text
  .globl main


main:

  # HERE, implement mips code
  # to get the sum, max, and min of the ‘digit’ list above
  # and to print the results (sum, max, and min)

  # the printing format should be as follows:
  # sum is xxx
  # max is yyy
  # min is zzz

  # Load the base address of array
  la $s1, digit

  # Load the first element of array
  # $t0 = digit[0]
  lw $t0, 0($s1)

  # $t1 = sum
  move $t1, $t0

  # $t2 = max
  move $t2, $t0

  # $t3 = min
  move $t3, $t0

  # Load the address of length of array
  la $s2, length

  # Load the length of array
  # $s2 = len(digit)
  lw $t4, 0($s2)


# Ask to choose the calculation
Loop:

  # $v0 = 4: Load code to print string
  li $v0, 4

  # Load the address of the string for print
  la $a0, Message

  # Print the string
  # Ask to choose among s, x, n, q
  syscall

  # $v0 = 8: Load the code to read input
  # To ask and read the input
  li $v0, 8

  # Load the address of the input
  la $a0, input

  # Load the length of the input
  li $a1, 1024

  # Read the input
  syscall

  # Load the input
  la $t7, input

  # Get the first character in the input
  # $t6 = the input
  lb $t6, 0($t7)

  # Check if the input is 's'
  beq $t6, 's', sumLoop

  # Check if the input is 'x'
  beq $t6, 'x', maxLoop

  # Check if the input is 'n'
  beq $t6, 'n', minLoop

  # Check if the input is 'q'
  beq $t6, 'q', Exit


# Loop for sum
sumLoop:

  # Get the next element of array
  addi $s1, $s1, 4

  # Load that element of array
  # $t5 = current element of array
  lw $t5, 0($s1)

  # $t4 = the length of array
  # $t4 -= 1 for counting
  # Till it reaches the last element of array
  addi $t4, $t4, -1

  # Add the element
  # $t1 = sum ($t1 = digit[0] at the very first)
  add $t1, $t1, $t5

  # Branch if $t4 is greater than 1
  # Go for loop again
  bgt $t4, 1, sumLoop

  # Go to printSum
  j printSum


# Loop for max
maxLoop:

  # Get the next element of array
  addi $s1, $s1, 4

  # Load that element of array
  # $t5 = current element of array
  lw $t5, 0($s1)

  # $t4 = the length of array
  # $t4 -= 1 for counting
  # Till it reaches the last element of array
  addi $t4, $t4, -1

  # Check condition for Max ($t2 = digit[0] at the very first)
  # Branch if $t5 is greater than $t2 (max)
  bgt $t5, $t2, storeMax

  # Branch if $t4 is greater than 1
  # Go for loop again
  bgt $t4, 1, maxLoop

  # Go to printMax
  j printMax


# Store new max to $t2 (max)
storeMax:

  # Store the element as the new max
  move $t2, $t5

  # Jump to maxLoop
  j maxLoop


# Loop for min
minLoop:

  # Get the next element of array
  addi $s1, $s1, 4

  # Load that element of array
  # $t5 = current element of array
  lw $t5, 0($s1)

  # $t4 = the length of array
  # $t4 -= 1 for counting
  # Till it reaches the last element of array
  addi $t4, $t4, -1

  # Check condition for Min ($t3 = digit[0] at the very first)
  # Branch if $t5 is less than $t3 (min)
  blt $t5, $t3, storeMin

  # Branch if $t4 is greater than 1
  # Go for loop again
  bgt $t4, 1, minLoop

  # Go to printMin
  j printMin


# Store new min to $t3 (min)
storeMin:

  # Store the element as the new min
  move $t3, $t5

  # Jump to minLoop
  j minLoop


# Print the result of sum calculation
printSum:

  # $v0 = 4: Load code to print string
  li $v0, 4

  # Load the address of the string for print
  la $a0, sumform

  # Print the string
  syscall

  # $v0 = 1: Load the code to print integer
  li $v0, 1

  # Load the sum
  move $a0, $t1

  # Print the sum
  syscall

  # Jump to main to clear the register and ask again
  j main


# Print the result of max calculation
printMax:

  # $v0 = 4: Load code to print string
  li $v0, 4

  # Load the address of the string for print
  la $a0, maxform

  # Print the string
  syscall

  # $v0 = 1: Load the code to print integer
  li $v0,1

  # Load the max value
  move $a0, $t2

  # Print the max value
  syscall

  # Jump to main to clear the register and ask again
  j main


# Print the result of min calculation
printMin:

  # $v0 = 4: Load code to print string
  li $v0, 4

  # Load the address of the string for print
  la $a0, minform

  # Print the string
  syscall

  # $v0 = 1: Load the code to print integer
  li $v0, 1

  # Load the min value
  move $a0, $t3

  # Print the min value
  syscall

  # Jump to main to clear the register and ask again
  j main


# Quit calculation
Exit:

  # $v0 = 10: Load the code to quit the calculation
  li $v0, 10

  # Stop the calculation
  syscall

  .end
