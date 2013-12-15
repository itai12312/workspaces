f = open('myEncryptorTest.txt')
encryptedString = f.read()
f.close()
print encryptedString
newstr = encryptedString.replace("\r", "")
newstr1 = newstr.replace("\n", "")
newstr2=newstr1.split('.')
i=0
max=800;min=800
newstr3=[0] * (len(newstr2)/3)
while (3*i+2)<len(newstr2):
    newstr3[i]=int(newstr2[i])+int(newstr2[i+1])+int(newstr2[i+2])
    if newstr3[i]>max:
        max=newstr3[i]
    elif newstr3[i]<min:
        min=newstr3[i]
    i+=1
print newstr3
print min
print max
for j in range(-728,-633):
    st=""    
    for k in range(0,len(newstr3)):
        if (newstr3[k]+j)>31 and (newstr3[k]+j)<127:
            st+=str(unichr(newstr3[k]+j))
    print j
    print st