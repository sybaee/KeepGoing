.data

# Dimension and value (single precision)
matrixA:   .float 1.5, -1.2, 2.3, -2.8, 0.7, 3.9, 1.3, 1.3, 1.4, 1.0, 0.5, -1.2, 1.2, -2.1, 1.8

vectorX:   .float 3.2, -1.5, 1.1            # 3 dimension
vectorB:   .float 0.3, -0.5, 1.3, -2.1, 0.1 # 5 dimension

standard:  .float 0.0

shapeA:    .word 5, 3                       # matrixA's size is 5 x 3

str1:   .asciiz "shape of matrix A = "
str2:   .asciiz "\ny = ( "
str3:   .asciiz " )"
str4:   .asciiz "\n\n\nThe result of max(0, Ax + B) is\n"

matrix: .asciiz " X "
space:  .asciiz " "
nl:     .asciiz "\n      "


.text
.globl main

main:

  # print the values of y,
  # where y = max(0, matrixA * vectorX + vectorB)

        la    $s1, matrixA        # $s1 = &matrixA[0][0]
        la    $s2, vectorX        # $s2 = &vectorX[0] (Column vector)
        la    $s3, vectorB        # $s3 = &vectorB[0] (Row vector)

        la    $s4, standard       # $s4 = &standard[0]
        lwc1  $f8, 0($s4)         # $f8 = 0.0

        la    $s5, shapeA         # $s5 = &shapeA[0]

        lw    $s6, 4($s5)         # $s6 = matrixA's Column number
        lw    $s5, 0($s5)         # $s5 = matrixA's Row number

        move  $t0, $s1            # $t0 = &matrixA[0][0]

        mul   $t1, $s5, $s6       # $t1 = Row * Column
        addi  $t1, $t1, -1        # $t1 = (Row * Column) - 1
        sll   $t1, $t1, 2
        add   $t1, $t1, $s1       # $t1 = &matrixA[Row][Column]

        move  $t2, $s2            # $t2 = &vectorX[0] (Column vector)
        move  $t3, $s3            # $t3 = &vectorB[0] (Row vector)

        li    $t4, 0              # $t4 = count = 0
        lwc1  $f4, 0($t3)         # $f4 = B[0]

  L1:   lwc1  $f0, 0($t0)         # $f0 = A[ ][ ]
        lwc1  $f2, 0($t2)         # $f2 = X[ ]

        mul.s $f0, $f0, $f2       # $f0 = A[ ][ ] * X[ ]
        add.s $f4, $f4, $f0       # $f4 = (A[ ][ ] * X[ ]) + B[ ]

        addi  $t4, $t4, 1         # $t4 += 1
        addi  $t0, $t0, 4         # $t0 += 4, go to next element
        addi  $t2, $t2, 4         # $t2 += 4, go to next element

        beq   $t4, $s6, L2        # if multiplication of one row is done, go to L2

        j     L1                  # jump to L1

  L2:   li    $t4, 0              # $t4 = count = 0

        move  $t2, $s2            # $t2 = &X[0]

        swc1  $f4, 0($t3)         # B[ ] += A[ ][ ] * X[ ]
        addi  $t3, $t3, 4         # $t3 += 4, go to next element
        lwc1  $f4, 0($t3)         # $f4 = B[ ]

        slt   $t6, $t0, $t1
        beq   $t6, $zero, L3      # if ($t0 >  &A[Row][Column]), go to L3

        j     L1

  L3:   li    $v0, 4              # system call code for printing string = 4
        la    $a0, str1
        syscall                   # call operating system to perform operation

        li    $v0, 1              # print shape of matrixA
        la    $s0, shapeA
        lw    $a0, 0($s0)
        syscall

        li    $v0, 4              # system call code for printing string = 4
        la    $a0, matrix
        syscall                   # call operating system to perform operation

        li    $v0, 1
        lw    $a0, 4($s0)
        syscall

        li    $v0, 4              # system call code for printing string = 4
        la    $a0, str4
        syscall                   # call operating system to perform operation

        li    $v0, 4              # system call code for printing string = 4
        la    $a0, str2
        syscall                   # call operating system to perform operation

        li    $t5, 1
  L4:   lwc1  $f6, 0($s3)         # load y element
        c.lt.s $f6, $f8           # if (y element < 0.0), go to mx
        bc1t  mx

        li    $v0, 2              # print all y elements in a row
        lwc1  $f12, 0($s3)
        syscall

        beq   $t5, $s5, L5        # if reading-rows is done, go to L5

        li    $v0, 4              # system call code for printing string = 4
        la    $a0, nl
        syscall                   # call operating system to perform operation

        addi  $t5, $t5, 1
        addi  $s3, $s3, 4         # $s3 += 4, go to next element

        j     L4

  mx:  swc1  $f8, 0($s3)          # set y element as 0.0
        j     L4

  L5:   li    $v0, 4              # system call code for printing string = 4
        la    $a0, str3
        syscall                   # call operating system to perform operation

        li $v0,10
        syscall

.end
