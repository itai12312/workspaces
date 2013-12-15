import hashlib
import OpenGL.GLUT as glut
import OpenGL.GL as gl
import OpenGL
#OpenGL.ERROR_CHECKING = False
import OpenGL.GL.ARB.framebuffer_object as glarb
import OpenGL.GL.EXT.framebuffer_blit as glblit
import Image
import time
import math
import random
import binfiles
#import winsound
from numpy import array

G_CHUNK_SIZE=20.0

g_curid=1
g_difficulty=3
def getparam(object,range):
    hs=hashlib.sha1(object).hexdigest()
    return int(hs,16)%range

getparamw_cache={}
def getparamw(object,range):
    global getparamw_cache
    world=12388
    if len(getparamw_cache) > 50000:
        getparamw_cache={}
    if object in getparamw_cache:
        return getparamw_cache[object]%range
    else:
        if range == 0:
            return 0
        hs=hashlib.sha1(object+hex(world)).hexdigest()
        getparamw_cache[object]=int(hs,16)
        return getparamw_cache[object]%range

#scale from the rect [-1,1,1,-1] to rect
def scale(point,rect):
    x=(rect[0]+rect[2])/2.0+point[0]*(rect[0]-rect[2])/2.0
    y=(rect[1]+rect[3])/2.0+point[1]*(rect[1]-rect[3])/2.0
    return [x,y]

forced_id=-1
forced_serial=0
def idcolor(num,serial,repeat):
    if forced_id != -1:
        num=forced_id
        serial=forced_serial
    idcolor=[float(num%131072)/131072,float((num-num%131072)/131072)/131072,float(serial)/65536]
    if int(idcolor[0]*131072+131072*int(idcolor[1]*131072+0.1)+0.1) != num:
        print "ERROR:",num,idcolor
    return [float(num%131072)/131072,float((num-num%131072)/131072)/131072,float(serial)/65536]*repeat

def force_id(num,serial):
    global forced_id,forced_serial
    forced_id=num
    forced_serial=serial
    
def free_id():
    global forced_id
    forced_id=-1

def find_obj(objdict,objid,serial):
    if objid in objdict:
        return objdict[objid]
    else:
        for obj in objdict.values():
            if obj.serial == serial:
                return obj
    return nothing_item

class mesher:
    def open_pyramid4(self,x1,y1,x2,y2,cx,cy,zlo,zhi):
        return [x1,y1,zlo,cx,cy,zhi,x2,y1,zlo,
                x2,y1,zlo,cx,cy,zhi,x2,y2,zlo,
                x2,y2,zlo,cx,cy,zhi,x1,y2,zlo,
                x1,y2,zlo,cx,cy,zhi,x1,y1,zlo]
    def open_pyramid(self,x1,y1,x2,y2,x3,y3,cx,cy,zlo,zhi):
        return [x1,y1,zlo,cx,cy,zhi,x2,y2,zlo,
                x2,y2,zlo,cx,cy,zhi,x3,y3,zlo,
                x3,y3,zlo,cx,cy,zhi,x1,y1,zlo]
    def closed_pyramid(self,x1,y1,x2,y2,x3,y3,cx,cy,zlo,zhi):
        return [x1,y1,zlo,cx,cy,zhi,x2,y2,zlo,
                x2,y2,zlo,cx,cy,zhi,x3,y3,zlo,
                x3,y3,zlo,cx,cy,zhi,x1,y1,zlo,
                x1,y1,zlo,x2,y2,zlo,x3,y3,zlo]
    def closed_unaligned_pyramid(self,x1,y1,z1,x2,y2,z2,x3,y3,z3,cx,cy,cz):
        return [x1,y1,z1,cx,cy,cz,x2,y2,z2,
                x2,y2,z2,cx,cy,cz,x3,y3,z3,
                x3,y3,z3,cx,cy,cz,x1,y1,z1,
                x1,y1,z1,x2,y2,z2,x3,y3,z3]
    def closed_ypyramid(self,x1,z1,x2,z2,x3,z3,cx,cz,ylo,yhi):
        return [x1,ylo,z1,cx,yhi,cz,x2,ylo,z2,
                x2,ylo,z2,cx,yhi,cz,x3,ylo,z3,
                x3,ylo,z3,cx,yhi,cz,x1,ylo,z1,
                x1,ylo,z1,x2,ylo,z2,x3,ylo,z3]
    def cube(self,x1,y1,x2,y2,zlo,zhi):
        return [x1,y1,zlo,x1,y2,zlo,x2,y1,zlo,x2,y1,zlo,x1,y2,zlo,x2,y2,zlo,  #bottom
                x1,y1,zlo,x2,y1,zlo,x1,y1,zhi,x1,y1,zhi,x2,y1,zlo,x2,y1,zhi,  #front
                x1,y1,zlo,x1,y2,zlo,x1,y1,zhi,x1,y1,zhi,x1,y2,zlo,x1,y2,zhi,  #left
                x1,y1,zhi,x1,y2,zhi,x2,y1,zhi,x2,y1,zhi,x1,y2,zhi,x2,y2,zhi,  #top
                x1,y2,zlo,x2,y2,zlo,x1,y2,zhi,x1,y2,zhi,x2,y2,zlo,x2,y2,zhi,  #back
                x2,y1,zlo,x2,y2,zlo,x2,y1,zhi,x2,y1,zhi,x2,y2,zlo,x2,y2,zhi]  #right
    def nobottom_cube(self,x1,y1,x2,y2,zlo,zhi):
        return [x1,y1,zlo,x2,y1,zlo,x1,y1,zhi,x1,y1,zhi,x2,y1,zlo,x2,y1,zhi,  #front
                x1,y1,zlo,x1,y2,zlo,x1,y1,zhi,x1,y1,zhi,x1,y2,zlo,x1,y2,zhi,  #left
                x1,y2,zlo,x2,y2,zlo,x1,y2,zhi,x1,y2,zhi,x2,y2,zlo,x2,y2,zhi,  #back
                x2,y1,zlo,x2,y2,zlo,x2,y1,zhi,x2,y1,zhi,x2,y2,zlo,x2,y2,zhi,  #right
                x1,y1,zhi,x1,y2,zhi,x2,y1,zhi,x2,y1,zhi,x1,y2,zhi,x2,y2,zhi]  #top

class suitability:
    def bigbuilding(self,worldx,worldy,sizex,sizey):
        minzcoord=10000
        maxzcoord=-10000
        for xoff in range(3):
            for yoff in range(3):
                z=cell_ground(worldx+xoff*sizex/2,worldy+yoff*sizey/2).zcoord
                minzcoord=min(minzcoord,z)
                maxzcoord=max(maxzcoord,z)
        return ((minzcoord > 0) and (maxzcoord-minzcoord < 16))

class item:
    def __init__(self,type):
        global g_curid
        self.type=type
        self.arrfied=False
        self.trigs=[]
        self.colors=[]
        self.uses=[]
        self.item_id=g_curid
        self.serial=0
        g_curid+=1
    def init(self,type):
        global g_curid
        self.type=type
        self.arrfied=False
        self.trigs=[]
        self.colors=[]
        self.alive=True
        self.uses=[]
        self.item_id=g_curid
        self.serial=0
        g_curid+=1
    def changed(self):
        return False
    def interact(self):
        pass
    def make(self,chunk,x,y,z=0.0):
        pass
    def update(self,timestep):
        return False
    def __repr__(self):
        return str(self.type)+" item ID"+str(self.item_id)+" (serial "+str(self.serial)+")"

class ground_item(item):
    def __init__(self,height):
        self.init("ground")
        self.height=height
        self.margins=5
        if height > 4.0:
            self.kind="grass"
        elif height > -4.0:
            self.kind="ground"
        else:
            self.kind="water"
        if self.kind == "water":
            self.zcoord=-4.0/8.0
        else:
            self.zcoord=int(self.height/8.0)
    def make(self,chunk,x,y):
        #chunk.obj_trigs+=mesh_calc.nobottom_cube(x/G_CHUNK_SIZE,y/G_CHUNK_SIZE,(x+1)/G_CHUNK_SIZE,(y+1)/G_CHUNK_SIZE,self.zcoord/G_CHUNK_SIZE-0.5,self.zcoord/G_CHUNK_SIZE)[-18:]
        chunk.obj_trigs+=[x,y,self.zcoord,(x+1),(y+1),self.zcoord-self.margins,0,0,0]
        if self.kind == "water":
            topcolor=[0.2,0.2,1.0,1.0]
            topcolor2=[0.4,0.4,1.0,1.0]
            color2=[0.1,0.1,0.5,1.0]
        elif self.kind == "ground":
            topcolor=[0.7,0.7,0.0,1.0]
            topcolor2=[0.9,0.9,0.0,1.0]
            color2=[0.4,0.4,0.0,1.0]
        else:
            topcolor=[0.5,1.0,0.5,1.0]
            topcolor2=[0.2,1.0,0.2,1.0]
            color2=[0.3,0.5,0.3,1.0]
        #chunk.obj_colors+=([0.0,0.0,0.0,1.0]*2+color2*2+[0.0,0.0,0.0,1.0]+color2)*4+topcolor*2+topcolor2+topcolor+topcolor2+topcolor
        chunk.obj_idcolors+=idcolor(self.item_id,self.serial,3)
        topcolor[3]-=3
        chunk.obj_colors+=(topcolor+topcolor2+color2)
        #chunk.obj_idcolors+=idcolor(self.item_id,self.serial,3)
            

class block_item(item):
    def __init__(self,material,zcoord):
        self.init("block")
        self.mat=material
        self.zcoord=zcoord
    def make(self,chunk,x,y):
        chunk.obj_trigs+=mesh_calc.cube(x,y,x+1,y+1,self.zcoord,self.zcoord+1)
        chunk.obj_colors+=[0.35,0.35,0.35,1.0,0.25,0.25,0.25,1.0,0.35,0.35,0.35,1.0]*12
        chunk.obj_idcolors+=idcolor(self.item_id,self.serial,36)

class door_item(item):
    def __init__(self,worldx,worldy,z,dir,lock):
        self.init("door")
        self.closed=True
        self.locked=lock
        self.haslock=lock
        self.worldx=worldx
        self.worldy=worldy
        self.zcoord=z
        self.dir=dir
        print "zcoord:",z
    def changed(self):
        return not self.closed
    def make(self,chunk,x,y):
        if self.closed:
            trigs=mesh_calc.cube(x,y+0.2,x+3,y+0.8,self.zcoord,self.zcoord+6)
            chunk.obj_colors+=[0.9,0.7,0.0,1.0,0.8,0.6,0.0,1.0,0.9,0.7,0.0,1.0]*12
            if self.haslock:
                if self.locked:
                    lockx=x+0.5
                    lockz=self.zcoord+2.5
                else:
                    lockx=x+1.5
                    lockz=self.zcoord+4.0
                trigs+=mesh_calc.closed_ypyramid(lockx,lockz,x+2.5,self.zcoord+2,
                                                    x+2.5,self.zcoord+3,x+2,self.zcoord+2.5,y+0.1,y+0.5)
                chunk.obj_colors+=[0.9,0.3,0.0,1.0,0.8,0.2,0.0,1.0,0.9,0.3,0.0,1.0]*4
                chunk.obj_idcolors+=idcolor(self.item_id,self.serial,12)
        else:
            trigs=mesh_calc.cube(x+2.2,y,x+2.8,y+3,self.zcoord,self.zcoord+6)
            chunk.obj_colors+=[0.9,0.7,0.0,1.0,0.8,0.6,0.0,1.0,0.9,0.7,0.0,1.0]*12
        if self.dir % 2 == 1:
            for i in range(len(trigs)/3):
                tmpx=trigs[i*3]
                trigs[i*3]=trigs[i*3+1]-y+x
                trigs[i*3+1]=tmpx-x+y
        if self.dir == 2:
            for i in range(len(trigs)/3):
                trigs[i*3]=(x+1.5)*2-trigs[i*3]
                trigs[i*3+1]=(y+0.5)*2-trigs[i*3+1]
        elif self.dir == 1:
            for i in range(len(trigs)/3):
                trigs[i*3]=(x+0.5)*2-trigs[i*3]
                trigs[i*3+1]=(y+1.5)*2-trigs[i*3+1]
        chunk.obj_trigs+=trigs
        chunk.obj_idcolors+=idcolor(self.item_id,self.serial,36)
    def interact(self):
        print "door interaction",self.locked
        change_cell(self.worldx,self.worldy)
        me=changes[(self.worldx,self.worldy)][self.item_id]
        if not me.locked:
            me.closed=(not me.closed)

class stick_item(item):
    def __init__(self,worldx,worldy,zcoord=0.0):
        self.init("stick")
        self.zcoord=zcoord
        self.uses=["put"]
        self.worldx=worldx
        self.worldy=worldy
        self.angle=getparamw("stick_angle"+str(self.item_id),100)/100.0*math.pi*2
    def make(self,chunk,x,y,zcoord=None):
        x1=0.5-0.5*math.sin(self.angle)
        y1=0.5-0.5*math.cos(self.angle)
        x2=1-x1
        y2=1-y1
        if zcoord == None:
            zcoord=self.zcoord
        chunk.obj_trigs+=mesh_calc.closed_unaligned_pyramid(
            x+x1-0.05,y+y1-0.05,zcoord+0.1,
            x+x1+0.05,y+y1-0.05,zcoord+0.1,
            x+x1,y+y1+0.05,zcoord,
            x+x2,y+y2,zcoord+0.2)
        chunk.obj_colors+=[0.2,0.2,0.0,1.0,0.4,0.4,0.0,1.0,0.4,0.4,0.0,1.0]*4
        chunk.obj_idcolors+=idcolor(self.item_id,self.serial,12)
    def use(self,tgtdata,worldx,worldy,usage):
        global changes
        if usage == "put":
            change_cell(worldx,worldy)
            target=objectbydata([(worldx,worldy),tgtdata[0],tgtdata[1]])
            if target.type == "ground":
                self.zcoord=target.zcoord
            else:
                return
            self.worldx=worldx
            self.worldy=worldy
            changes[(worldx,worldy)][self.item_id]=self
            player.losecurrent()
        else:
            return
    def interact(self):
        global player
        change_cell(self.worldx,self.worldy)
        if player.in_hand.type == "nothing":
            player.carry(self)
            delitem(self.worldx,self.worldy,self.item_id)

class fireplace_item(item):
    def __init__(self,worldx,worldy,zcoord,size):
        self.init("fireplace")
        self.zcoord=zcoord
        self.worldx=worldx
        self.worldy=worldy
        self.size=size
        self.reduce_time=2.5
    def changed(self):
        return True
    def update(self,timestep):
        self.reduce_time-=timestep
        if self.reduce_time < 0:
            self.size-=1
            self.reduce_time+=2.5
            if self.size == 0:
                delitem(self.worldx,self.worldy,self.item_id)
            return True
        return False
    def make(self,chunk,x,y,zcoord=None):
        if zcoord == None:
            zcoord=self.zcoord
        size=self.size*0.1
        chunk.obj_trigs+=mesh_calc.closed_pyramid(x+0.5-size,y+0.5-size,
                                                  x+0.5+size,y+0.5-size,
                                                  x+0.5,y+0.5+size,
                                                  x+0.5,y+0.5,
                                                  zcoord,zcoord+size*2)
        chunk.obj_colors+=[1.0,1.0,0.2,0.7,0.8,0.0,0.0,0.7,1.0,1.0,0.2,0.7]*4
        chunk.obj_idcolors+=idcolor(self.item_id,self.serial,12)

