bif="H:\programs\programming\pythonprograms/bg.jpg" #must be jpg
mif="H:\programs\programming\pythonprograms/ball.png"
import pygame
import sys
import wx
from pygame.locals import *
pygame.init()
screen=pygame.display.set_mode((1024,768),0,32) #size as background image size, builds a window
bg=pygame.image.load(bif).convert() # make locaion avbslolute, get iamge
mousea=pygame.image.load(mif).convert_alpha() # sets image as variable
"""2-3 tutorials:
    while True:
        for event in pygame.event.get():
            if  event.type==QUIT:
                pygame.quit()
                sys.exit()
    screen.blit(bg,(0,0)) #copy background to screen
    x,y=pygame.mouse.get_pos() # get x,y
    x-=mousea.get_width()/2 
    y-=mousea.get_height()/2
    blit=add
    screen.blit(mousea,(x,y))
    pygame.display.update() #hpouosekepping
 """
x,y=1,1
x2=0
movex,movey=0,0
#points=[]
scoreu,scoreopp=0,0
x1=0
y1=0
flag3=1
flagx,flagy=1,1
clock=pygame.time.Clock()
speed=200
while True:
    for event in pygame.event.get():
        if  event.type==QUIT:
            pygame.quit()
            sys.exit()
        #tutorial num 4-5 events
        if event.type==KEYDOWN: #preassed a a down key
            if event.key==K_LEFT:
                movex=-1
            elif event.key==K_RIGHT:
                movex=+1
            elif event.key==K_UP:
                movey=-1
            elif event.key==K_DOWN:
                movey=+1
        if event.type==KEYUP:
            if event.key==K_LEFT:
                movex=0
            elif event.key==K_RIGHT:
                movex=0
            elif event.key==K_UP:
                movey=0
            elif event.key==K_DOWN:
                movey=0
    x+=movex
    y+=movey
    screen.blit(bg,(0,0))
    #screen.blit(mousea,(x,y))
    #num6
    screen.lock() #nothing happens on screen until finished rawing or watever
    pygame.draw.rect(screen,(100,100,100),Rect((100,100),(130,50)) )#rect= position, size100,100,10RGB
    points=[(100,100),(150,50),(x,y)]
    color=(250,250,250)
    pygame.draw.polygon(screen,color,points)
    #radius=(x+y)
    #post=(200+x,200+y) #location of center of circle
    #pygame.draw.circle(screen,color,post,radius)
    #rect=(10,10,150,150)
    #pygame.draw.ellipse(screen, color,rect)
    pygame.draw.line(screen,color,(300,300),(300,500), 4)
    # 4=thikcness of line
    screen.unlock()
    #number 11-
    #    if event.type==MOUSEBUTTONDOWN:
    #        points.append(event.pos) # x,y of event
    #    if event.type==MOUSEMOTION:
    #        points.append(event.pos)
    x+=movex
    y+=movey
    screen.blit(bg,(0,0))
    screen.lock()
    pygame.draw.rect(screen,(100,100,100),Rect((x,y),(100,100)))
    pygame.draw.rect(screen,(100,100,100),Rect((650,x2),(100,100)))
    screen.unlock()
    #if len(points)>1: # more then one point, check if true
    #    pygame.draw.lines(screen,(100,100,100),True, points,3)
    screen.blit(mousea,(x1,y1))
    milli=clock.tick() #one milisicends
    seconds=milli/1000.0
    dm=seconds*speed
    #score()
    if x1>(1024-mousea.get_width()):
        flagx=-1
    elif x1<0:
        flagx=1
    if y1>(768-mousea.get_height()):
        flagy=-1
    elif y1<0:
        flagy=1
    if x2>(668):
        flag3=-1
    elif x2<0:
        flag3=1
    x2+=dm*flag3
    x1+=dm*flagx
    y1+=dm*flagy
    
    if(int(x1)==int(x)|int(x1)==int(x+100)):
        flagx=(-1)*flagx
        x1+=dm*flagx
    elif(int(x1)==int(650)):
        flagx=(-1)*flagx
        x1+=dm*flagx
    elif(int(x1)==750|int(x1+mousea.get_width())==int(x)):
        flagx=(-1)*flagx
        x1+=dm*flagx
    elif(int(x1+mousea.get_width())==int(x+100)|int(x1+mousea.get_width())==650|int(x1+mousea.get_width())==750):
        flagx=(-1)*flagx
        x1+=dm*flagx
    elif(int(y1)==int(y)|int(y1)==int(y+100)|int(y1)==int(x2)|int(y1)==int(x2+100)|int(y1+mousea.get_height())==int(y)|int(y1+mousea.get_height())==int(y+100)):
        flagy=(-1)*flagy
        y1+=dm*flagy
    elif(int(y1+mousea.get_height())==int(x2)|int(y1+mousea.get_height())==int(x2+100)):
        flagy=(-1)*flagy
        y1+=dm*flagy
    
    screen.blit(mousea,(x1,y1))
    """while(ifintersction(x,y,x1,y1,100,100,mousea.get_width(),mousea.get_height())):
        if((x1>x&x+100>x1)&(x1+mousea.get_width()>x&x+100>x1+mousea.get_width())):
            flagx=(-1)*flagx
        if((y1>y&y+100>y1)&(y1mousea.get_height()>y&y+100>y1+mousea.get_height())):
            flagy=(-1)*flagy
        y1+=dm*flagy
        x1+=dm*flagx
        screen.blit(mousea,(x1,y1))"""
    pygame.display.update()





def ifintersction(x1,y1,x2,y2,height1,width1,width2,height2):
    if((x2>x1&x1+width1>x2)|(width2+x2>x1&x1+width1>width2+x2)):
        if((y2>y1&y1+height1>y2)|(height2+y2>y1&y1+height1>height2+y2)):
            return true
