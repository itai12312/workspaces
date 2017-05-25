# -*- coding: utf-8 -*-
import math
import random
import time
strheb=u"אבגדהוזחטיכךלמםנןסעפףצץקרשת"
strheb+="qw"
originalkeyboard=[4,22,12,11,23,6,20,16,5,15,13,18,17,25,8,24,7,11,14,9,19,16,18,2,3,10,17,0,1]
heb=[u"א",u"ב",u"ג",u"ד",u"ה",u"ו",u"ז",u"ח",u"ט",u"י",u"כ",u"ך",u"ל",u"מ",u"ם",u"נ",u"ן",u"ס",u"ע",u"פ",u"ף",u"צ",u"ץ",u"ק",u"ר",u"ש",u"ת",u"/",u"'"]
frequneciesletters=[4.94,4.98,1.26,2.59,8.18,10.00,0.88,2.36,1.08,10.78,2.29,0.41,6.29,4.75,2.85,3.34,1.27,1.55,2.97,1.79,0.19,1.28,0.14,1.95,5.42,4.55,5.32,0.0,0.0]
bigrams=["ות","ים","הי","יו","של","ני","יה","את","ור","לה","רי","וא","או","ול","לא","לי","מו","יי","לו","די","הו","על","רו","ית","בי"]
frequenceisbigrans=[2.14,2.02,1.91,1.47,1.42,1.30,1.29,1.17,1.16,1.15,1.14,1.10,1.07,1.00,0.99,0.95,0.90,0.90,0.83,0.83,0.82,0.82,0.82,0.79,0.76]#of all bigrams
#keymapping=[indexofletter,,,,....]
keyhardness=[4,2,2,3,4,5,3,2,2,4,1.5,1,1,1,3,3,1,1,1,1.5,4,4,3,2,5,3,2,3,4]
"""
order of letters
qwertyuiopa asdfghjkl;zxcvbnm,.

(in hebrewm ., not changing position-maybe do a version where they do)

http://www.tutorialspoint.com/python/python_tutorial.pdf
"""
percantagesforcretria=[10,10,10,10,10,10,10,10,10,10,0,0,0]
def lettertoindex(str):
    if str in letter_index:
        return letter_index[str]
    return -1# returns -1
def distancerelative(pos):
    if(level(pos)==1 or level(pos)==1.5):
        return 0
    elif(level(pos)==2):
        return 2*1
    elif(level(pos)==3):
        return 2*1.5
    elif(level(pos)==4):
        return 2*2.5
    elif(level(pos)==5):
        return 2*3
def distance(pos):
    if(level(pos)==1 or level(pos)==1.5):
        return 0
    if(level(pos)==2 or level(pos)==3 or level(pos)==4):
        return 2*1#move finger back-forth
    if(level(pos)==5):
        return 2*1.5
def level(pos):
    if(pos==11 or pos==12 or pos==13 or pos==16 or pos==17 or pos==18):
        return 1
    elif(pos==10 or pos==19):
        return 1.5
    elif(pos==1 or pos==2 or pos==23 or pos==26 or pos==7 or pos==8):
        return 2
    elif(pos==3 or pos==22 or pos==14 or pos==15 or pos==25 or pos==6 or pos==27):
        return 3
    elif(pos==0 or pos==20 or pos==21 or pos==4 or pos==9 or pos==28):#not chagngin period or comma location
        return 4
    elif(pos==24 or pos==5):
        return 5
def finger(pos):#which finger, 0=pinky
    for i in range(0,5):
        if(pos==0+i  or  pos==10+i  or  pos==20+i):
            return i
def hand(pos):#which hand
    if(pos<5 and pos>-1):
        return 0#left hand
    elif(pos<15 and pos>9):
        return 0#left hand
    elif(pos<25 and pos>19):
        return 0#left hand
    return 1#right hand
def sigmausage(array,keyboard):
    sum=0
    for i in range (0,len(array)):
        sum+=frequneciesletters[keyboard[i]]
    return sum
# index 0=% usage of middle colums
# 1=% usage in left hand(shoukd be a little more than 50)
# 2=% usage in each finger(comapred to what its supposed to be for each finger)->replaced with ferqu*difficuktness
# 3=% same hand usage/alternation
# 4=% same finger utalizations(percentage of strokes that are done by same finger as previous one)
# 5=overall easynesss=sigam for all keys:frequency*hardness->changed to number 2
# 6= %percent of of letter in level 1+1.5
# 7= %percent of of letter in level 1+1.5+2
# 8= total distance(in terms of 1 keyspace)
# 9=total distance relative to difficultness
# 10=bigrams(relative to freqeuncy) that can be typed in level 1+1.5
# 11=number of letters changed from currect location

