import sys

def stairs(n):
    if n == 1 :
        return 1
    if n == 2 :
        return 2
    if n == 3 :
        return 4
    if n > 3 :
        return stairs(n-3) + stairs(n-2) + stairs(n-1) + 7

print stairs(int(sys.argv[1]))