class tree_item(item):
    def __init__(self,worldx,worldy,zcoord):
        self.init("tree")
        self.fire=0
        self.burntime=0
        self.shouldupdate=False
        self.haschanged=False
        self.zcoord=zcoord
        idstr=str(worldx)+"_"+str(worldy)
        tree_rand=zcoord+getparamw(idstr+"_treetype",40)
        if tree_rand > 50:
            self.treetype="high"
        else:
            self.treetype="low"
        self.height=6+getparamw(self.treetype+"_treeheight",5)
        ftype=getparamw(idstr+"_fruit",100)
        if ftype == 0:
            self.fruit=fruit_item(self.treetype+"_rare",self.zcoord+4)
        elif ftype < 5:
            self.fruit=fruit_item(self.treetype+"_regular",self.zcoord+4)
        else:
            self.fruit=nothing_item
    def changed(self):
        return not (self.haschanged or (self.fire == 1))
    def update(self,timestep):
        if self.fire:
            self.burntime-=timestep
            if self.burntime < 0.0:
                self.burntime=0
                self.fire=0
                return True
        if self.shouldupdate:
            self.shouldupdate=False
            return True
        return False
    def make(self,chunk,x,y):
        if self.treetype == "low":
            color=[0.0,0.8,0.0,1.0]
            color2=[0.0,1.0,0.0,1.0]
        else:
            color=[0.1,0.4,0.0,1.0]
            color2=[0.0,0.8,0.0,1.0]
        left=(x+0.6)
        mid=(x+1.5)
        right=(x+2.4)
        top=(y+2.3)
        bottom=(y+0.7)
        chunk.obj_trigs+=mesh_calc.open_pyramid(x+1.2,y+1.2,x+1.5,y+1.8,
                                                x+1.8,y+1.2,x+1.5,y+1.5,
                                                self.zcoord,(self.zcoord+self.height))
        chunk.obj_trigs+=mesh_calc.closed_pyramid(left,bottom,mid,top,right,bottom,mid,y+1.5,
                                                self.zcoord+5,self.zcoord+self.height)
        chunk.obj_colors+=[0.7,0.5,0.0,1.0]*9
        for i in range(3):
            chunk.obj_colors+=color*2
            chunk.obj_colors+=color2
        chunk.obj_colors+=color*3
        if self.fire == 1:
            chunk.obj_trigs+=mesh_calc.closed_pyramid(left,bottom,mid,top,right,bottom,mid,y+1.5,
                                                    self.zcoord+6,self.zcoord+self.height+1)
            color=[1.0,1.0,0.2,0.7]
            color2=[0.8,0.0,0.0,0.7]
            print "burning"
            for i in range(3):
                chunk.obj_colors+=color*2
                chunk.obj_colors+=color2
            chunk.obj_colors+=color*3
            chunk.obj_idcolors+=idcolor(self.item_id,self.serial,12)
        chunk.obj_idcolors+=idcolor(self.item_id,self.serial,21)
        force_id(self.item_id,self.serial)
        self.fruit.make(chunk,x+0.1,y+0.2)
        self.fruit.make(chunk,x+1.0,y+1.8)
        free_id()
    def interact(self):
        global player
        print player.in_hand.type
        if player.in_hand.type == "nothing":
            print "takefruit"
            player.carry(self.fruit)
            self.fruit=nothing_item
            self.shouldupdate=True
            if self.fruit.type != "nothing":
                self.haschanged=True
    

class fruit_item(item):
    def __init__(self,rarity,zcoord):
        self.init("fruit")
        self.worldx=0
        self.worldy=0
        self.zcoord=zcoord
        self.uses=["put","squeeze"]
        self.shouldupdate=False
        self.rarity=rarity
        colorstr=rarity+"_fruit_c"
        self.color=[getparamw(colorstr+"1",256)/256.0,getparamw(colorstr+"2",256)/256.0,getparamw(colorstr+"3",256)/256.0,1.0]
        self.color2=[self.color[0]+0.2,self.color[1]-0.1,self.color[2]+0.1,1.0]
    def make(self,chunk,x,y,z=None):
        left=(x+0.2)
        right=(x+0.8)
        top=(y+0.8)
        bottom=(y+0.2)
        ymid=(y+0.5)
        xmid=(x+0.5)
        if z == None:
            z=self.zcoord
        chunk.obj_trigs+=mesh_calc.open_pyramid(left,bottom,xmid,top,right,bottom,xmid,ymid,(z+0.5),(z+1))
        chunk.obj_trigs+=mesh_calc.open_pyramid(left,bottom,xmid,top,right,bottom,xmid,ymid,(z+0.5),z)
        for i in range(6):
            chunk.obj_colors+=self.color*2
            chunk.obj_colors+=self.color2
        ic=idcolor(self.item_id,self.serial,18)
        chunk.obj_idcolors+=ic
    def update(self,timestep):
        if self.shouldupdate:
            self.shouldupdate=False
            return True
        return False
    def use(self,tgtdata,worldx,worldy,usage):
        global changes
        if usage == "put":
            change_cell(worldx,worldy)
            target=objectbydata([(worldx,worldy),tgtdata[0],tgtdata[1]])
            if target.type == "ground":
                self.zcoord=target.zcoord
            else:
                return
            self.worldx=worldx
            self.worldy=worldy
            changes[(worldx,worldy)][self.item_id]=self
            player.losecurrent()
        else:
            change_cell(worldx,worldy)
            target=objectbydata([(worldx,worldy),tgtdata[0],tgtdata[1]])
            if target.type == "jug":
                target.fill(liquid_item(getparamw("fruit_juice_"+self.rarity,1000000)))
            else:
                return
            player.losecurrent()
    def interact(self):
        global player
        change_cell(self.worldx,self.worldy)
        if player.in_hand.type == "nothing":
            player.carry(self)
            delitem(self.worldx,self.worldy,self.item_id)

class liquid_item(item):
    def __init__(self,num):
        self.init("liquid")
        self.num=num
        self.ingredients=[num]
    def effect(self):
        rand=getparamw("liq_effect_"+str(self.num),10)
        quality=getparamw("liqqual_"+str(self.num),10)*len(self.ingredients)
        if rand < 5:
            return resize_effect(quality,getparamw("liq_resizedir_"+str(self.num),2)*2-1)
        else:
            return invisible_effect(quality)
    def mixwith(self,other):
        if other.num not in self.ingredients:
            self.ingredients.append(other.num)
        else:
            return
        first=max(self.num,other.num)
        second=min(self.num,other.num)
        self.num=getparamw("liquid_mix_"+str(first)+"_"+str(second),1000000)
    def heat(self):
        if self.ingredients[-1] != -1:
            if getparamw("liq_heateffect_"+str(self.num),3) == 0:
                self.num=getparamw("liq_heat_"+str(self.num),1000000)
                self.ingredients.append(-1)
    def gcolors(self):
        idstr=str(self.num)
        color1=[getparamw("liquid_c1_"+idstr,256)/256.0,getparamw("liquid_c2_"+idstr,256)/256.0,getparamw("liquid_c3_"+idstr,256)/256.0,1.0]
        if len(self.ingredients) > 1:
            if getparamw("liquid_chcolor"+idstr,9)-len(self.ingredients) < 1:
                idx=getparamw("liquid_chcoloridx"+idstr,3)
                color1[idx]=-color1[idx]
        color2=color1[:]
        change_pos=getparamw("liquid_cinv_"+idstr,3)
        color2[change_pos]=0.5+color2[change_pos]
        if color2[change_pos] > 1.0:
            color2[change_pos]-=1.0
        return (color1,color2)

class jug_item(item):
    def __init__(self,worldx,worldy,zcoord):
        self.init("jug")
        self.worldx=worldx
        self.worldy=worldy
        self.zcoord=zcoord
        self.shouldupdate=False
        self.uses=["put","pour","drink"]
        self.contain=nothing_item
    def update(self,timestep):
        if self.shouldupdate:
            self.shouldupdate=False
            return True
        return False
    def fill(self,liquid):
        if self.contain.type == "nothing":
            self.contain=liquid
        else:
            self.contain.mixwith(liquid)
        self.shouldupdate=True
    def make(self,chunk,x,y,z=None):
        if z == None:
            z=self.zcoord
        chunk.obj_trigs+=mesh_calc.nobottom_cube(x+0.1,y+0.1,x+0.9,y+0.9,z+0.9,z+0.1) #outside
        chunk.obj_trigs+=mesh_calc.nobottom_cube(x+0.2,y+0.2,x+0.8,y+0.8,z+0.9,z+0.15) #inside
        chunk.obj_colors+=[0.35,0.35,0.35,1.0,0.25,0.25,0.25,1.0,0.35,0.35,0.35,1.0]*20
        chunk.obj_idcolors+=idcolor(self.item_id,self.serial,60)
        if self.contain.type != "nothing":
            print "HAS LIQUID"
            left=x+0.2
            right=x+0.8
            bottom=y+0.2
            top=y+0.8
            lz=self.zcoord+0.8
            chunk.obj_trigs+=[left,bottom,lz,right,top,lz,left,top,lz,right,top,lz,left,bottom,lz,right,bottom,lz]
            colors=self.contain.gcolors()
            collist=(colors[0]+colors[1]+colors[0])*2
            chunk.obj_colors+=collist
            chunk.obj_idcolors+=idcolor(self.item_id,self.serial,6)
    def use(self,tgtdata,worldx,worldy,usage):
        global changes
        if usage == "put":
            change_cell(worldx,worldy)
            target=objectbydata([(worldx,worldy),tgtdata[0],tgtdata[1]])
            if target.type == "ground" or target.type == "fireplace":
                self.zcoord=target.zcoord
            else:
                return
            for obj in changes[(worldx,worldy)].items():
                if obj[1].type == "fireplace":
                    if obj[1].zcoord == self.zcoord:
                        if self.contain.type != "nothing":
                            self.contain.heat()
            self.worldx=worldx
            self.worldy=worldy
            changes[(worldx,worldy)][self.item_id]=self
            player.losecurrent()
        elif usage == "pour":
            change_cell(worldx,worldy)
            target=objectbydata([(worldx,worldy),tgtdata[0],tgtdata[1]])
            if target.type == "jug":
                if self.contain.type != "nothing":
                    target.fill(self.contain)
                    self.contain=nothing_item
            elif target.type == "ground":
                self.contain=nothing_item
            else:
                return
        else:
            effect=self.contain.effect()
            effect.apply([player.x,player.y],player.item_id,player.serial)
            effects.append(effect)
    def interact(self):
        global player
        change_cell(self.worldx,self.worldy)
        if player.in_hand.type == "nothing":
            player.carry(self)
            delitem(self.worldx,self.worldy,self.item_id)

class creature_item(item):
    def __init__(self,worldx,worldy,zcoord):
        self.init("creature")
        self.worldx=worldx
        self.worldy=worldy
        self.zcoord=zcoord
        self.hasmoved=False
        self.movetimer=0.5*random.random()
        self.frontdir=int(random.random()*4)
        self.specie=int(((random.random()**6)*4+cell_creature(worldx,worldy))%4)
        self.leg=(getparamw("creature_leg"+str(self.specie),2) == 0)
        if self.leg:
            r=getparamw("creature_legc1_"+str(self.specie),256)/256.0
            g=r+0.2+getparamw("creature_legc2_"+str(self.specie),150)/256.0
            b=g+0.2+getparamw("creature_legc3_"+str(self.specie),150)/256.0
            g=g%1.0
            b=b%1.0
            self.legcol=[r,g,b,1.0]
        self.aggressive=(getparamw("creature_aggr"+str(self.specie),2) == 0)
        self.power=(getparamw("creature_power"+str(self.specie),10)/10.0)
    def changed(self):
        return True
    def randcol(self,name):
        r=getparamw("creature_%sc1_"%(name)+str(self.specie),256)/256.0
        g=r+0.2+getparamw("creature_%sc2_"%(name)+str(self.specie),150)/256.0
        b=g+0.2+getparamw("creature_%sc3_"%(name)+str(self.specie),150)/256.0
        g=g%1.0
        b=b%1.0
        return [r,g,b,1.0]
    def update(self,timestep):
        self.movetimer-=timestep
        playerdist=abs(self.worldx-player.x)+abs(self.worldy-player.y)
        if playerdist > G_CHUNK_SIZE:
            self.movetimer=0.5*random.random()
            return False
        if self.movetimer < 0:
            self.movetimer+=0.5
            dir=[(0,-1),(1,0),(0,1),(-1,0)][self.frontdir]
            newx=self.worldx+dir[0]
            newy=self.worldy+dir[1]
            if passable(newx,newy) and (not player.at(newx,newy)):
                change_cell(self.worldx,self.worldy)
                change_cell(newx,newy)
                changes[(newx,newy)][self.item_id]=self
                delitem(self.worldx,self.worldy,self.item_id)
                self.worldx=newx
                self.worldy=newy
                for obj in cell_object(newx,newy).values():
                    if obj.type == "ground":
                        self.zcoord=obj.zcoord
                        break
                self.set_frontdir(playerdist)
                return True
            elif player.at(newx,newy) and (player.visible) and self.aggressive:
                print "hit by",self,"from",self.worldx,self.worldy,newx,newy
                player.hit(self.power)
            else:
                self.frontdir=int(random.random()*4)
        return False
    def set_frontdir(self,playerdist):
        if playerdist > 10 or (not player.visible) or not self.aggressive:
             if random.random() < 0.25:
                self.frontdir=int(random.random()*4)
        else:
            if abs(self.worldx-player.x) > abs(self.worldy-player.y):
                if self.worldx < player.x:
                    self.frontdir=1
                else:
                    self.frontdir=3
            else:
                if self.worldy < player.y:
                    self.frontdir=2
                else:
                    self.frontdir=0
    def make(self,chunk,x,y):
        xs=[x+0.2,x+0.8,x+0.5,x+0.5]
        ys=[y+0.9,y+0.9,y+0.1,y+0.5]
        for i in range(self.frontdir):
            for pt in range(len(xs)):
                xs[pt],ys[pt]=1.0-(ys[pt]-y)+x,xs[pt]-x+y
        chunk.obj_trigs+=mesh_calc.closed_pyramid(xs[0],ys[0],xs[1],ys[1],xs[2],ys[2],xs[3],ys[3],self.zcoord+self.leg*1.0,self.zcoord+0.3+self.leg*1.0)
        lowcol=[0.6,0.6,0.6,1.0]
        hicol=[0.8,0.8,0.8,1.0]
        nosecol=self.randcol("nose")
        chunk.obj_colors+=lowcol+hicol+lowcol+lowcol+hicol+nosecol+nosecol+hicol+lowcol+lowcol+lowcol+hicol
        if self.leg:
            xs=[x+0.3,x+0.7,x+0.5,x+0.5]
            ys=[y+0.7,y+0.7,y+0.3,y+0.5]
            for i in range(self.frontdir):
                for pt in range(len(xs)):
                    xs[pt],ys[pt]=1.0-(ys[pt]-y)+x,xs[pt]-x+y
            chunk.obj_trigs+=mesh_calc.closed_pyramid(xs[0],ys[0],xs[1],ys[1],xs[2],ys[2],xs[3],ys[3],self.zcoord,self.zcoord+0.3+self.leg*1.0)
            chunk.obj_colors+=(self.legcol+[0.8,0.8,0.8,1.0]+self.legcol)*3+self.legcol*3
            chunk.obj_idcolors+=idcolor(self.item_id,self.serial,12)
        chunk.obj_idcolors+=idcolor(self.item_id,self.serial,12)
            

class player_item(item):
    def __init__(self):
        self.init("player")
        self.in_hand=nothing_item
        self.height=5.0
        self.hp=10.0
        self.z=-1.0
        self.visible=True
    def carry(self,thing):
        global ui_chunk
        self.in_hand=thing
        ui_chunk.updated=False
    def makecurrent(self):
        pass
    def losecurrent(self):
        global ui_chunk,ui_info
        self.in_hand=nothing_item
        ui_chunk.updated=False
        ui_info.menusel=0
        ui_info.menu=[]
    def hit(self,damage):
        self.hp-=damage
        if self.hp < 0:
            self.x=0
            self.y=0
            self.losecurrent()
            self.compute_z()
            self.hp=10.0
    def invisible(self,inv):
        self.visible=not inv
    def update(self,timestep):
        self.hp+=timestep/30.0
        if self.hp > 10:
            self.hp=10.0
    def at(self,x,y):
        return ((x-self.x < 1) and (y-self.y < 1)) and ((x-self.x > -2) and (y-self.y > -2))
    def compute_z(self):
        self.z=-1.0
        for xoff,yoff in [[0,0],[-1,0],[0,-1],[-1,-1]]:
            objs=cell_object(self.x+xoff,self.y+yoff)
            for o in objs.values():
                if o.type == "ground":
                    self.z=max(self.z,o.zcoord)

