import os
primes=open("primemillion.txt","r")
str=primes.read(os.path.getsize('primemillion.txt'))
a=[]
raw_input("s");
a=str.split(" ")#prime numbers
sumdivisors=[]#sum of divisors
numbers=[]
k=0#counter
raw_input("a \n");
for i in range(1,1000000):
    if(sums(i)<1000000):
        if(i!=sums(i)):
            if(i!=sums(sums(i))):
                numbers[k]=i
                k=k+1
print numbers
def sums(number):
    if(sumdivisors[number]>0):
        return sumdivisors[number]
    elif(inarray(number)==True):
        sumdivisors[number]=1+number
        return sumdivisors[number]
    for j in range(1,sqrt(i+2)):
        if(i%j==0):
            sumdivisors[number]=(j+1)*sums(number/j)
    return sumdivisors[number]
def inarray(number):
    for i in range(0,length(a)):
        if(a[i]==number):
            return True
    return False