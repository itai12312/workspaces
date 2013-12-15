import itertools

class Group:
    def __init__(self,sz):
        self.size=sz
        self.props={}
    def props_of_type(self,kind):
        if kind not in self.props:
            return {}
        return self.props[kind]

class FactIs:
    def __init__(self,prop,value):
        if type(prop) not in [list,tuple]:
            prop=(prop,)
        self.prop=prop
        self.value=value
    def new_for(self,group):
        if self.prop[0] not in group.props:
            return True
        if self.prop not in group.props[self.prop[0]]:
            return True
        if group.props[self.prop[0]][self.prop] != self.value:
            return True
        return False
    def add_to(self,group):
        if self.prop[0] not in group.props:
            group.props[self.prop[0]]={}
        group.props[self.prop[0]][self.prop]=self.value
    def __str__(self):
        return self.prop[0].to_str(self.prop,self.value)

class FactTrue(FactIs):
    def __init__(self,*args):
        FactIs.__init__(self,args,True)
    
class FactFalse(FactIs):
    def __init__(self,*args):
        FactIs.__init__(self,args,False)
        
class FactOneOf(FactIs):
    def __init__(self,prop,value):
        FactIs.__init__(self,prop,value)
    def new_for(self,group):
        if self.prop[0] not in group.props:
            return True
        if self.prop not in group.props[self.prop[0]]:
            return True
        for option in group.props[self.prop[0]][self.prop]:
            if option not in self.value:
                return True
        return False
    def add_to(self,group):
        if self.prop[0] not in group.props:
            group.props[self.prop[0]]={}
        if self.prop not in group.props_of_type(self.prop[0]):
            group.props[self.prop[0]][self.prop]=self.value
        else:
            prev_value=group.props[self.prop[0]][self.prop]
            new_value=list(set(prev_value)&set(self.value))
            group.props[self.prop[0]][self.prop]=new_value

class HasSub:
    @staticmethod
    def to_str(prop,value):
        if value:
            return "Has a subgroup of size %d"%(prop[1])
        else:
            return "Doesn't have a subgroup of size %d"%(prop[1])
    
class HasNormSub:
    @staticmethod
    def to_str(prop,value):
        if value:
            return "Has a normal subgroup of size %d"%(prop[1])
        else:
            return "Doesn't have a normal subgroup of size %d"%(prop[1])

class IsSimple:
    @staticmethod
    def to_str(prop,value):
        if value:
            return "Is simple"
        else:
            return "Is not simple"

class PosSubSize:
    @staticmethod
    def to_str(prop,value):
        return "Possible sizes of subgroups are %s"%(",".join(map(str,value)))

class PosNumSubs:
    @staticmethod
    def to_str(prop,value):
        return "Has %s subgroups of size %d"%(",".join(map(str,value)),prop[1])

def factors(num):
    d=2
    power=0
    retval={}
    while num > 1:
        if num % d == 0:
            num/=d
            power+=1
        else:
            if power != 0:
                retval[d]=power
                power=0
            d+=1
    if power != 0:
        retval[d]=power
        power=0
    return retval

def divisors(num):
    fs=factors(num).items()
    primes=[f[0] for f in fs]
    exps=[f[1] for f in fs]
    power_ranges=[range(e+1) for e in exps]
    retval=[]
    for powers in itertools.product(*power_ranges):
        retval.append(reduce(lambda x,y:x*y,
            [p**power for p,power in zip(primes,powers)],1))
    return sorted(retval)

class Index2Normal:
    descr="A subgroup of index 2 is normal"
    @staticmethod
    def infer(group):
        if group.size % 2 != 0:
            return [] #no subgroups of index 2 if |G| is odd
        retval=[]
        subgroups=group.props_of_type(HasSub)
        for prop in subgroups:
            if prop[1] == group.size/2 and subgroups[prop] == True:
                retval.append(FactTrue(HasNormSub,prop[1]))
        return retval

class LagrangeSubgroups:
    descr="The size of a subgroup divides |G|"
    @staticmethod
    def infer(group):
        return [FactOneOf(PosSubSize,divisors(group.size))]

class Sylow1:
    descr="G has a p-sylow group if p divides |G|"
    @staticmethod
    def infer(group):
        fs=factors(group.size)
        retval=[]
        for p in fs:
            p_group_size=p**fs[p]
            retval.append(FactTrue(HasSub,p_group_size))
        return retval

class Sylow1Extended:
    descr="G has a subgroup of size p**k if p**k divides |G|"
    @staticmethod
    def infer(group):
        fs=factors(group.size)
        retval=[]
        for p in fs:
            for power in range(1,fs[p]+1):
                p_group_size=p**power
                retval.append(FactTrue(HasSub,p_group_size))
        return retval

class Sylow3:
    descr="No. of p-sylow subgruops is 1 mod p and divides |G|/p**k"
    @staticmethod
    def infer(group):
        fs=factors(group.size)
        retval=[]
        for p in fs:
            p_group_size=p**fs[p]
            m=group.size/p_group_size
            possible_numbers=[]
            for d in divisors(m):
                if d % p == 1:
                    possible_numbers.append(d)
            retval.append(FactOneOf((PosNumSubs,p_group_size),possible_numbers))
        return retval

class SimpleDefinition:
    descr="G isn't simple if it has a proper normal subgroup"
    @staticmethod
    def infer(group):
        normal_subs=group.props_of_type(HasNormSub)
        for prop in normal_subs:
            if normal_subs[prop] == True:
                if prop[1] != 1 and prop[1] != group.size:
                    return [FactFalse(IsSimple)]
        return []

class SingleOfSizeNormal:
    descr="If G has just one subgroup of a size, it is normal"
    @staticmethod
    def infer(group):
        num_subs=group.props_of_type(PosNumSubs)
        retval=[]
        for prop in num_subs:
            if num_subs[prop] == [1]:   
                #the only possible number of subgroups of size
                #prop[1] is 1
                retval.append(FactTrue(HasNormSub,prop[1]))
        return retval

tools=[
    Index2Normal,
    LagrangeSubgroups,
    SimpleDefinition,
    SingleOfSizeNormal,
    Sylow1,
    Sylow1Extended,
    Sylow3]

def deduce(group):
    found_new=True
    while found_new:
        found_new=False
        for tool in tools:
            results=tool.infer(group)
            for r in results:
                if r.new_for(group):
                    print "%s (from %s)"%(r,tool.__name__)
                    r.add_to(group)
                    found_new=True

print "Tools (theorems and techniques) to be used:"
print "-------------------------------------------"
for t in tools:
    print "%s: %s"%(t.__name__,t.descr)

while True:
    print
    print "Enter details about your group:"
    sz=int(raw_input("Size:"))
    print
    print "Conclusions:"
    group=Group(sz)
    deduce(group)