class text:
    def __init__(self):
        self.quads=[]
        self.coords=[]
        self.opacity=1.0

class building:
    def __init__(self,worldx,worldy,sizex,sizey):
        self.areatype="building"
        idstr=str(worldx)+"_"+str(worldy)
        self.worldx=worldx
        self.worldy=worldy
        self.sizex=sizex
        self.sizey=sizey
        doorside=getparamw("bld_doorside"+idstr,4)
        doorlock=(0 == getparamw("bld_doorlock"+idstr,2))
        self.floorz=max(1,cell_ground(worldx+sizex/2,worldy+sizey/2).zcoord)
        self.rooms=[[1,1,sizex-2,sizey-2]]   #format: x1,y1,x2,y2
        self.items=[]
        stripwidth=0
        if self.sizex > 18 and self.sizey > 18:
            self.doors=[]
            self.gen_rooms()
        else:
            if doorside%2 == 0:
                doorpos=2+getparamw("bld_doorpos"+idstr,sizex-6) #-6 for outside walls, no-trees strip & door size
                if doorside == 0:
                    self.doors=[[doorpos,1,doorlock,0]]
                else:
                    self.doors=[[doorpos,sizey-2,doorlock,2]]
            else:
                doorpos=2+getparamw("bld_doorpos"+idstr,sizey-6) #-6 for outside walls, no-trees strip & door size
                if doorside == 1:
                    self.doors=[[sizex-2,doorpos,doorlock,1]]
                else:
                    self.doors=[[1,doorpos,doorlock,3]]
        for r in self.rooms:
            roomidstr=idstr+"_"+str(r[0])+"_"+str(r[1])
            if (r[2]-r[0])*(r[3]-r[1]) > getparamw("room_item_"+roomidstr,10):
                ixoff=getparamw("item_xoff_"+roomidstr,r[2]-r[0]-1)
                iyoff=getparamw("item_yoff_"+roomidstr,r[3]-r[1]-1)
                self.items.append([r[0]+1+ixoff,r[1]+1+iyoff])
                print self.items
        print "rooms:",self.rooms,stripwidth
    def gen_rooms(self):
        idstr=str(self.worldx)+"_"+str(self.worldy)
        door_offsets=[(0,-1),(1,0),(0,1),(-1,0)] #offset by door direction
        xdiv=5+getparamw("building_xdiv_"+idstr,self.sizex-10) #5 for notree strip(1),external wall(1), possible door (3)
        ydiv=5+getparamw("building_ydiv_"+idstr,self.sizey-10)
        xcoords=[1,xdiv,self.sizex-2]
        ycoords=[1,ydiv,self.sizey-2]
        room_idxs=[]  #format: [isroom,is_accessible,doordirs]
        for x in range(2):  #initialize rooms matrix
            room_idxs.append([])
            for y in range(2):
                room_idxs[-1].append([False,False,[]])
        for i in range(2):  #select rooms
            posx=getparamw("building_roomposx"+str(i)+"_"+idstr,2)
            posy=getparamw("building_roomposy"+str(i)+"_"+idstr,2)
            room_idxs[posx][posy][0]=True
        regions=[]
        for x in range(2):  #divide to regions
            for y in range(2):
                if room_idxs[x][y][0]:
                    regions.append([[x,y]])
                else:
                    listed=False
                    for r in regions:
                        if [x,y] in r:
                            listed=True
                            break
                    if not listed:
                        regchg=True
                        newrgn=[[x,y]]
                        while regchg:
                            regchg=False
                            for room in newrgn:
                                for xoff,yoff in [[0,1],[0,-1],[1,0],[-1,0]]:
                                    x=max(0,room[0]+xoff)
                                    y=max(0,room[1]+yoff)
                                    if y > 1:
                                        y=1
                                    if x > 1:
                                        x=1
                                    if not room_idxs[x][y][0] and [x,y] not in newrgn:
                                        regchg=True
                                        newrgn.append([x,y])
                        regions.append(newrgn)
        print idstr
        print "\tregions:",regions
        accessible=False
        iteration=0
        while not accessible:
            print "\t\troom matrix (iteration %d):"%(iteration)
            for y in room_idxs:
                print "\t\t",y
            roomnum=getparamw("building_startroom"+str(iteration)+"_"+idstr,4)  #random search start position
            print roomnum
            accessible=True            
            for off in range(4):  #search for inaccessible room
                y=(roomnum+off)%2
                x=((roomnum+off-y)/2)%2
                print y,x
                if not room_idxs[x][y][1]:  #found
                    accessible=False
                    self.add_door(regions,room_idxs,x,y)
                    break
            for x in range(2): #recalculate accessibility
                for y in range(2):
                    if not room_idxs[x][y][1]:  #found inaccessible room
                        doors=room_idxs[x][y][2]
                        for d in doors:
                            destx=x+door_offsets[d][0]
                            desty=y+door_offsets[d][1]
                            if desty in [-1,2]:         #check for outside door
                                room_idxs[x][y][1]=True
                                break
                            elif destx in [-1,2]:
                                room_idxs[x][y][1]=True
                                break
                            else:     #check for neighbouring accessible room
                                if room_idxs[destx][desty][1]:
                                    room_idxs[x][y][1]=True
                                    break
            for r in regions:  #set whole region accessible if one room is
                reg_access=False
                for room in r:
                    if room_idxs[room[0]][room[1]][1]:
                        reg_access=True
                        break
                if reg_access:
                    for room in r:
                        room_idxs[room[0]][room[1]][1]=True
            iteration+=1
        print "\t\troom matrix (iteration %d):"%(iteration)
        for y in room_idxs:
                print "\t\t",y
        self.build_rooms(room_idxs,xcoords,ycoords)
        print self.rooms
        print self.doors
    def add_door(self,regions,rooms,roomx,roomy):
        door_offsets=[(0,-1),(1,0),(0,1),(-1,0)] #offset by door direction
        doorid=str(self.worldx)+"_"+str(self.worldy)+"_"+str(roomx)+"_"+str(roomy)
        directions=[0,1,2,3]
        doors=rooms[roomx][roomy][2]
        for d in doors:          #remove already-present directions to get possible directions list
            directions.remove(d)
        roomreg=[]
        for r in regions:       #find the region where the room is
            if [roomx,roomy] in r:
                roomreg=r
                break
        print roomx,roomy,"Reg:",roomreg
        while len(directions) > 0:
            dir=directions[getparamw("doordir_"+str(len(doors))+"_"+doorid,len(directions))]
            off=door_offsets[dir]
            iswall=([roomx+off[0],roomy+off[1]] not in roomreg)    #check that the room has wall in that direction
            if iswall:
                rooms[roomx][roomy][2].append(dir)
                return
            else:
                directions.remove(dir)
    def build_rooms(self,rooms,xs,ys):
        for roomx in range(2):
            for roomy in range(2):
                r=rooms[roomx][roomy]
                if r[0]:
                    self.rooms.append([xs[roomx],ys[roomy],xs[roomx+1],ys[roomy+1]])
                for d in r[2]:
                    if d in [0,2]:
                        if d == 0:
                            doory=ys[roomy]
                        else:
                            doory=ys[roomy+1]
                        doorid=str(self.worldx)+"_"+str(self.worldy)+"_"+str(roomx)+"_"+str(roomy)
                        doorx=xs[roomx]+1+getparamw("doorx"+doorid,xs[roomx+1]-xs[roomx]-4)
                        self.doors.append([doorx,doory,False,d])
                    else:
                        if d == 1:
                            doorx=xs[roomx+1]
                        else:
                            doorx=xs[roomx]
                        doorid=str(self.worldx)+"_"+str(self.worldy)+"_"+str(roomx)+"_"+str(roomy)
                        doory=ys[roomy]+1+getparamw("doory"+doorid,ys[roomy+1]-ys[roomy]-4)
                        self.doors.append([doorx,doory,False,d])
    def relcoords(self,worldx,worldy):
        if worldx < self.worldx or worldx >= self.worldx+self.sizex:
            return [-1,-1]
        if worldy < self.worldy or worldy >= self.worldy+self.sizey:
            return [-1,-1]
        return [worldx-self.worldx,worldy-self.worldy]
    def cell(self,relx,rely):
        retval=[ground_item(self.floorz*8.0)]
        if relx == 0 or relx == self.sizex-1:   #no trees strip
            return iddict(retval)
        if rely == 0 or rely == self.sizey-1:
            return iddict(retval)
        retval.append(block_item("stone",self.floorz+8))
        wallstart=8
        isdoor=False
        for r in self.rooms:
            if relx > r[2] or relx < r[0]:
                continue
            if rely > r[3] or rely < r[1]:
                continue
            if relx == r[0] or relx == r[2] or rely == r[1] or rely == r[3]:
                wallstart=0
        for d in self.doors:
            if relx == d[0] and rely == d[1]:
                retval.append(door_item(self.worldx+relx,self.worldy+rely,self.floorz,d[3],d[2]))
            if d[3]%2 == 0 and relx >= d[0] and relx < d[0]+3 and rely == d[1]:
                wallstart=6
            if d[3]%2 == 1 and rely >= d[1] and rely < d[1]+3 and relx == d[0]:
                wallstart=6
        for i in self.items:
            if relx == i[0] and rely == i[1]:
                print "floorz:",self.floorz
                item=jug_item(self.worldx+i[0],self.worldy+i[1],self.floorz)
                retval.append(item)
        if wallstart != 8:
            for i in range(wallstart,8):
                retval.append(block_item("stone",self.floorz+i))
        return iddict(retval)

class chunk_info:
    pass

class something:
    pass

class chunk:
    def __init__(self,worldx,worldy):
        self.gr_quads=[]
        self.gr_colors=[]
        self.gr_idcolors=[]
        self.obj_trigs=[]
        self.obj_colors=[]
        self.obj_idcolors=[]
        self.coord_gr_quads=[]
        self.coord_gr_colors=[]
        self.coord_gr_idcolors=[]
        self.coord_obj_trigs=[]
        self.coord_obj_colors=[]
        self.coord_obj_idcolors=[]
        self.worldx=worldx
        self.worldy=worldy
        self.arrfied=False
        self.updated=True
        self.update_coords=[]
    def reg_update(self,x,y):
        if [int(x),int(y)] not in self.update_coords:
            self.update_coords.append([int(x),int(y)])
            self.updated=False
    def update(self):
        if not self.updated:
            for x,y in self.update_coords:
                self.obj_trigs=[]
                self.obj_colors=[]
                self.obj_idcolors=[]
                objs=cell_object(self.worldx+x,self.worldy+y)
                for o in objs.values():
                    o.make(self,x,y)
                    g_objectpos[o.item_id]=(self.worldx+x,self.worldy+y)
                self.coord_obj_trigs[y*int(G_CHUNK_SIZE)+x]=self.obj_trigs
                self.coord_obj_colors[y*int(G_CHUNK_SIZE)+x]=self.obj_colors
                self.coord_obj_idcolors[y*int(G_CHUNK_SIZE)+x]=self.obj_idcolors
            self.update_coords=[]
            self.updated=True
            self.arrfied=False
    def writecell(self,x,y):  #transfers obj_* to coord_obj_*[y*chunk_size+x]
        while len(self.coord_obj_colors) < y*int(G_CHUNK_SIZE)+x+1:
            self.coord_obj_trigs.append([])
            self.coord_obj_colors.append([])
            self.coord_obj_idcolors.append([])
        self.coord_obj_trigs[y*int(G_CHUNK_SIZE)+x]=self.obj_trigs
        self.coord_obj_colors[y*int(G_CHUNK_SIZE)+x]=self.obj_colors
        self.coord_obj_idcolors[y*int(G_CHUNK_SIZE)+x]=self.obj_idcolors
    def render(self,xoff,yoff,zoff=0.0):
        if not self.arrfied:
            gr_quads=[]
            gr_colors=[]
            gr_idcolors=[]
            obj_trigs=[]
            obj_colors=[]
            obj_idcolors=[]
            for i in range(len(self.coord_obj_colors)):
                obj_trigs+=self.coord_obj_trigs[i]
                obj_colors+=self.coord_obj_colors[i]
                obj_idcolors+=self.coord_obj_idcolors[i]
            self.final_gr_quads=array(gr_quads,'f')
            self.final_gr_colors=array(gr_colors,'f')
            self.final_gr_idcolors=array(gr_idcolors,'f')
            self.final_obj_trigs=array(obj_trigs,'f')
            self.final_obj_colors=array(obj_colors,'f')
            self.final_obj_idcolors=array(obj_idcolors,'f')
            self.arrfied=True
        #start=time.clock()
        
        #print "setuniform:",time.clock()-start #,len(self.obj_trigs),"trig coords",len(self.gr_quads),"quad coords"
        #start=time.clock()
        #gl.glVertexAttribPointer(attrpos,3,gl.GL_FLOAT,gl.GL_TRUE,0,self.gr_quads)
        #gl.glVertexAttribPointer(attrcolor,4,gl.GL_FLOAT,gl.GL_TRUE,0,self.gr_colors)
        #gl.glVertexAttribPointer(attridcolor,3,gl.GL_FLOAT,gl.GL_TRUE,0,self.gr_idcolors)
        #print "setquads:",time.clock()-start
        #start=time.clock()
        #gl.glDrawArrays(gl.GL_QUADS,0,len(self.gr_quads)/3)
        #print "drawquads:",time.clock()-start
        #start=time.clock()
        gl.glUniform3f(offsetpos,xoff,yoff,zoff)
        gl.glVertexAttribPointer(attrpos,3,gl.GL_FLOAT,gl.GL_TRUE,0,self.final_obj_trigs)
        gl.glVertexAttribPointer(attrcolor,4,gl.GL_FLOAT,gl.GL_TRUE,0,self.final_obj_colors)
        gl.glVertexAttribPointer(attridcolor,3,gl.GL_FLOAT,gl.GL_TRUE,0,self.final_obj_idcolors)
        #end=time.clock()-start
        #print "settrigs:",end,
        #start=time.clock()
        gl.glDrawArrays(gl.GL_TRIANGLES,0,len(self.final_obj_trigs)/3)
        #end=time.clock()-start
        #print " render:",end,len(self.final_obj_trigs),"trig coords"

class bring_effect:
    def __init__(self,quality):
        self.range=quality
        self.basecolor=[0.0,0.0,0.0]
        self.basecolor[getparamw("bring_colidx",3)]+=1.0
        self.lifetime=1.0
    def update(self,timestep):
        self.lifetime-=timestep
    def alive(self):
        if self.lifetime > 0:
            return True
        return False
    def apply(self,pos,objid,serial):
        global changes
        self.targx=pos[0]
        self.targy=pos[1]
        if math.sqrt((player.x-self.targx)**2+(player.y-self.targy)**2) < self.range:
            change_cell(self.targx,self.targy)
            obj=find_obj(changes[(pos[0],pos[1])],objid,serial)
            if obj.type == "tree":
                if obj.fruit.type != "nothing":
                    obj.interact()
            elif obj.type == "fruit":
                obj.interact()
    def paint(self):
        global obj_trigs,obj_colors,obj_idcolors
        targdist=math.sqrt((player.x-self.targx)**2+(player.y-self.targy)**2)
        length=min(self.range,targdist)
        if self.lifetime > 0.5:
            color=self.basecolor+[0.7]
        else:
            color=self.basecolor+[0.25]
        if targdist != 0:
            lenratio=length/targdist
            tx,ty=xy2screen(self.targx,self.targy)
            obj_trigs+=[-0.25*ty/targdist,0.25*tx/targdist,-2.5,tx*lenratio,ty*lenratio,-2.5,0.25*ty/targdist,-0.25*tx/targdist,-2.5]
            obj_colors+=color*3
            obj_idcolors+=[0.0,0.0,0.0]*3

