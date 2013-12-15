istring=open("restricted.txt","r").read()
string=""
for k in istring:
    if ord(k) in range(32,96):
        string+=k
print string
string2=""

for w in range(0,len(string),4):
    sum=0
    for counter in range(4):
        print sum,string[w+counter],ord(string[w+counter])    
        sum+=(ord(string[w+counter])-32)*(64**(3-counter))
        print counter,(ord(string[w+counter])-32)*(64**(3-counter)),"=",sum
    string2+=chr(sum/65536)+chr((sum/256)%256)+chr(sum%256)
#print string2
open("result.txt","w").write(string2)