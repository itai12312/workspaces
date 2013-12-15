import hashlib,random
f=open("a.txt","w")
arr=[]
length=100
i=1
key=''
s=[]
for num in range(1,10):
    s+=chr(int(random.random()*26+97))
r=''.join([e+'' for e in s]).rstrip()
print r
#hashlib.md5("Nobody inspects the spammish repetition").digest()
while i<length:
    print hashlib.md5(key).digest()
    i=i+1