class explode_effect:
    def __init__(self,quality):
        self.range=quality
        self.lifetime=1.0
    def update(self,timestep):
        self.lifetime-=timestep
    def alive(self):
        if self.lifetime > 0:
            return True
        return False
    def apply(self,pos,objid,serial):
        global changes
        self.targx=pos[0]
        self.targy=pos[1]
        if math.sqrt((player.x-self.targx)**2+(player.y-self.targy)**2) < self.range:
            change_cell(self.targx,self.targy)
            obj=find_obj(changes[(pos[0],pos[1])],objid,serial)
            if obj.type == "tree":
                print "tree found"
                idstr=str(obj.item_id)
                delitem(pos[0],pos[1],obj.item_id)
                for i in range(4):
                    xoff=getparamw("stick_xoff"+idstr+"_"+str(i),5)-2
                    yoff=getparamw("stick_yoff"+idstr+"_"+str(i),5)-2
                    ground=cell_ground(pos[0]+xoff,pos[1]+yoff)
                    if ground.kind != "water":
                        change_cell(pos[0]+xoff,pos[1]+yoff)
                        st=stick_item(pos[0]+xoff,pos[1]+yoff,ground.zcoord)
                        st.serial=len(changes[(pos[0]+xoff,pos[1]+yoff)])
                        changes[(pos[0]+xoff,pos[1]+yoff)][st.item_id]=st
    def paint(self):
        global obj_trigs,obj_colors,obj_idcolors
        targdist=math.sqrt((player.x-self.targx)**2+(player.y-self.targy)**2)
        length=min(self.range,targdist)
        if self.lifetime > 0.5:
            alpha=0.7
        else:
            alpha=0.25
        color1=[0.0,1.0,0.4,alpha]
        color2=[0.5,0.9,0.7,alpha]
        if targdist != 0:
            lenratio=length/targdist
            tx,ty=xy2screen(self.targx,self.targy)
            obj_trigs+=[-0.25*ty/targdist,0.25*tx/targdist,-2.5,tx*lenratio,ty*lenratio,-2.5,0.25*ty/targdist,-0.25*tx/targdist,-2.5]
            obj_colors+=color1+color2+color1
            obj_idcolors+=[0.0,0.0,0.0]*3

class fire_effect:
    def __init__(self,quality):
        self.range=100.0 #quality
        self.lifetime=1.0
    def update(self,timestep):
        self.lifetime-=timestep
    def alive(self):
        if self.lifetime > 0:
            return True
        return False
    def apply(self,pos,objid,serial):
        global changes
        self.targx=pos[0]
        self.targy=pos[1]
        print "applying effect at",pos[0],pos[1]
        if math.sqrt((player.x-self.targx)**2+(player.y-self.targy)**2) < self.range:
            change_cell(pos[0],pos[1])
            targobj=find_obj(changes[(pos[0],pos[1])],objid,serial)
            if targobj.type == "tree":
                print "tree found"
                targobj.fruit=nothing_item
                targobj.burntime=5
                targobj.fire=1
                return
            fireplace_size=0
            for obj in changes[(pos[0],pos[1])].values():
                if targobj.type == "stick" and obj.type == "stick" and obj.zcoord == targobj.zcoord:
                    fireplace_size+=1
            if fireplace_size > 0:
                fireplace=fireplace_item(pos[0],pos[1],targobj.zcoord,fireplace_size)
                changes[(pos[0],pos[1])][fireplace.item_id]=fireplace
            for obj in changes[(pos[0],pos[1])].values():
                if targobj.type == "stick" and obj.type == "stick" and obj.zcoord == targobj.zcoord:
                    delitem(pos[0],pos[1],obj.item_id)
    def paint(self):
        global obj_trigs,obj_colors,obj_idcolors
        targdist=math.sqrt((player.x-self.targx)**2+(player.y-self.targy)**2)
        length=min(self.range,targdist)
        if self.lifetime > 0.5:
            color=[0.7,0.7,0.0,0.7]
        else:
            color=[0.7,0.7,0.0,0.25]
        if targdist != 0:
            lenratio=length/targdist
            tx,ty=xy2screen(self.targx+1,self.targy+1)
            obj_trigs+=[-0.25*ty/targdist,0.25*tx/targdist,-2.5,tx*lenratio,ty*lenratio,-2.5,0.25*ty/targdist,-0.25*tx/targdist,-2.5]
            obj_colors+=color*3
            obj_idcolors+=[0.0,0.0,0.0]*3

class invisible_effect:
    def __init__(self,quality):
        self.quality=quality
        self.lifetime=1000000000
    def update(self,timestep):
        self.lifetime-=timestep
        if self.lifetime < 0.0 and self.target.type != "nothing":
            player.invisible(False)
    def alive(self):
        return (self.lifetime > 0)
    def apply(self,pos,objid,serial):
        if objid == player.item_id:
            self.target=player
            self.lifetime=self.quality
            player.invisible(True)
        else:
            self.target=nothing_item
    def paint(self):
        pass

class resize_effect:
    def __init__(self,quality,dir):
        self.sizefactor=float(quality)/25
        self.dir=dir
    def update(self,timestep):
        self.lifetime-=timestep
        if self.lifetime > 0.0 and self.target.type != "nothing":
            cur_height=round(self.lifetime*self.height1+(1-self.lifetime)*self.height2,0)
            self.target.height=cur_height
    def alive(self):
        return (self.lifetime > 0)
    def apply(self,pos,objid,serial):
        #change_cell(pos[0],pos[1])
        #obj=find_obj(changes[(pos[0],pos[1])],objid,serial)
        if objid == player.item_id:
            self.lifetime=1.0
            self.target=player
            self.height1=player.height
            if self.dir == 1:
                maxheight=round(5.0*(1+self.sizefactor),0)
                if player.height >= maxheight:
                    self.height2=player.height
                else:
                    self.height2=min(player.height*(1+self.sizefactor),maxheight)
            else:
                minheight=round(5.0/(1+self.sizefactor),0)
                if player.height <= minheight:
                    self.height2=player.height
                else:
                    self.height2=max(player.height/(1+self.sizefactor),minheight)
        else:
            self.target=nothing_item
    def paint(self):
        pass

class unlock_effect:
    def __init__(self,quality):
        self.power=quality
        contrast=getparamw("unlock_contrast",200)
        if contrast > 100:
            contrast+=55
        contrast/=256.0
        self.outcolor=[contrast,contrast,contrast]
        self.incolor=[1.0-contrast,1.0-contrast,1.0-contrast]
        self.lifetime=1.0
    def update(self,timestep):
        self.lifetime-=timestep
    def alive(self):
        if self.lifetime > 0:
            return True
        return False
    def apply(self,pos,objid,serial):
        global changes
        self.targx=pos[0]
        self.targy=pos[1]
        #if math.sqrt((player.x-self.targx)**2+(player.y-self.targy)**2) < self.range:
        change_cell(self.targx,self.targy)
        obj=find_obj(changes[(pos[0],pos[1])],objid,serial)
        if obj.type == "door":
            if obj.haslock & obj.locked:
                obj.locked=False
    def paint(self):
        global obj_trigs,obj_colors,obj_idcolors
        targdist=math.sqrt((player.x-self.targx)**2+(player.y-self.targy)**2)
        length=targdist
        if self.lifetime > 0.5:
            alpha=0.7
        else:
            alpha=0.25
        if targdist != 0:
            lenratio=length/targdist
            tx,ty=xy2screen(self.targx,self.targy)
            print "unlock target:",tx,ty
            obj_trigs+=[-0.3*ty/targdist,0.3*tx/targdist,-2.5,tx*lenratio,ty*lenratio,
                        -2.5,0.3*ty/targdist,-0.3*tx/targdist,-2.5]
            obj_trigs+=[-0.15*ty/targdist,0.2*tx/targdist,-2.45,tx*lenratio,ty*lenratio,
                        -2.45,0.2*ty/targdist,-0.15*tx/targdist,-2.45]
            obj_colors+=(self.outcolor+[alpha])*3
            obj_colors+=(self.incolor+[alpha])*3
            obj_idcolors+=[0.0,0.0,0.0]*6

#
# SPELLWORDS & EFFECTS GENERATION
#

cons=['s','d','f','r','c','v','z']
vows=['a','e','w']
out_vows=['i','o','u']
all_vows=['a','e','i','o','u']
def syll_by_code(code,state):
    syll_ends=["","","","k","l","p","m","n"]
    w_vows=["ou","io","ei"]
    if len(code) == 1:
        code+=out_vows[state%len(out_vows)]
    if code[1] == 'w':
        code=code[0]+w_vows[state%len(w_vows)]
    return (code+syll_ends[state%len(syll_ends)]).capitalize()

def spell_word(spell):
    vow_sufxs={'o':["","m"],
           'u':["m","s"],
           'e':["m","s"],
           'i':["","s"],
           'a':["","s","m"]}
    sufxs=["us","um","o","is"]
    retval=""
    syllcode=""
    syllarr=[]
    curstate=getparam(parse_spell(spell)[0],1000)
    for c in spell:
        if c in cons:
            curstate=getparam(hex(curstate)+c,1000)
            if syllcode != "":
                retval+=syll_by_code(syllcode,curstate)
            syllcode=c
        elif c in vows:
            syllcode+=c
    curstate=getparam(hex(curstate),1000)
    if syllcode != "":
        retval+=syll_by_code(syllcode,curstate)
    curstate=getparam(hex(curstate),1000)
    if retval[-1] in all_vows:
        retval+=vow_sufxs[retval[-1]][curstate%len(vow_sufxs[retval[-1]])]
    else:
        retval+=sufxs[curstate%len(sufxs)]
    return retval

def parse_spell(spell):
    retval=["",""]
    syllfull=False
    insyll=False
    for c in spell:
        if c in cons:
            if insyll and (not syllfull):
                retval[1]+='o'
            retval[0]+=c
            insyll=True
            syllfull=False
        elif c in vows:
            retval[1]+=c
            syllfull=True
    if insyll and (not syllfull):
        retval[1]+='o'
    return retval

def is_magical(skeleton):
    rand=getparamw(skeleton,pow(g_difficulty,len(skeleton)))
    return rand == 1

def spell_effect(skeleton,spell):
    effects=[['light'],['set something on fire','detect life'],['blow up something','make something disappear'],['kill something','destroy something']]
    rand=getparamw(skeleton+"_level",8)
    if rand < 4:
        level=0
    elif rand < 6:
        level=1
    elif rand < 7:
        level=2
    else:
        level=3
    rand=getparamw(skeleton+"_effect",7)
    quality=100.0 #round(100.0/float((getparamw(spell+"_quality",100)+1)**2),2)
    #return effects[level][rand%len(effects[level])]
    #return fire_effect(quality) ###CHEAT
    if rand < 4:
        print "CHEAT"
        return fire_effect(quality)
    else:
        print "CHEAT"
        return explode_effect(quality)
    if rand < 1:
        print "CHEAT: fire"
        return fire_effect(quality)
    elif rand < 3:
        print "CHEAT: unlock"
        return unlock_effect(quality)
    else:
        print "CHEAT: bring"
        return bring_effect(quality)

#
# SOUND GENERATION
#


def clamp(val,range):
    if val < (-range):
        return -range
    elif val > range:
        return range
    return val

def sign(val):
    if val < 0:
        return -1
    elif val > 0:
        return 1
    return 0

def add_ending(orig_notes):
    newnotes=[]
    for n in orig_notes:
        newnotes.append(n[:])
    notes=newnotes
    notes.append([notes[-1][0],2,0.5,notes[-1][3],2])
    return notes

def modify_notes(orig_notes,seed):
    newnotes=[]
    for n in orig_notes:
        newnotes.append(n[:])
    notes=newnotes
    i=-1
    while notes[i][0] in ['O','C','G']:
        i-=1
    cidx1=i
    i-=1
    while notes[i][0] in ['O','C','G']:
        i-=1
    cidx2=i
    if getparamw("music_chg"+str(seed),3) == 0:
        shift=2
    else:
        shift=1
    if getparamw("music_chg_sgn"+str(seed),2) == 0:
        shift=-shift
    if notes[cidx1][0]+shift not in range(4,30):
        shift=-shift
    if notes[cidx1][0] != -1:
        notes[cidx1][0]+=shift
    if getparamw("music_chg_len"+str(seed),2) == 0:
        #print notes[-5:]
        if notes[cidx2][0]+shift in range(4,30):
            if notes[cidx2][0] != -1:
                notes[cidx2][0]+=shift
    return notes

def noteslen(notes,beat):
    retval=0
    for n in notes:
        retval+=n[1]/beat
    return retval

def gen_frag(size,seed,styleseed):
    ishalf=False
    if getparamw("music_inithalf"+str(seed),3) == 0:
        ishalf=True
    cnote=12+getparamw("music_stnote"+str(seed),8)
    beatrand=getparamw("music_beat"+str(styleseed),10)
    if beatrand < 3:
        beat=0.75  #30% fast
    elif beatrand < 6:
        beat=1.0   #30% medium
    elif beatrand < 8:
        beat=1.5  #20% slow
    else:
        beat=2.0  #20% vslow
    print "BEAT:",beat
    #beat=0.75+0.25*getparamw("music_beat"+str(styleseed),3)
    if beat == 0.75:
        instrument=2
    else:
        instrument=2*getparamw("music_inst"+str(styleseed),2)
        cnote-=int(4*beat-2)
    beat*=0.75   #Improves Music Dramatically
    #else:
    #    instrument=0
    #    cnote-=4
    instrument+=getparamw("music_instmetal"+str(styleseed),2)
    if instrument == 3:
        print "metallic"
    if instrument > 1:
        echo=beat*getparamw("music_echo"+str(styleseed),2)
    else:
        echo=0
    accord_added=(echo == 0)
    trend=getparamw("music_inittrend"+str(seed),2)*2-1
    noteid=seed*1000
    notes=[]
    fraglen=0
    succ_repeats=0
    trend_changed=False
    silence_added=(getparamw("add_silence_"+str(styleseed),2) == 0)
    split31=((not silence_added) and (beat > 0.7))   #have exactly 1 special (silence/split) per fragment
    mid_split=False               #beat AFTER mult by 0.75!
    issplit=(split31 and (getparamw("music_initsplit"+str(seed),3) == 0) and (not ishalf))
    target_len=size/beat-(size/beat)%4
    while fraglen < target_len or mid_split:
        too_same=False
        sameness=0
        halflen=False
        if len(notes) > 2:
            if notes[-1][0] == notes[-2][0] and notes[-2][0] == notes[-3][0]:
                too_same=True
        if len(notes) > 3:
            if too_same and notes[-3][0] == notes[-4][0]:
                sameness=1
        if len(notes) > 4:
            if sameness == 1 and notes[-4][0] == notes[-5][0]:
                sameness=2
        if abs(fraglen-int(fraglen+0.1)) > 0.3:
            halflen=True
        if (getparamw("music_repeat"+str(noteid),3) > 0) or (target_len-fraglen < 4) or (len(notes) < 4) or too_same or halflen or succ_repeats > 1 or mid_split: #note change
            succ_repeats=0
            rand=getparamw("music"+str(noteid),50)+10
            if rand < 20-sameness*10:
                change=0
            elif rand < 34:
                change=1
            elif rand < 48:
                change=2
            elif rand < 50:
                change=3
            else:
                change=4
            cnote+=change*trend
            if cnote < 4:
                cnote=4
                trend=-trend
            if cnote > 30:
                cnote=30
                trend=-trend
            if ishalf:
                notelen=0.5
            else:
                notelen=1.0
            #if False and (echo > 0 or accord_added) and getparamw("music_silence"+str(noteid),6) == 0 and getparamw("music_silence"+str(noteid-1),6) != 0:
            #    notes.append([-1,notelen*beat,1.0,instrument,notelen*beat+echo])
            #else:
            if issplit:
                if (not mid_split):
                    notelen=notelen*3.0/4
                    mid_split=True
                else:
                    notelen=notelen/4.0
                    mid_split=False
            notes.append([cnote,notelen*beat,1.0,instrument,notelen*beat+echo])
            fraglen+=notelen
        else:     #2 or 4 note repeat
            if getparamw("music_repsize"+str(seed),2) == 0 or fraglen < 4:
                replen=2
            else:
                replen=4
            if ishalf:
                replen/=2
            succ_repeats+=1
            i=1
            #print notes,replen
            while noteslen(notes[-i:],beat) < replen:
                print i,noteslen(notes[-i:],beat)
                i+=1
            fraglen+=noteslen(notes[-i:],beat)
            if ishalf:
                notelen=0.5
            else:
                notelen=1.0
            if silence_added and getparamw("music_silence"+str(noteid),3) == 0 and (fraglen+2*notelen <= target_len): #after-repeat silence
                notes+=[[-1,notelen*beat,1.0,instrument,notelen*beat+echo]]+notes[-i:]+[[-1,notelen*beat,1.0,instrument,notelen*beat+echo]]
                fraglen+=notelen*2
                succ_repeats+=1  #dont repeat on silence!
            else:
                notes+=notes[-i:]
        if abs(fraglen-int(fraglen)) < 0.01:   #integer len
            if getparamw("musich"+str(noteid),3) == 0:   #change half status
                print "chlen:",fraglen
                ishalf=(not ishalf)
            if split31 and (getparamw("musicsplit"+str(noteid),3) == 0):   #change half status
                print "split chlen:",fraglen
                issplit=(not issplit)
            if ishalf and issplit:
                issplit=False
        trend_changed=False
        if getparamw("musictr"+str(noteid),4) == 0:   #change trend
            trend=-trend
            trend_changed=True
        noteid+=1
    print "fraglen:",fraglen,"of:",size
    fraglen=0.0
    newnotes=[]
    accid=getparamw("music_accseed_"+str(seed),1000000)
    if instrument < 2:    #non-xylophonic
        acctype=0
    else:
        acctype=getparamw("music_acctype_"+str(styleseed),2) #0-regular accords 1-long accords
    acckind=notes[0][0]
    accshift=accid%7
    if acctype == 1:
        accspacing=8
    else:
        accspacing=4+4*(getparamw("music_accspace_"+str(styleseed),3) != 0)
    for n in notes:
        if accord_added and echo < 0.01:
            if abs(fraglen%accspacing) < 0.05 and (target_len-fraglen > 2): #drums-bkgnd in front of note
                if n[0]%7 in [0,2,4,6]:
                    newnotes+=[['C',4*beat+acctype*4*beat,acckind*acctype,accshift]]
                else:
                    newnotes+=[['G',4*beat+acctype*4*beat,acckind*acctype,accshift]]
        accid+=1
        accshift+=getparamw("music_accprog"+str(accid),2)*4-2
        if accshift < 0:
            accshift+=2
        if accshift > 7:
            accshift-=2
        fraglen+=n[1]/beat
        newnotes.append(n)
    return newnotes

