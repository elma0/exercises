def mergeSort(arr):    
    if len(arr) <= 1: 
       return arr    
    mid = len(arr) / 2 
    left = mergeSort(arr[:mid])
    right = mergeSort(arr[mid:])
    res = sort(left,right)
    return res

def sort(left,right):
    result = []
    l = r = 0
    while l<len(left) and r<len(right):        
        if left[l] < right[r]:            
            result.append(left[l])            
            l+=1
        else:
            result.append(right[r])
            r+=1
    if l<len(left):
        result.extend(left[l:])
    else:
        result.extend(right[r:])
    return result


a=[5,8,3,7,1,9,4,6,6,8,1,11,3,564,8,7,926,6,4,987,88,44,66,22,77,33,22,1,15,46,78,65,151,67,84,66]
print 'first: '
print mergeSort(a)


b = [5, 6, 7, 9, 8, 1, 3 ]
print 'second: '
print mergeSort(b)


