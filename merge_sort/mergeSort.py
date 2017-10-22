def merge(a,b):
    res=[]
    while len(a)>0 and len(b)>0:
        if a[0]<=b[0]:
            res.append(a[0])
            a=a[1:]
        else:
            res.append(b[0])
            b=b[1:]
    if len(a)>0:
        res+=a
    else:
        res+=b
    return res;

        


def mergeSort(input):
    if len(input)<=1:
        return input;
    mid = len(input)/2 
    a = mergeSort(input[:mid])
    b = mergeSort(input[mid:])
    return merge(a,b)



ii = [5,7,3,1,8,9,4,6,8,70,47]
print(mergeSort(ii))