def gen_notes():
    retval=[]
    seed=0
    for i in [18,4,5,28]:  #TrXyStFl
    #for i in range(2):
        #while True:
        A=gen_frag(8+4*getparamw("music_fraglen1_"+str(i),3),i*2,500+i)
        #    lastidx=-1
        #    while A[lastidx][0] in ['O','C','G']:
        #        lastidx-=1
        #    firstidx=0
        #    while A[firstidx][0] in ['O','C','G']:
        #        firstidx+=1
        #    if getparamw("music_eqnotes1",2) == 3 or (A[firstidx][0] != A[lastidx][0]): # and A[firstidx][1] == A[lastidx][1]):
        #        break
        #    seed+=1000
        A=modify_notes(A*2,10000+i*2000)
        B=gen_frag(8+4*getparamw("music_fraglen2_"+str(i),3),i*2+1,500+i)
        B=modify_notes(B*2,11000+i*2000)
        retval+=add_ending(A+B)+[[-1,4,1.0,0,4]]
    #C=gen_frag(2)
    #C=modify_notes(C,7000)
    return retval
        

def sawtooth(x):
    x-=math.pi*2*int(x/(math.pi*2))
    if x < math.pi/2:
        return x/(math.pi/2)
    if x < 1.5*math.pi:
        return 2.0-x/(math.pi/2)
    return -4.0+x/(math.pi/2)

bkg_tempo=[]
for i in range(11):
    bkg_tempo.append(getparamw("music_drum"+str(i),2))

acc_lookup=[]
for i in range(1000):
    pos=math.pi/500*i
    val=0
    #harmonics=[0,-1.9,-6.4,-2.8,-5.1,-8.7,-9,-13.1]
    harmonics=[-3,0,0,0,-3,-6,-9,-12]
    for h in range(len(harmonics)):
        val+=pow(10,harmonics[h]/10.0)*math.sin(pos*h)
    acc_lookup.append(val)

def bkg_synth(type,sample,length,kind,shift=0):
    retval=0
    if kind == 0:
        pos=float(sample*8)/length
    else:
        pos=float(sample)/length
    phase=sample/32000.0*math.pi*2*2
    cycle=int(pos)
    if kind == 0:
        if type == 'C':
            retval+=math.sin(phase*freqs[4]) #G2
            retval+=math.sin(phase*freqs[7]) #C3
            if bkg_tempo[cycle] == 0:
                retval+=math.sin(phase*freqs[9]) #E3
            else:
                retval+=math.sin(phase*freqs[11]) #G3
        if type == 'G':
            retval+=math.sin(phase*freqs[4]) #G2
            retval+=math.sin(phase*freqs[6]) #B2
            if bkg_tempo[cycle] == 0:
                retval+=math.sin(phase*freqs[8]) #D3
            else:
                retval+=math.sin(phase*freqs[10]) #F3
    else:
        kind=kind%7+2
        d=0.7
        retval+=acc_lookup[int(((phase*freqs[kind+shift])%(math.pi*2))/math.pi*500)]
        retval+=acc_lookup[int(((phase*freqs[kind+shift+2])%(math.pi*2))/math.pi*500)]
        retval+=acc_lookup[int(((phase*freqs[kind+shift+4])%(math.pi*2))/math.pi*500)]
        retval*=0.2
    amp=pos-int(pos)
    ###possibly should decomment
    #if kind == 0:
    amp=min(amp*8,1.0)-amp*amp
    #else:
    #    amp=2*(amp-pow(amp,3))
    return int(1000*amp*retval)

notefreq=261.626/4.0
freqs=[]
for n in range(5):
    freqs.append(notefreq)
    notefreq*=1.059463094359295
    notefreq*=1.059463094359295
    freqs.append(notefreq)
    notefreq*=1.059463094359295
    notefreq*=1.059463094359295
    freqs.append(notefreq)
    notefreq*=1.059463094359295
    freqs.append(notefreq)
    notefreq*=1.059463094359295
    notefreq*=1.059463094359295
    freqs.append(notefreq)
    notefreq*=1.059463094359295
    notefreq*=1.059463094359295
    freqs.append(notefreq)
    notefreq*=1.059463094359295
    notefreq*=1.059463094359295
    freqs.append(notefreq)
    notefreq*=1.059463094359295

def gen_sound():
    snd=open("bkgnd.wav","wb")
    notes=gen_notes()
    spb=16000  #samples per beat
    totbeats=0
    for n in notes:
        if n[0] not in ['C','G']:
            totbeats+=n[1]
    datasize=int(totbeats*spb*2) #32KHz sampling *2bytes/sample. beat 120
    snd.write("RIFF")
    snd.write(binfiles.dwordn(36+datasize))
    snd.write("WAVE")
    snd.write("fmt ")
    snd.write(binfiles.dwordn(16))
    snd.write(binfiles.wordn(1))      #compression (1=PCM)
    snd.write(binfiles.wordn(1))      #channels
    snd.write(binfiles.dwordn(32000)) #sample rate
    snd.write(binfiles.dwordn(64000)) #byte rate
    snd.write(binfiles.wordn(2))      #bytes per sample * channels
    snd.write(binfiles.wordn(16))      #bits per sample
    snd.write("data")
    snd.write(binfiles.dwordn(datasize))
    rng=range(65536)
    curacc='O'
    curacctype=0
    accsample=0
    acclen=0
    filedata=[-1]*int((totbeats+10)*spb*2)
    fileacc=[-1]*int((totbeats+10)*spb*2)
    sampleidx=0
    randoms=[]
    for i in range(50000):
        randoms.append(random.random()*2-1)
    for n in notes:
        sounddata=[]
        print n[0],
        if n[0] in ['C','G','O']:
            curacc=n[0]
            curacctype=n[2]
            accsample=0
            acclen=n[1]*spb
            if n[2] != 0:
                    acclen*=1.3
            for s in range(int(acclen)):
                fileacc[sampleidx+s]+=bkg_synth(curacc,s,acclen,curacctype,n[3])
        else:
            if n[0] != -1:
                freq=freqs[n[0]]
            else:
                print
                freq=0.0
            beats=n[4]  #n[1]
            print n[1],"("+str(beats)+")",
            samples=int(beats*spb)
            starttime=time.clock()
            phase=0
            for s in range(samples):
                amp=float(s)/samples
                if n[3] == 2 or n[3] == 3:
                    amp=1.4*(min(math.sqrt(amp)*8,1.0)-pow(amp,0.15))
                elif n[3] == 0:  #flute
                    amp=2.35*(amp-pow(amp,3))
                else:  #"trumpet"
                    amp=min(amp*6,1.0,6-amp*6)
                amp*=n[2]#*(1+0.4*math.sin(float(s)/32000*math.pi*2*4))
                if n[3] == 1:
                    phase+=1.0/32000*freq*math.pi*2*(1+0.005*math.sin(float(s)/32000*math.pi*2*4))
                else:
                    phase+=1.0/32000*freq*math.pi*2
                data=0
                if n[3] == 2:
                    data=int(8000*amp*math.sin(phase)+   #"celesta"=vibes/metallic-xylophone
                                8000*amp*math.sin(phase*3)/4+
                                8000*amp*math.sin(phase*14)/32)
                elif n[3] == 0:
                    data=int(10000*amp*(math.sin(phase)))  #flute
                elif n[3] == 1:
                    data=int(1000*amp*(math.sin(phase)+  #"trumpet"
                                    math.sin(phase*2)*4+
                                    math.sin(phase*3)+
                                    math.sin(phase*4)+
                                    math.sin(phase*5)*1.5+
                                    math.sin(phase*6)/8+
                                    math.sin(phase*8)/8+
                                    math.sin(phase*10)/32))
                else:
                    data=int(1000*amp*(math.sin(phase)+  #sitar
                                    math.sin(phase*2)/2+
                                    math.sin(phase*3)*6+
                                    math.sin(phase*4)+
                                    math.sin(phase*5)*2+
                                    math.sin(phase*6)/4+
                                    math.sin(phase*8)/2+
                                    math.sin(phase*9)/4+
                                    math.sin(phase*10)+
                                    math.sin(phase*11)/8))
                #data+=int(1000*amp*(random.random()*2-1)) #no-correlation noise
                #rand1=randoms[int(phase/(math.pi/2))]    #noise with specified freq
                #rand2=randoms[int(phase/(math.pi/2))+1]
                #interpos=(phase%(math.pi/2))/(math.pi/4)
                #interpos=interpos-1
                #interpos=0#(math.sin(interpos*math.pi/2)+1)/2
                #data+=(rand1*(1-interpos)+rand2*interpos)*amp*1000
                if data < 0:
                    data+=65536
                filedata[sampleidx]+=data
                if filedata[sampleidx] < 0:
                    filedata[sampleidx]+=65536
                sampleidx+=1
            sampleidx-=int((n[4]-n[1])*spb)
    for i in range(len(filedata)):
        snd.write(binfiles.wordn(int(filedata[i]+fileacc[i])))

#gen_sound()
#winsound.PlaySound("bkgnd.wav",winsound.SND_FILENAME+winsound.SND_LOOP+winsound.SND_ASYNC)

def gen_sky():
    sk=chunk(0,0)
    phi=(math.sqrt(5)+1)/2
    sk.coord_obj_trigs=[mesh_calc.open_pyramid4(-1000.0,-1000.0,1000.0,1000.0,0.0,0.0,0.0,1410)]
    sk.coord_obj_trigs[0]+=mesh_calc.open_pyramid4(-1000.0,-1000.0,1000.0,1000.0,0.0,0.0,0.0,-1410)
    sk.coord_obj_colors=[[]]
    colors=[]
    for i in range(12):
        colors.append([getparamw("ski_c1_"+str(i),256)/256.0,getparamw("sky_c2_"+str(i),256)/256.0,getparamw("sky_c3_"+str(i),256)/256.0,1.0])
    ##set color[0]-=5 to disable sky fading
    for i in range(24):
        vertex=sk.coord_obj_trigs[0][i*3:i*3+3]
        vx=0
        if vertex[0] > 0:
            vx=1
        vy=0
        if vertex[1] > 0:
            vy=1
        vz=0
        if vertex[2] == 0.0:
            vz=1
        elif vertex[2] > 0.0:
            vz=2
        sk.coord_obj_colors[0]+=colors[vx+2*vy+4*vz]
    sk.coord_obj_idcolors=[[0.0,0.0,0.0]*24]
    sk.writecell(1,1)
    sk.updated=True
    return sk

def nonoverlap_rand(areax,areay,type,areasize,prob):
    if getparamw(str(areax)+"_"+str(areay)+"_"+type,prob) != 0:
        return False
    if getparamw(str(areax-areasize)+"_"+str(areay)+"_"+type,prob) == 0:
        return False
    if getparamw(str(areax)+"_"+str(areay-areasize)+"_"+type,prob) == 0:
        return False
    if getparamw(str(areax-areasize)+"_"+str(areay-areasize)+"_"+type,prob) == 0:
        return False
    if getparamw(str(areax-areasize)+"_"+str(areay+areasize)+"_"+type,prob) == 0:
        return False
    if type == "hut":
        print "has hut in",areax,areay,areasize
    return True

nonoverlapobj_cahce={}
def nonoverlap_obj(worldx,worldy,type,areasize,prob):
    areax=worldx-worldx%areasize
    areay=worldy-worldy%areasize
    if (areax,areay,type) in nonoverlapobj_cahce:
        return nonoverlapobj_cahce[(areax,areay,type)]
    retval=[(areax,areay,type)]
    if nonoverlap_rand(areax,areay,type,areasize,prob):
        retval=[(areax,areay,type),0,0]
    elif nonoverlap_rand(areax-areasize,areay,type,areasize,prob):
        retval=[(areax,areay,type),-areasize,0]
    elif nonoverlap_rand(areax,areay-areasize,type,areasize,prob):
        retval=[(areax,areay,type),0,-areasize]
    elif nonoverlap_rand(areax-areasize,areay-areasize,type,areasize,prob):
        retval=[(areax,areay,type),-areasize,-areasize]
    nonoverlapobj_cahce[(areax,areay,type)]=retval
    return retval

ground_type=suitability()
nothing_area=something()
nothing_area.areatype="nothing"
areaobject_cache={}
misses=0
def area_object(worldx,worldy):
    global misses
    big_obj=nonoverlap_obj(worldx,worldy,"big_building",40,6)  #originally prob=15
    hut_obj=nonoverlap_obj(worldx,worldy,"hut",20,25)
    if hut_obj[0] in areaobject_cache and len(hut_obj) > 1:
        if areaobject_cache[hut_obj[0]].areatype != "nothing":
            return [areaobject_cache[hut_obj[0]],hut_obj[1],hut_obj[2]]
    if big_obj[0] in areaobject_cache and len(big_obj) > 1:
        if areaobject_cache[big_obj[0]].areatype != "nothing":
            return [areaobject_cache[big_obj[0]],big_obj[1],big_obj[2]]
    if big_obj[0] not in areaobject_cache and len(big_obj) > 1:
        misses+=1
        print "big miss"
        objstr=str(big_obj[0][0]+big_obj[1])+"_"+str(big_obj[0][1]+big_obj[2])
        bldx=big_obj[0][0]+big_obj[1]+getparamw(objstr+"_buildingx",40)
        bldy=big_obj[0][1]+big_obj[2]+getparamw(objstr+"_buildingy",40)
        bldxsize=18+getparamw(objstr+"_buildingxsz1",10)+getparamw(objstr+"_buildingxsz2",10)
        bldysize=18+getparamw(objstr+"_buildingysz1",10)+getparamw(objstr+"_buildingysz2",10)
        if ground_type.bigbuilding(bldx,bldy,bldxsize,bldysize):
            retval=building(bldx,bldy,bldxsize,bldysize)
        else:
            retval=nothing_area
        if retval.areatype != "nothing":
            areaobject_cache[big_obj[0]]=retval
            return [retval,big_obj[1],big_obj[2]]
    if hut_obj[0] not in areaobject_cache and len(hut_obj) > 1:
        misses+=1
        print "small miss"
        objstr=str(hut_obj[0][0]+hut_obj[1])+"_"+str(hut_obj[0][1]+hut_obj[2])
        bldx=hut_obj[0][0]+hut_obj[1]+getparamw(objstr+"_buildingx",16)
        bldy=hut_obj[0][1]+hut_obj[2]+getparamw(objstr+"_buildingy",16)
        bldxsize=7+getparamw(objstr+"_buildingxsz1",4)+getparamw(objstr+"_buildingxsz2",4)
        bldysize=7+getparamw(objstr+"_buildingysz1",4)+getparamw(objstr+"_buildingysz2",4)
        retval=building(bldx,bldy,bldxsize,bldysize)
        areaobject_cache[hut_obj[0]]=retval
        return [retval,hut_obj[1],hut_obj[2]]
    if hut_obj[0] in areaobject_cache and len(hut_obj) > 1:
        return [areaobject_cache[hut_obj[0]],hut_obj[1],hut_obj[2]]
    areaobject_cache[hut_obj[0]]=nothing_area
    areaobject_cache[big_obj[0]]=nothing_area
    return [nothing_area,0,0]

