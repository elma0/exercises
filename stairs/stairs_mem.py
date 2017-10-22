import sys

def stairs(n):
    arr = [0 for x in range(n)]
    return stairsM(n-3,arr) + stairsM(n-2, arr) + stairsM(n-1,arr) + 7

def stairsM(n, arr):
    if arr[n-1] == 0 :        
       if n == 1 :
           arr[0] = 1
           return 1
       if n == 2 :
           arr[1] = 2
           return 2
       if n == 3 :
           arr[2] = 4
           return 4
       if n > 3 :        
           arr[n-1] = stairsM(n-3,arr) + stairsM(n-2,arr) + stairsM(n-1,arr) + 7
           return arr[n-1]
    else:
        return arr[n-1]

print stairs(int(sys.argv[1]))

