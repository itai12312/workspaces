def gen1(str):
    data=[]
    for st in str:
        data.extend((st+">",st+"<",st+"+",st+"-",st+"["))
        if(st.endswith("[")==False):
            data.append(st+"]")
    return data
def compile(str):
    data=[0]
    index=0
    i=0
    while i<len(str):
        if(str[i]==">"):
            index=index+1
            if(len(data)>=index):
                data.append(0)
        if(str[i]=="<"):
            index=index-1
            if(index<0):
                index=0
        if(str[i]=="+"):
            data[index]=data[index]+1
        if(str[i]=="-"):
            data[index]=data[index]-1
            if(data[index]<0):
                data[index]=0
        if(str[i]=="["):
            if(data[index]==0):
                for j in range (i,len(str)):
                    if(str[j]=="]"):
                        i=j+1
                        break
        if(i<len(str) and str[i]=="]"):
            j=i-1
            while j>=0:
                if(j>-1 and str[j]=="["):
                    i=j-1#not i=j becuase of line 39
                    break                
                j=j-1                                
        i=i+1
    return data
def gen(i,string):
    str=[""]
    #arr=[">","<","+","-","[","]"]
    for p in range (0,i):
        str=gen1(str)
        for st in str:
            if compile(st)==string:
                print "s"
                print st
                break
def run(str):
    array1=[]
    i=0
    commapos=0
    #ini
    while i<len(str):
        if(str[i]==","):
            array1.append(int(float(str[commapos:i])))
            commapos=i+1
        i=i+1
def getPattren(str):
    leng=10
    gen(leng,run(str))
getPattren("1,2,3,4")

"""

str = raw_input("Enter your input: ");
print "Received input is : ", str

in check - coukd be a situation like this [+>] so create an infinite array, so need to check only first n  elements
"""