def iddict(items):
    retval={}
    serial=1
    for i in items:
        i.serial=serial
        retval[i.item_id]=i
        serial+=1
    return retval
            
nothing_item=item("nothing")
def cell_object(worldx,worldy,post_reg=False,check_changes=True):
    global changes
    if check_changes and (worldx,worldy) in changes:
        return changes[(worldx,worldy)]
    #ground=ground_item("grass")
    area=area_object(worldx,worldy)[0]
    if area.areatype != "nothing":
        rel=area.relcoords(worldx,worldy)
        if rel[0] != -1:
            return area.cell(rel[0],rel[1])
    ground=cell_ground(worldx,worldy)
    if ground.kind == "water":
        return iddict([ground])
    if getparamw(str(worldx)+"_"+str(worldy)+"_creature",400) == 0:
        creature=creature_item(worldx,worldy,ground.zcoord)
        if post_reg:
            changes[(worldx,worldy)]={}
            changes[(worldx,worldy)][ground.item_id]=ground
            changes[(worldx,worldy)][creature.item_id]=creature
        return iddict([ground,creature])
    forest=cell_forest(worldx,worldy)
    if forest > 0:
        if getparamw(str(worldx/3)+"_"+str(worldy/3)+"_obj",10) == 0 and ground.kind == "grass":
            slotx=getparamw(str(worldx/3)+"_"+str(worldy/3)+"_objx",3)
            sloty=getparamw(str(worldx/3)+"_"+str(worldy/3)+"_objy",3)
            if worldx%3 != slotx or worldy%3 != sloty:
                return iddict([ground])
            for (xoff,yoff) in [(-1,-1),(-1,0),(-1,1),(0,-1)]: #,(1,-1),(1,0)]:
                if getparamw(str(worldx/3+xoff)+"_"+str(worldy/3+yoff)+"_obj",10) == 0:
                    otherx=getparamw(str(worldx/3+xoff)+"_"+str(worldy/3+yoff)+"_objx",3)
                    if abs(otherx+xoff*3-slotx) < 3:
                        othery=getparamw(str(worldx/3+xoff)+"_"+str(worldy/3+yoff)+"_objy",3)
                        if abs(othery+yoff*3-sloty) < 3:
                            return iddict([ground])
            tree=tree_item(worldx,worldy,cell_ground(worldx+1,worldy+1).zcoord)
            return iddict([ground,tree])
    return iddict([ground])

def passable(worldx,worldy):
    for i in cell_object(worldx-1,worldy-1).values():
        if i.type == "tree":
            print "blocked by tree"
            return False
    for i in cell_object(worldx,worldy).values():
        if i.type == "block" and i.zcoord <= player.z+player.height and i.zcoord > player.z:
            print "blocked by block"
            return False
        if i.type == "door":
            print "blocked by door"
            return not i.closed
    for i in cell_object(worldx-1,worldy).values():
        if i.type == "door" and (i.dir == 0 or i.dir == 2):
            print "blocked by door"
            return not i.closed
    for i in cell_object(worldx-2,worldy).values():
        if i.type == "door" and (i.dir == 0 or i.dir == 2):
            print "blocked by door"
            return not i.closed
    return True

def change_cell(worldx,worldy):
    global changes
    if (worldx,worldy) in changes:
        if xy2chunk(worldx,worldy) in chunks:
            chunks[xy2chunk(worldx,worldy)].reg_update(worldx%G_CHUNK_SIZE,worldy%G_CHUNK_SIZE)
        return
    z=cell_object(worldx,worldy)
    #print "change_cell",worldx,worldy,"computed",z
    changes[(worldx,worldy)]=cell_object(worldx,worldy)
    if xy2chunk(worldx,worldy) in chunks:
        chunks[xy2chunk(worldx,worldy)].reg_update(worldx%G_CHUNK_SIZE,worldy%G_CHUNK_SIZE)

def delitem(worldx,worldy,itemid):
    if (worldx,worldy) in changes:
        #print "delitem"
        del changes[(worldx,worldy)][itemid]
        if len(changes[(worldx,worldy)]) == len(cell_object(worldx,worldy,False,False)):
            change_needed=False
            for obj in changes[(worldx,worldy)].values():
                if obj.changed():
                    change_needed=True
            if not change_needed:
                del changes[(worldx,worldy)]
        if xy2chunk(worldx,worldy) in chunks:
            chunks[xy2chunk(worldx,worldy)].reg_update(worldx%int(G_CHUNK_SIZE),worldy%int(G_CHUNK_SIZE))

coeffs=[]
forest_coeffs=[]
creature_coeffs=[]
random.seed(getparamw("height_seed",1000000))
for i in range(3):
    coeffs.append([random.random(),random.random(),random.random()/5.0,random.random()/5.0,random.random()*2*math.pi,random.normalvariate(1.0,0.5)*5.0])
for i in range(3):
    coeffs.append([random.random(),random.random(),random.random(),random.random(),random.random()*2*math.pi,random.normalvariate(1.0,0.5)])
for i in range(3):
    coeffs.append([random.random(),random.random(),random.random()/13.0,random.random()/13.0,random.random()*2*math.pi,random.normalvariate(1.0,0.5)*13.0])
for i in range(3):
    coeffs.append([random.random(),random.random(),random.random()/53.0,random.random()/53.0,random.random()*2*math.pi,random.normalvariate(1.0,0.5)*53.0])
for s in coeffs:
    print "fq:",s[2],s[3],"ang:",s[4],"amp:",s[5]

#lower frequencies because we work on 3x3 chunks
for i in range(3):
    forest_coeffs.append([random.random(),random.random(),random.random()/4.0,random.random()/4.0,random.random()*2*math.pi,random.normalvariate(1.0,0.5)*13.0])
for i in range(3):
    forest_coeffs.append([random.random(),random.random(),random.random()/13.0,random.random()/13.0,random.random()*2*math.pi,random.normalvariate(1.0,0.5)*53.0])

for i in range(3):
    creature_coeffs.append([random.random(),random.random(),random.random()/13.0,random.random()/13.0,random.random()*2*math.pi,random.normalvariate(1.0,0.5)/4])
for i in range(3):
    creature_coeffs.append([random.random(),random.random(),random.random()/53.0,random.random()/53.0,random.random()*2*math.pi,random.normalvariate(1.0,0.5)])



cellheight_cache={}
def cell_height(worldx,worldy):
    if (worldx,worldy) in cellheight_cache:
        return cellheight_cache[(worldx,worldy)]
    if len(cellheight_cache) > 50000:
       for k in cellheight_cache.keys()[:20000]:
            del cellheight_cache[k]
    height=0
    for i in range(len(coeffs)):
        xph=coeffs[i][0]
        zph=coeffs[i][1]
        xfq=coeffs[i][2]
        zfq=coeffs[i][3]
        ang=coeffs[i][4]
        amp=coeffs[i][5]
        angcos=math.cos(ang*2*math.pi)
        angsin=math.sin(ang*2*math.pi)
        worldxfq=worldx*xfq
        worldyfq=worldy*zfq
        height+=amp*(( math.sin(worldxfq*angsin+worldyfq*angcos+xph) + math.sin(worldyfq*angsin+worldxfq*angcos+zph) )**3)
    cellheight_cache[(worldx,worldy)]=height
    return height
    #return cell_creature(worldx,worldy)*80

cellforest_cache={}
def cell_forest(wx,wy):
    worldx=wx/3
    worldy=wy/3
    if (worldx,worldy) in cellforest_cache:
        return cellforest_cache[(worldx,worldy)]
    if len(cellforest_cache) > 50000:
        for k in cellforest_cache.keys()[:20000]:
            del cellforest_cache[k]
    forest=0
    for i in range(len(forest_coeffs)):
        xph=forest_coeffs[i][0]
        zph=forest_coeffs[i][1]
        xfq=forest_coeffs[i][2]
        zfq=forest_coeffs[i][3]
        ang=forest_coeffs[i][4]
        amp=forest_coeffs[i][5]
        angcos=math.cos(ang*2*math.pi)
        angsin=math.sin(ang*2*math.pi)
        worldxfq=worldx*xfq
        worldyfq=worldy*zfq
        forest+=amp*(( math.sin(worldxfq*angsin+worldyfq*angcos+xph) + math.sin(worldyfq*angsin+worldxfq*angcos+zph) )**3)
    cellforest_cache[(worldx,worldy)]=forest
    return forest

cellcreature_cache={}
#no ^3 for sines
def cell_creature(worldx,worldy):
    #if (worldx,worldy) in cellcreature_cache:
    #    return cellcreature_cache[(worldx,worldy)]
    #if len(cellcreature_cache) > 50000:
    #    for k in cellcreature_cache.keys()[:20000]:
    #        del cellcreature_cache[k]
    #creature=0
    #for i in range(len(creature_coeffs)):
    #    xph=creature_coeffs[i][0]
    #    zph=creature_coeffs[i][1]
    #    xfq=creature_coeffs[i][2]
    #    zfq=creature_coeffs[i][3]
    #    ang=creature_coeffs[i][4]
    #    amp=creature_coeffs[i][5]
    #    angcos=math.cos(ang*2*math.pi)
    #    angsin=math.sin(ang*2*math.pi)
    #    worldxfq=worldx*xfq
    #    worldyfq=worldy*zfq
    #    creature+=amp*(( math.sin(worldxfq*angsin+worldyfq*angcos+xph) + math.sin(worldyfq*angsin+worldxfq*angcos+zph) ))
    #cellcreature_cache[(worldx,worldy)]=creature
    #return creature
    return (cell_height(worldx,worldy)/300+2*(cell_forest(worldx,worldy) > 0))%4.0


def cell_ground(worldx,worldy):
    ht=cell_height(worldx,worldy)
    margins=0
    retval=ground_item(ht)
    for xoff,yoff in [(-1,0),(1,0),(0,-1),(0,1)]:
        if max(-0.5,int(cell_height(worldx+xoff,worldy+yoff)/8.0+1)-1) < retval.zcoord-0.01: #+1 & -1 because int() is toward 0 &! -inf
            margins=5
    retval.margins=margins
    return retval
    
def paint_player(rect):
    global gr_squares,gr_colors,obj_trigs,obj_colors,obj_idcolors
    xcoords=[-1.0,-0.707,0.0,0.707,1.0,0.707,0.0,-0.707]
    ycoords=[0.0,0.707,1.0,0.707,0.0,-0.707,-1.0,-0.707]
    if player.visible:
        a1=a2=a3=1.0
    else:
        a1=a3=-1.0
        a2=0.0
    for i in range(8):
        obj_trigs+=scale([xcoords[i-1],ycoords[i-1]],rect)+[-player.height]
        obj_trigs+=scale([xcoords[i],ycoords[i]],rect)+[-player.height]
        obj_trigs+=scale([0.0,0.0],rect)+[0.0]
        obj_colors+=[0.5+(0.05*i),0.5+(0.05*i),0.5+(0.05*i),a1]
        obj_colors+=[0.5+(0.05*i),0.5+(0.05*i),0.5+(0.05*i),a2]
        obj_colors+=[0.5+(0.05*i),0.5+(0.05*i),0.5+(0.05*i),a3]
    obj_idcolors+=idcolor(player.item_id,player.serial,3*8)

def gen_chunk(worldx,worldy):
    global misses
    c=chunk(worldx,worldy)
    cotime=0
    maketime=0
    misses=0
    for y in range(int(G_CHUNK_SIZE)):
        for x in range(int(G_CHUNK_SIZE)):
            start=time.clock()
            co=cell_object(worldx+x,worldy+y,True).values()
            cotime+=time.clock()-start
            start=time.clock()
            for obj in co:
                if obj.type != "nothing":
                    obj.make(c,x,y)
                    g_objectpos[obj.item_id]=(worldx+x,worldy+y)
            c.coord_obj_trigs.append(c.obj_trigs)
            c.coord_obj_colors.append(c.obj_colors)
            c.coord_obj_idcolors.append(c.obj_idcolors)
            c.obj_trigs=[]
            c.obj_colors=[]
            c.obj_idcolors=[]
            maketime+=time.clock()-start
    print "gen: cell_object (",misses,")",cotime,"make",maketime
    return c
    
def make_polygons():
    global gr_squares,gr_colors,gr_idcolors,obj_trigs,obj_colors,obj_idcolors,player,chunks
    gr_squares=[]
    gr_colors=[]
    gr_idcolors=[]
    obj_trigs=[]
    obj_colors=[]
    obj_idcolors=[]
    r=[0,0,0,0]
    chunkx=player.x-player.x%int(G_CHUNK_SIZE)
    chunky=player.y-player.y%int(G_CHUNK_SIZE)
    for xoff in range(-3,4):  #range(-int(1+9/zoomlevel),int(2+9/zoomlevel)):
        for yoff in range(-3,4):  #range(-int(1+9/zoomlevel),int(2+9/zoomlevel)):
            coordid=(chunkx+xoff*int(G_CHUNK_SIZE),chunky+yoff*int(G_CHUNK_SIZE))
            if coordid not in chunks:
                print "gen"
                chunks[coordid]=gen_chunk(chunkx+xoff*int(G_CHUNK_SIZE),chunky+yoff*int(G_CHUNK_SIZE))
            else: 
                chunks[coordid].update()
    if random.random() < 0.1:
        xoff=-4
        done=False
        while xoff < 5:
            yoff=-4
            while yoff < 5:
                coordid=(chunkx+xoff*int(G_CHUNK_SIZE),chunky+yoff*int(G_CHUNK_SIZE))
                if coordid not in chunks:
                    print "PRE gen"
                    chunks[coordid]=gen_chunk(chunkx+xoff*int(G_CHUNK_SIZE),chunky+yoff*int(G_CHUNK_SIZE))
                    done=True
                    break
                yoff+=1
            xoff+=1
            if done:
                break
        off=-5
        while off < 6:
            coordid=(chunkx-5*int(G_CHUNK_SIZE),chunky+off*int(G_CHUNK_SIZE))
            if coordid in chunks:
                del chunks[coordid]
            coordid=(chunkx+5*int(G_CHUNK_SIZE),chunky+off*int(G_CHUNK_SIZE))
            if coordid in chunks:
                del chunks[coordid]
            coordid=(chunkx+off*int(G_CHUNK_SIZE),chunky-5*int(G_CHUNK_SIZE))
            if coordid in chunks:
                del chunks[coordid]
            coordid=(chunkx+off*int(G_CHUNK_SIZE),chunky+5*int(G_CHUNK_SIZE))
            if coordid in chunks:
                del chunks[coordid]
            off+=1
    paint_player([-1.0,1.0,1.0,-1.0])

def get_perspective(type):
    if type == "world":
        return array([math.cos(player.direction),-math.sin(player.direction),0.0,0.0,
                      math.sin(player.direction)*math.cos(player.lookangle),math.cos(player.direction)*math.cos(player.lookangle),math.sin(player.lookangle),0.0,
                      0.0,0.0,-0.0,1.0,
                      math.sin(player.direction)*math.sin(player.lookangle),math.cos(player.direction)*math.sin(player.lookangle),-math.cos(player.lookangle),10.0],'f')
    elif type == "ui":
        return array([1.0,0.0,0.0,0.0,
                      0.0,0.0,1.0,0.0,
                      0.0,0.0,0.0,1.0,
                      0.0,0.0,0.0,1.0],'f')

