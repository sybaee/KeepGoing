"""
Description: printing subsequences for observing the behavior of a recursive program
"""

def fast_fib(n, memo = {}):
    global count, level
    level += 1
    if n in memo: # n값이 이미 dictionary memo 안에 있다면 이미 풀었다는 뜻이므로 다시 계산 안하기 
        display(n, flag = True)
        level -= 1
        return memo[n]

    display(n, flag = False)
    if n == 0 or n == 1:
        level -= 1
        return 1

    count += 1
    result = fast_fib(n - 1, memo) + fast_fib(n - 2, memo)
    memo[n] = result
    level -= 1
    return result

def display(n, flag = False):
    if flag:
        print(" " * 4 * level, "fib(" + str(n) + ")", "Already computed")
    else:
        print(" " * 4 * level, "fib(" + str(n) + ")")

count = 0
level = -1
result = fast_fib(6)

print()
print(result, count)