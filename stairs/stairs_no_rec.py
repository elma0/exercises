import sys

def stairs(n):
    if n == 1 :
        return 1
    if n == 2 :
        return 2
    if n == 3 :
        return 4
    if n > 3 :     
       a,b,c = 1,2,4
       res = 0
       for i in range(3,n) :
           res = a + b + c + 7
           a = b
           b = c
           c = res
       return res

print stairs(int(sys.argv[1]))