def objectbydata(objdata):
    retval=nothing_item
    pos=objdata[0]
    if pos in changes:
        objlist=changes[(pos[0],pos[1])]
    else:
        objlist=cell_object(pos[0],pos[1])
    return find_obj(objlist,objdata[1],objdata[2])
    #if objdata[1] in objlist:
    #    retval=changes[(pos[0],pos[1])][objdata[1]]
    #else:
    #    for obj in objlist.values():
    #        if obj.serial == objdata[2]:
    #            retval=obj
    #return retval

def screen2xy(xpix,ypix,zoom=0):
    global win_size
    if zoom == 0:
        zoom=zoomlevel
    scrx=float(xpix)/(win_size[0]*0.5)-1.0
    scry=float(ypix)/(win_size[1]*0.5)-1.0
    xoff=scrx*(G_CHUNK_SIZE/zoom)
    yoff=(-scry)*(G_CHUNK_SIZE/zoom)
    if xoff+player.x < 0:
        xoff-=1    #prevent rounding the whole range (-1,1) to 0
    if yoff+player.y < 0:
        yoff-=1
    return [xoff+player.x,yoff+player.y]

def screen2obj(xpix,ypix):
    gl.glReadBuffer(glarb.GL_COLOR_ATTACHMENT1)
    print ypix,win_size[1]-ypix
    idcolor=gl.glReadPixels(xpix,win_size[1]-ypix,1,1,gl.GL_RGB,gl.GL_FLOAT)[0][0]
    objid=idcolor[0]*131072
    objid+=131072*int(idcolor[1]*131072+0.1)
    objser=gl.glReadPixels(xpix,win_size[1]-ypix,1,1,gl.GL_RGB,gl.GL_FLOAT)[0][0][2]*65536
    if abs(int(objid+0.1)-objid) > 0.2:
        print "screen2obj rounding error",objid,"(",idcolor,") ->",int(objid)
    if int(objid) == 0:
        return [(0,0),0,0]
    pos=g_objectpos[int(objid+0.1)]
    return [pos,int(objid+0.1),int(objser+0.1)]

def xy2screen(x,y):
    scrx=(x-player.x+0.5)
    scry=(y-player.y+0.5)
    return [scrx,scry]

def xy2chunk(x,y):
    chx=x-x%G_CHUNK_SIZE
    chy=y-y%G_CHUNK_SIZE
    return (int(chx),int(chy))

def display_chunks():
    chunkx=player.x-player.x%G_CHUNK_SIZE
    chunky=player.y-player.y%G_CHUNK_SIZE
    playerx=player.x%G_CHUNK_SIZE
    playery=player.y%G_CHUNK_SIZE
    rendertime=0
    for xoff in range(-2,3):  #range(-int(1+9/zoomlevel),int(2+9/zoomlevel)):
        for yoff in range(-2,3):  #range(-int(1+9/zoomlevel),int(2+9/zoomlevel)):
            start=time.clock()
            chunks[(chunkx-xoff*G_CHUNK_SIZE,chunky-yoff*G_CHUNK_SIZE)].render((-xoff*G_CHUNK_SIZE-playerx)/(G_CHUNK_SIZE/zoomlevel),(-yoff*G_CHUNK_SIZE-playery)/(G_CHUNK_SIZE/zoomlevel),-(player.z+player.height)/(G_CHUNK_SIZE/zoomlevel))
            stop=time.clock()  
            rendertime+=stop-start          
    #print "chunkrender:",rendertime
    gl.glUniform3f(offsetpos,0.0,0.0,0.0)

def make_effects(timestep):
    global effects
    effects2=[]
    for e in effects:
        if e.alive():
            e.paint()
            effects2.append(e)
    effects=effects2

def write_line(x,y,size,string,opacity=1.0):
    global texts
    ch_starts=[0,8,16,24,32,40,47,56,65,72,78,87,94,105,114,122,130,138,146,154,162,171,180,191,199,208,216,220,441,448,455,462,469,477,484,491,499,506,512]
    texty=y
    textsize=size
    textx=x
    t=text()
    t.opacity=opacity
    for c in string:
        if c >= '0' and c <= '9':
            cid=ord(c)-ord('0')+28
        elif c >= 'a' and c <= 'z':
            cid=ord(c)-ord('a')
        else:
            cid=26
        left=ch_starts[cid]/512.0
        right=ch_starts[cid+1]/512.0
        t.quads+=[textx,texty,textx,texty+textsize*11.0/9,textx+textsize,texty+textsize*11.0/9,textx+textsize,texty]
        t.coords+=[left,21.5/32,left,1.0,right,1.0,right,21.5/32]
        textx+=textsize
    texts.append(t)

def make_text():
    global codebuf,saying,sayingop,texts,ui_info
    txt_quads=[]
    txt_txcoords=[]
    texts=[]
    write_line(-0.7,-0.9,0.05,codebuf)
    write_line(-0.7,-0.3,0.1,saying,sayingop)
    #dbgtxt=str(nonoverlap_obj(player.x,player.y,"big_building",40,6))+" aobj:"+area_object(player.x,player.y)[0].areatype
    dbgtxt="cr "+str(cell_creature(player.x,player.y))
    write_line(-0.7,-0.97,0.025,str(player.x)+","+str(player.y)+" "+dbgtxt+" fps "+str(int(1.0/frametime)))
    if len(ui_info.menu) != 0:
        menu_w=0
        for i in ui_info.menu:
            menu_w=max(menu_w,len(i))
        menux=1.0-menu_w*0.05
        menuy=-0.94
        for i in range(len(ui_info.menu)):
            op=0.7
            if i == ui_info.menusel:
                op=1.0
            write_line(menux,menuy,0.05,ui_info.menu[i].ljust(menu_w,' '),op)
            menuy+=0.05*11.0/9

def make_ui():
    global ui_chunk
    ui_chunk=chunk(0,0)
    hpbar_size=(G_CHUNK_SIZE-3)*player.hp/10.0
    ui_chunk.coord_obj_trigs=[[G_CHUNK_SIZE-1,1.0,-G_CHUNK_SIZE+1, G_CHUNK_SIZE,1.0,-G_CHUNK_SIZE+1, G_CHUNK_SIZE,1.0,-G_CHUNK_SIZE, G_CHUNK_SIZE,1.0,-G_CHUNK_SIZE, G_CHUNK_SIZE-1,1.0,-G_CHUNK_SIZE+1, G_CHUNK_SIZE-1,1.0,-G_CHUNK_SIZE]]
    ui_chunk.coord_obj_trigs[0]+=[G_CHUNK_SIZE-1.5,1.0,-G_CHUNK_SIZE+3, G_CHUNK_SIZE-1.5,1.0,0, G_CHUNK_SIZE-0.5,1.0,0, G_CHUNK_SIZE-1.5,1.0,-G_CHUNK_SIZE+3, G_CHUNK_SIZE-0.5,1.0,-G_CHUNK_SIZE+3, G_CHUNK_SIZE-0.5,1.0,0]
    ui_chunk.coord_obj_trigs[0]+=[G_CHUNK_SIZE-1.4,1.0,-G_CHUNK_SIZE+3, G_CHUNK_SIZE-0.6,1.0,-G_CHUNK_SIZE+3, G_CHUNK_SIZE-0.6,1.0,-G_CHUNK_SIZE+3+hpbar_size,
                               G_CHUNK_SIZE-0.6,1.0,-G_CHUNK_SIZE+3+hpbar_size, G_CHUNK_SIZE-1.4,1.0,-G_CHUNK_SIZE+3+hpbar_size, G_CHUNK_SIZE-1.4,1.0,-G_CHUNK_SIZE+3]
    ui_chunk.coord_obj_colors=[[1.0,1.0,1.0,0.3]*12+[1.0,0.0,1.0,0.3]*6]
    ui_chunk.coord_obj_idcolors=[[0.0,0.0,0.0]*18]
    ui_chunk.arrfied=False
    if player.in_hand.type != "nothing":
        print "ui_contains",player.in_hand
        player.in_hand.make(ui_chunk,G_CHUNK_SIZE-1,0,-G_CHUNK_SIZE)
    ui_chunk.writecell(1,1)
    ui_chunk.updated=True

def nextframe(timestep):
    global sayingop
    sayingop=pow(0.1,timestep)*sayingop
    for e in effects:
        e.update(timestep)
    for coords,items in changes.items():
        for i in items.values():
            if i.update(timestep):
                if xy2chunk(coords[0],coords[1]) in chunks:
                    #print "registering update at",(coords[0],coords[1])
                    chunks[xy2chunk(coords[0],coords[1])].reg_update(coords[0]%G_CHUNK_SIZE,coords[1]%G_CHUNK_SIZE)
    player.update(timestep)

saying="hello"
sayingop=1.0
codebuf=""
lastspell=""
def keybd(key,x,y):
    global codebuf,saying,sayingop,ui_info
    print "pressed"
    if key in cons+vows:
        codebuf+=key
    elif ord(key) == 13 and len(codebuf) != 0:
        saying=spell_word(codebuf).lower()
        sayingop=1.0
        codebuf=""
    elif key == '1':
        if len(ui_info.menu) != 0:
            ui_info.menu=[]
        elif player.in_hand.type != "nothing":
            ui_info.menu=player.in_hand.uses
    display()

def mouse(key,state,x,y):
    global codebuf,saying,sayingop,effects,lastspell
    if key == glut.GLUT_LEFT_BUTTON:
        print screen2obj(mousex,mousey)
        if state == glut.GLUT_DOWN:
            if codebuf == "" and lastspell != "":
                codebuf=lastspell
            if len(codebuf) > 0:
                lastspell=codebuf
                saying=spell_word(codebuf).lower()
                sayingop=1.0
                skeleton=parse_spell(codebuf)[0]
                if is_magical(skeleton):
                    effect=spell_effect(skeleton,codebuf)
                    objdata=screen2obj(mousex,mousey)
                    print "pix:",mousex,mousey,"->",objdata[0],"(zoom=",zoomlevel,")"
                    effect.apply(objdata[0],objdata[1],objdata[2])
                    effects.append(effect)
                codebuf=""
    elif key == glut.GLUT_RIGHT_BUTTON:
        if state == glut.GLUT_DOWN:
            if glut.glutGetModifiers()&glut.GLUT_ACTIVE_SHIFT:
                print "shift"
                objdata=screen2obj(mousex,mousey)
                change_cell(int(objdata[0][0]),int(objdata[0][1]))
                print objectbydata(objdata)
                objectbydata(objdata).interact()
            else:
                print "noshift"
                objdata=screen2obj(mousex,mousey)
                print "use on objdata:",objdata
                if player.in_hand.type != "nothing":
                    player.makecurrent()
                    usage=player.in_hand.uses[ui_info.menusel]
                    player.in_hand.use([objdata[1],objdata[2]],int(objdata[0][0]),int(objdata[0][1]),usage)

mousex=0
mousey=0
def mousemove(x,y):
    global mousex,mousey
    mousex=x
    mousey=y

def mousewheel(button,dir,x,y):
    global zoomlevel
    print x,y
    if dir > 0:
        zoomlevel*=0.9
    else:
        zoomlevel*=1.1
    print "zoom:",zoomlevel

def spkeybd(key,x,y):
    global player,ui_info
    if glut.glutGetModifiers()&glut.GLUT_ACTIVE_CTRL == 0:
        if len(ui_info.menu) != 0:
            if key == glut.GLUT_KEY_UP:
                ui_info.menusel+=1
                ui_info.menusel=ui_info.menusel%len(ui_info.menu)
            if key == glut.GLUT_KEY_DOWN:
                ui_info.menusel-=1
                if ui_info.menusel == -1:
                    ui_info.menusel=len(ui_info.menu)-1
            return
        if key == glut.GLUT_KEY_RIGHT:
            dir=1
        elif key == glut.GLUT_KEY_LEFT:
            dir=3
        elif key == glut.GLUT_KEY_UP:
            dir=0
        elif key == glut.GLUT_KEY_DOWN:
            dir=2
        else:
            return
        dir=(4+dir+int(2*player.direction/math.pi+0.5))%4
        if dir == 1:
            if passable(player.x+1,player.y) and passable(player.x+1,player.y-1):
                player.x+=1
        if dir == 3:
            if passable(player.x-2,player.y) and passable(player.x-2,player.y-1):
                player.x-=1
        if dir == 0:
            if passable(player.x,player.y+1) and passable(player.x-1,player.y+1):
                player.y+=1
        if dir == 2:
            if passable(player.x,player.y-2) and passable(player.x-1,player.y-2):
                player.y-=1
        player.compute_z()
    else:
        if key == glut.GLUT_KEY_RIGHT:
            player.direction+=0.02
        if key == glut.GLUT_KEY_LEFT:
            player.direction-=0.02
        if key == glut.GLUT_KEY_UP:
            player.lookangle-=0.01
        if key == glut.GLUT_KEY_DOWN:
            player.lookangle+=0.01
        if player.direction < 0.0:
            player.direction+=math.pi*2
        if player.direction > math.pi*2:
            player.direction-=math.pi*2
    display()

def reshape(w,h):
    global win_size
    win_size[0]=w
    win_size[1]=h
    gl.glViewport(0,0,w,h)


def display_text():
    global texts
    oppos=gl.glGetUniformLocation(txprog,"opacity")
    for t in texts:
        gl.glVertexAttribPointer(txattrpos,2,gl.GL_FLOAT,gl.GL_TRUE,0,array(t.quads,'f'))
        gl.glVertexAttribPointer(txattrcoord,2,gl.GL_FLOAT,gl.GL_TRUE,0,array(t.coords,'f'))
        gl.glUniform1f(oppos,t.opacity)
        gl.glDrawArrays(gl.GL_QUADS,0,len(t.quads)/2)

