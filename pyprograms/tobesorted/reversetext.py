#reverse text from bottom to top
import fileinput
list=[]
for line in fileinput.input("sampletext.txt"):
    line.strip()
    #line=line.replace(" .",".")
    line=line.replace(".",". ")
    list.append(line)
#list.reverse()
fo = open("sampletext.txt1", "w")
for line in list:
    fo.write(line)
fo.close()