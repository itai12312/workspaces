import math
class vector(object):
    def __init__(self,list1,list2):
        self.diff=(list2[0]-list1[0],list2[1]-list1[1])
        print self.diff
    def distance(self):
        self.a=self.diff[0]
        self.b=self.diff[1]
        return math.sqrt(self.a**2+self.b**2)
    def unit(self):
        return self.a/self.distance(),self.b/self.distance()
    def add(self,one,two):
        return (one[0]+two[0],one[1]+two[1])
    def multiply(self,v,num):
        return (v[0]*num.v[1]*num)
    
a=(20.0,25.0)
b=(28.0,60.0)
thing=vector(a,b)