gr_squares=[]
gr_colors=[]
gr_idcolors=[]
obj_trigs=[]
obj_colors=[]
obj_idcolors=[]
chunks={}
effects=[]
changes={}
g_objectpos={}
texts=[]
frametime=1.0
framestart=time.clock()
g_clockbase=time.clock()
zoomlevel=2.0
def display():
    global font,frametime,framestart
    starttime=time.clock()
    frameend=time.clock()
    make_polygons()
    
    make_text()
   
    gl.glUseProgram(glprog)
    #clear id buffer
    gl.glDrawBuffers([glarb.GL_COLOR_ATTACHMENT0,glarb.GL_COLOR_ATTACHMENT1])
    gl.glClear(gl.GL_COLOR_BUFFER_BIT+gl.GL_DEPTH_BUFFER_BIT)
    gl.glEnable(gl.GL_DEPTH_TEST)
    gl.glEnableVertexAttribArray(attrpos)
    gl.glEnableVertexAttribArray(attrcolor)
    gl.glEnableVertexAttribArray(attridcolor)
    gl.glUniform1f(scalepos,zoomlevel/G_CHUNK_SIZE)
    gl.glUniform1f(timepos,time.clock()-g_clockbase)
    gl.glUniformMatrix4fv(persppos,1,gl.GL_TRUE,get_perspective("world"))
    ski_chunk.render(0,0)
    display_chunks()
    make_effects(frameend-framestart)
    frametime=frameend-framestart
    gl.glUniformMatrix4fv(persppos,1,gl.GL_TRUE,get_perspective("world"))
    #gl.glUniform3f(offsetpos,0.0,0.0,-5.0/16)
    gl.glVertexAttribPointer(attrpos,3,gl.GL_FLOAT,gl.GL_TRUE,0,array(gr_squares,'f'))
    gl.glVertexAttribPointer(attrcolor,4,gl.GL_FLOAT,gl.GL_TRUE,0,array(gr_colors,'f'))
    gl.glVertexAttribPointer(attridcolor,3,gl.GL_FLOAT,gl.GL_TRUE,0,array(gr_idcolors,'f'))
    gl.glDrawArrays(gl.GL_QUADS,0,len(gr_squares)/3)
    gl.glVertexAttribPointer(attrpos,3,gl.GL_FLOAT,gl.GL_TRUE,0,array(obj_trigs,'f'))
    gl.glVertexAttribPointer(attrcolor,4,gl.GL_FLOAT,gl.GL_TRUE,0,array(obj_colors,'f'))
    gl.glVertexAttribPointer(attridcolor,3,gl.GL_FLOAT,gl.GL_TRUE,0,array(obj_idcolors,'f'))
    gl.glDrawArrays(gl.GL_TRIANGLES,0,len(obj_trigs)/3)
    gl.glDisable(gl.GL_DEPTH_TEST)
    #if not ui_chunk.updated:
    make_ui()
    gl.glUniform1f(scalepos,1.0/G_CHUNK_SIZE)
    gl.glUniformMatrix4fv(persppos,1,gl.GL_TRUE,get_perspective("ui"))
    ui_chunk.render(0,0)
    gl.glUniform1f(scalepos,1.0)
    gl.glDisableVertexAttribArray(attrpos)
    gl.glDisableVertexAttribArray(attrcolor)
    gl.glDisableVertexAttribArray(attridcolor)
    gl.glActiveTexture(gl.GL_TEXTURE1)
    gl.glBindTexture(gl.GL_TEXTURE_2D,font)
    gl.glUseProgram(txprog)
    gl.glEnableVertexAttribArray(txattrpos)
    gl.glEnableVertexAttribArray(txattrcoord)
    fnpos=gl.glGetUniformLocation(txprog,"tex")
    gl.glEnable(gl.GL_TEXTURE_2D)
    gl.glUniform1i(fnpos,1)
    gl.glTexParameterf(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR)
    gl.glTexParameterf(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_NEAREST)
    display_text()
    gl.glFlush()

    glarb.glBindFramebuffer(glarb.GL_READ_FRAMEBUFFER,selection_fbo)
    gl.glReadBuffer(glarb.GL_COLOR_ATTACHMENT0)
    glarb.glBindFramebuffer(glarb.GL_DRAW_FRAMEBUFFER,0)
    gl.glDrawBuffer(gl.GL_FRONT_LEFT)
    #-3 for testing
    glarb.glBlitFramebuffer(0,0,win_size[0],win_size[1],0,0,win_size[0],win_size[1],gl.GL_COLOR_BUFFER_BIT,gl.GL_NEAREST)
    gl.glFlush()    
    glarb.glBindFramebuffer(glarb.GL_DRAW_FRAMEBUFFER,selection_fbo)
    gl.glDrawBuffers([glarb.GL_COLOR_ATTACHMENT0,glarb.GL_COLOR_ATTACHMENT1])
    #glut.glutSwapBuffers()
    nextframe(frameend-framestart)
    framestart=frameend

font=0
def create_font():
    global font
    fimg=Image.open('in/font.bmp')
    txrect=fimg.crop([0,0,512,32])
    txrect=txrect.transpose(Image.FLIP_TOP_BOTTOM)
    pixels=list(txrect.getdata())
    tximg=[]
    for p in range(len(pixels)):
        tximg+=[float(pixels[p][0])/255,float(pixels[p][1])/255,float(pixels[p][2])/255]
    font=gl.glGenTextures(1)
    gl.glActiveTexture(gl.GL_TEXTURE0)
    gl.glBindTexture(gl.GL_TEXTURE_2D,font)
    gl.glTexImage2D(gl.GL_TEXTURE_2D,0,3,512,32,0,gl.GL_RGB,gl.GL_FLOAT,array(tximg,'f'))

mesh_calc=mesher()
ground_type=suitability()
player=player_item()
player.x=30
player.y=-37
player.direction=0
player.compute_z()
player.lookangle=math.pi/4
g_objectpos[player.item_id]=(0,0)
ski_chunk=gen_sky()
ui_chunk=chunk(0,0)
ui_info=something()
ui_info.menu=[]
ui_info.menusel=0


#initialize glut
win_size=[100,100]
glut.glutInit()
glut.glutInitDisplayMode(glut.GLUT_DEPTH)
glut.glutInitWindowSize(600,600)
glut.glutCreateWindow("GPU test")
glut.glutDisplayFunc(display)
glut.glutSpecialFunc(spkeybd)
glut.glutKeyboardFunc(keybd)
glut.glutMouseFunc(mouse)
glut.glutPassiveMotionFunc(mousemove)
glut.glutMouseWheelFunc(mousewheel)
glut.glutReshapeFunc(reshape)
glut.glutIdleFunc(display)

#create shaders
shvert=gl.glCreateShader(gl.GL_VERTEX_SHADER)
shgeom=gl.glCreateShader(gl.GL_GEOMETRY_SHADER)
shfrag=gl.glCreateShader(gl.GL_FRAGMENT_SHADER)
txvert=gl.glCreateShader(gl.GL_VERTEX_SHADER)
txfrag=gl.glCreateShader(gl.GL_FRAGMENT_SHADER)
gl.glShaderSource(shvert,"varying vec4 col; \
                          varying vec3 id;  \
                          uniform vec3 offset; \
                          uniform float scale; \
                          uniform mat4 camera; \
                          attribute vec4 color; \
                          attribute vec3 idcolor; \
                          attribute vec3 pos; \
                          void main() {  \
                           col=color;  \
                           id=idcolor; gl_Position=vec4((pos*scale+offset).xyz,1.0);}")

gl.glShaderSource(shgeom,"""#version 150
                          layout(triangles) in;
                          layout(triangle_strip, max_vertices=10) out; 
                          in vec4 col[3];  
                          in vec3 id[3]; 
                          out vec4 fragcol;
                          out vec3 fragid;
                          uniform mat4 camera;
                          void main() {
                              vec4 newcol[3];
                              float dist;
                              if (col[0].w > -2) {
                                  for(int i = 0; i < 3; i++) {  
                                    gl_Position = camera*gl_in[i].gl_Position;  
                                    fragcol=col[i];
                                    fragid=id[i];
                                    EmitVertex();  
                                  }  
                              } else {
                                  vec4 up_lb=camera*gl_in[0].gl_Position;
                                  vec4 up_lt=camera*vec4(gl_in[0].gl_Position.x,gl_in[1].gl_Position.y,gl_in[0].gl_Position.z,1.0);
                                  vec4 up_rb=camera*vec4(gl_in[1].gl_Position.x,gl_in[0].gl_Position.y,gl_in[0].gl_Position.z,1.0);
                                  vec4 up_rt=camera*vec4(gl_in[1].gl_Position.x,gl_in[1].gl_Position.y,gl_in[0].gl_Position.z,1.0);
                                  vec4 dn_lb=camera*vec4(gl_in[0].gl_Position.x,gl_in[0].gl_Position.y,gl_in[1].gl_Position.z,1.0);
                                  vec4 dn_lt=camera*vec4(gl_in[0].gl_Position.x,gl_in[1].gl_Position.y,gl_in[1].gl_Position.z,1.0);
                                  vec4 dn_rb=camera*vec4(gl_in[1].gl_Position.x,gl_in[0].gl_Position.y,gl_in[1].gl_Position.z,1.0);
                                  vec4 dn_rt=camera*gl_in[1].gl_Position;
                                  vec4 v1,v2,v3,v4,v5,v6;
                                  if (dot((up_lb-up_rb).xyw,up_lb.xyw) < 0)  //is left visible
                                  {
                                    if (dot((up_lb-up_lt).xyw,up_lb.xyw) < 0) {  //is bottom visible
                                        v1=up_lt;v2=dn_lt;
                                        v3=up_lb;v4=dn_lb;
                                        v5=up_rb;v6=dn_rb;
                                    } else {
                                        v1=up_rt;v2=dn_rt;
                                        v3=up_lt;v4=dn_lt;
                                        v5=up_lb;v6=dn_lb;
                                    }
                                  } else {
                                    if (dot((up_lt-up_lb).xyw,up_lt.xyw) < 0) { //is top visible
                                        v1=up_rb;v2=dn_rb;
                                        v3=up_rt;v4=dn_rt;
                                        v5=up_lt;v6=dn_lt;
                                    } else {
                                        v1=up_lb;v2=dn_lb;
                                        v3=up_rb;v4=dn_rb;
                                        v5=up_rt;v6=dn_rt;
                                    }
                                  }
                                  newcol[0]=col[0];
                                  newcol[1]=col[1];
                                  newcol[2]=col[2];
                                  newcol[0].w+=3;
                                  fragcol=newcol[1];
                                  fragid=id[0];
                                  gl_Position=up_lb;
                                   EmitVertex();
                                  gl_Position=up_lt;
                                  fragcol=newcol[0];
                                   EmitVertex();
                                  gl_Position=up_rb;
                                   EmitVertex();
                                  gl_Position=up_rt;
                                  fragcol=newcol[1];
                                   EmitVertex();
                                  if (abs(gl_in[0].gl_Position.z-gl_in[1].gl_Position.z) > 0.0001) {
                                      EndPrimitive();
                                      fragcol=newcol[0];
                                      gl_Position=v1;
                                       EmitVertex();
                                      fragcol=newcol[2];
                                      gl_Position=v2;
                                       EmitVertex();
                                      gl_Position=v3;
                                      fragcol=newcol[0];
                                       EmitVertex();
                                      gl_Position=v4;
                                      fragcol=newcol[2];
                                       EmitVertex();
                                      gl_Position=v5;
                                      fragcol=newcol[0];
                                       EmitVertex();
                                      gl_Position=v6;
                                      fragcol=newcol[2];
                                       EmitVertex();
                                  }
                              }
                          };
                          """)

gl.glShaderSource(shfrag,"""varying vec4 fragcol; varying vec3 fragid;
                          uniform float time;
                          void main() {gl_FragData[0]=fragcol;
                          if (gl_FragData[0].x < 0)
                            gl_FragData[0].x*=(sin(time*2)-1)/2.0;
                          if (gl_FragData[0].y < 0)
                            gl_FragData[0].y*=(sin(time*2)-1)/2.0;
                          if (gl_FragData[0].z < 0)
                            gl_FragData[0].z*=(sin(time*2)-1)/2.0;
                          if (gl_FragData[0].w < 0) {
                            gl_FragData[0].w=(sin(gl_FragData[0].w*6.28+time*4)+1.5)/3.0; //6.28=2pi
                          }
                          if (fragid.x+fragid.y+fragid.z > 0.0) {
                             gl_FragData[1]=vec4(fragid,1.0); }
                          else  
                             gl_FragData[1]=vec4(fragid,0.0);   
                        }""")
gl.glShaderSource(txvert,"attribute vec2 txc; \
attribute vec2 pos; \
varying vec2 txpos; \
void main() {txpos=txc; gl_Position=gl_ModelViewProjectionMatrix * vec4(pos,1.0,1.0);}")
gl.glShaderSource(txfrag,"uniform sampler2D tex;   \
 varying vec2 txpos; \
 uniform float opacity; \
void main() {gl_FragData[0]=vec4(texture2D(tex,txpos).rgb,opacity);gl_FragData[1]=vec4(0.0,0.0,0.0,0.0);}")

#create programs
glprog=gl.glCreateProgram()
gl.glAttachShader(glprog,shvert)
gl.glAttachShader(glprog,shgeom)
gl.glAttachShader(glprog,shfrag)
gl.glCompileShader(shvert)
gl.glCompileShader(shgeom)
gl.glCompileShader(shfrag)
gl.glLinkProgram(glprog)

txprog=gl.glCreateProgram()
gl.glAttachShader(txprog,txvert)
gl.glAttachShader(txprog,txfrag)
gl.glCompileShader(txvert)
gl.glCompileShader(txfrag)
print "v:\n",gl.glGetShaderInfoLog(shvert)
print "g:\n",gl.glGetShaderInfoLog(shgeom)
print "f:\n",gl.glGetShaderInfoLog(shfrag)
gl.glLinkProgram(txprog)

create_font()

#get attrib & uniform locations for programs
attrpos=gl.glGetAttribLocation(glprog,'pos')
attrcolor=gl.glGetAttribLocation(glprog,'color')
attridcolor=gl.glGetAttribLocation(glprog,'idcolor')
txattrpos=gl.glGetAttribLocation(txprog,'pos')
txattrcoord=gl.glGetAttribLocation(txprog,'txc')
offsetpos=gl.glGetUniformLocation(glprog,"offset")
scalepos=gl.glGetUniformLocation(glprog,"scale")
persppos=gl.glGetUniformLocation(glprog,"camera")
timepos=gl.glGetUniformLocation(glprog,"time")
print attrpos,attrcolor

#get window size
win_size[0]=glut.glutGet(glut.GLUT_WINDOW_WIDTH)
win_size[1]=glut.glutGet(glut.GLUT_WINDOW_HEIGHT)

#creating renderbuffers (rdf)
#print gl.glGetString(gl.GL_EXTENSIONS)
if not bool(glarb.glGenRenderbuffers):
    print "no support"
selection_rdf=glarb.glGenRenderbuffers(1)
glarb.glBindRenderbuffer(glarb.GL_RENDERBUFFER,selection_rdf)
glarb.glRenderbufferStorage(glarb.GL_RENDERBUFFER,gl.GL_RGBA32F,win_size[0]*2,win_size[1]*2)
rendering_depth_rdf=glarb.glGenRenderbuffers(1)
glarb.glBindRenderbuffer(glarb.GL_RENDERBUFFER,rendering_depth_rdf)
glarb.glRenderbufferStorage(glarb.GL_RENDERBUFFER,gl.GL_DEPTH_COMPONENT32,win_size[0]*2,win_size[1]*2)
rendering_rdf=glarb.glGenRenderbuffers(1)
glarb.glBindRenderbuffer(glarb.GL_RENDERBUFFER,rendering_rdf)
glarb.glRenderbufferStorage(glarb.GL_RENDERBUFFER,gl.GL_RGBA,win_size[0]*2,win_size[1]*2)

#setting up the framebuffer (fbo)
selection_fbo=glarb.glGenFramebuffers(1)
glarb.glBindFramebuffer(glarb.GL_DRAW_FRAMEBUFFER,selection_fbo)
glarb.glFramebufferRenderbuffer(glarb.GL_FRAMEBUFFER,glarb.GL_COLOR_ATTACHMENT0,glarb.GL_RENDERBUFFER,rendering_rdf)
glarb.glFramebufferRenderbuffer(glarb.GL_FRAMEBUFFER,glarb.GL_COLOR_ATTACHMENT1,glarb.GL_RENDERBUFFER,selection_rdf)
glarb.glFramebufferRenderbuffer(glarb.GL_FRAMEBUFFER,glarb.GL_DEPTH_ATTACHMENT,glarb.GL_RENDERBUFFER,rendering_depth_rdf)
gl.glDrawBuffers([glarb.GL_COLOR_ATTACHMENT0,glarb.GL_COLOR_ATTACHMENT1])
print glarb.glCheckFramebufferStatus(glarb.GL_DRAW_FRAMEBUFFER)

#enable blending
gl.glEnable(gl.GL_BLEND)
gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA)

gl.glDepthFunc(gl.GL_GREATER)
gl.glClearColor(0.0,0.0,0.0,0.0)
gl.glClearDepth(-100.0)
glut.glutMainLoop()

gen_chunk(-32,64)
running=True

while running:
    command=raw_input(">")
    if command[0] != '-':
        spell=command
        print "pronounced:",spell_word(spell)
        parsing=parse_spell(spell)
        print "parsed:",parsing
        if is_magical(parsing[0]):
            print "A MAGIC WORD"
            quality=round(100.0/float(getparamw(spell+"_quality",100)**2),2)
            print "\tquality (out of 100):",quality
            print "\tpower:",quality*(g_difficulty**len(parsing[0]))
            #print "\teffect:",spell_effect(parsing[0])
    else:
        if command == '-':
            running=false
        else:
            coords=command[1:].split()
            print cell_object(int(coords[0]),int(coords[1]))

##fading
##float aoff=0;  \
##                          if (color.w < -1) {  \
##                            color.w+=3.0;  \
##                            aoff=3.0;}  \
##                           vec3 p=pos+offset/scale;  \
##                           if (color.x > -1) \
##                            col=mix(color,vec4(color.xyz,0.0),clamp(dot(p,p)*0.2,0.0,1.0)); \
##                           else { \
##                            col=color+vec4(5.0,0.0,0.0,0.0);}  \
##                        col.w-=aoff;  \

    range(-int(1+1/zoomlevel),int(2+1/zoomlevel))
