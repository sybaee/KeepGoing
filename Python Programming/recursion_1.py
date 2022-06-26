"""
Description: printing subsequences for observing the behavior of a recursive program
"""

def fast_result(sub_lst, avail, memo):
    nextitem = sub_lst[0]
    if nextitem[2] <= avail:
        chosen1, val1 = bfast_max(sub_lst[1: ], avail - nextitem[2], memo)
        chosen1 = chosen1 + (nextitem, )
        val1 += nextitem[1]
        chosen2, val2 = bfast_max(sub_lst[1: ], avail, memo)
        if val1 > val2:
            result = chosen1, val1
        else:
            result = chosen2, val2

    else:
        print(" " * 4 * (level + 1), "No left node")
        result = bfast_max(sub_lst[1: ], avail, memo)

    return result

def display(list_of_items, weight, flag = False):
    nlist = []
    for item in list_of_items:
        nlist.append(item[0])

    if flag: # flag가 True라면 이미 전에 한번 풀었던 과정이므로 already solved 출력하기 
        print(" " * 4 * level, nlist, weight, "Already solved")
    else:
        print(" " * 4 * level, nlist, weight)

def bfast_max(sub_lst, avail, memo = {}):
    global level, count
    level += 1
    if (len(sub_lst), avail) in memo: # dictionary memo에 넣어서 같은 과정 반복 없애기 
        display(sub_lst, avail, flag = True) # flag를 True로 만들어서 Already solve 출력하기 
        level -= 1
        return memo[(len(sub_lst), avail)]

    display(sub_lst, avail, flag = False)
    if sub_lst == [] or avail == 0:
        level -= 1
        return (), 0

    count += 1
    result = fast_result(sub_lst, avail, memo)
    memo[(len(sub_lst), avail)] = result
    level -= 1
    return result

names = ["a", "b", "c", "d"]
vals = [6, 7, 8, 9]
weights = [3, 3, 2, 5]
items = []
for i in range(len(names)): # item list 만들기 
    items.append((names[i], vals[i], weights[i]))

count = 0 # recursion이 일어난 횟수 
level = -1
taken, val = bfast_max(items, 5)
print("\n")
for item in taken:
    print(item) # 선택된 item의 (item, value, weight)를 각각 print
print("Total value of items taken =", val, "count =", count)