sampletxt=open("ab","r")
data1=sampletxt.read()
sampletxt.close()
#data=unicode(data,"UTF-16")[:len(data)/10]
data = ''.join(ch for ch in data1 if ch in "קראטוןםפשדגכעיחלךףזסבהנמצתץ")
letter_index={}
for i in range(len(heb)):
    letter_index[heb[i]]=i

def fitness(keyboard):   
    global data 
    a=time.clock()
    values=[0]*12    
    values[0]=sigmausage([4,5,14,15,24,25],keyboard)/100# normalize
    
    values[1]=sigmausage([0,1,2,3,4,10,11,12,13,14,15,20,21,22,23,24,25],keyboard)/100# normalize
    
    values[2]=0
    for i in range (0,len(keyboard)):
        values[2]+=frequneciesletters[keyboard[i]]*(5-level(keyboard[i]))    
    #normalize value 2
    
    handtotal=0
    handchanges=0
    fingertotal=0
    fingerchanges=0
    dist=0
    relativedist=0
    #print len(data)
    for i in range(0,len(data)-1):#read a sample file
        if data[i] in letter_index and data[i+1] in letter_index:
            if(hand(data[i])!=hand(data[i+1])):
                handtotal=handtotal+1
                handchanges=handchanges+1
            else:
                handtotal=handtotal+1#increases handtotal by 1 #useful comment
            if(finger(data[i])!=finger(data[i+1])):
                fingertotal=fingertotal+1
                fingerchanges=fingerchanges+1
            else:
                fingertotal=fingertotal+1
            dist+=distance(lettertoindex(data[i]))
            relativedist+=distancerelative(lettertoindex(data[i]))
    #print time.clock()-a
    values[3]=handchanges/handtotal if handtotal !=0 else 0
    values[4]=fingerchanges/fingertotal if fingertotal!= 0 else 0
    
    values[5]=values[2]
    
    values[6]=sigmausage([10,11,12,13,16,17,18,19],keyboard)/100

    values[7]=sigmausage([1,2,23,26,7,8,10,11,12,13,16,17,18,19],keyboard)/100
    
    values[8]=dist
    values[9]=relativedist
    
    """"bigrams1=0
    for i in range(0,len(bigrams)):
        if(level(lettertoindex(bigrams[i][0]))==1  or  lettertoindex(bigrams[i][0])==1.5):
            if(lettertoindex(bigrams[i][1])==1  or  lettertoindex(bigrams[i][1])==1.5):
                bigrams1+=frequenceisbigrans[i]
    """
    values[10]=0#bigrams1/100
    
    changed=0
    for i in range(0,len(keyboard)):
        if(keyboard[i]!=originalkeyboard[i]):
            changed=changed+1
    values[11]=changed/len(keyboard)    
    
    sum=0
    for i in range (0,len(values)):
        sum+=values[i]*percantagesforcretria[i]
    #print "end:",time.clock()-a
    return sum*100/len(values)#in percantage, assuming each values is from 0 to 1
keyboards34=[]
def firstgenrator(keyboards):
    for k in range(0,500):
        r=range(0,29)
        random.shuffle(r)
        keyboards.append(r)
    return keyboards
    #maybe use hamddei method for first one
def mutation(keyboard1):
    keyboard2=keyboard1[:]   
    i=random.randint(0,28)
    j=random.randint(0,28)
    temp=keyboard2[i]
    keyboard2[i]=keyboard2[j]
    keyboard2[j]=temp
    return keyboard2
def nextgeneration(keyboardarray):
    keyboards1=keyboardarray[:]
    #generate stage
    for i in range(0,len(keyboardarray)):
        for j in range(0,5):
            keyboards1.append(mutation(keyboardarray[i]))
    #eval stage-need to tri|m to a 50 sets
    keyboards1.sort(reverse=True,key=fitness)
    keyboards1 = keyboards1[:10]
    return keyboards1
#def start():

#print type(heb[0])
keyboards34=firstgenrator(keyboards34)
for i in range(0,3):
    keyboards34=nextgeneration(keyboards34)
    #p = list(keyboards1)
    #p.sort(reverse=True)
print(keyboards34)

def keyboardtoprint(arr):
    first10 = []
    second10 = []
    last6 = []
    for i in range(0,10):
        for k in range(len(arr)):
            if arr[k] == i:
                first10.append(strheb[k])

    for i in range(11,20):
        for k in range(len(arr)):
            if arr[k] == i:
                second10.append(strheb[k])

    for i in range(21,27):
        for k in range(len(arr)):
            if arr[k] == i:
                last6.append(strheb[k])

    print first10
    print second10
    print last6

keyboardtoprint(keyboards34[-1])
c=5
