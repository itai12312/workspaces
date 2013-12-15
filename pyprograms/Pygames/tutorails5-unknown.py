import wx
class bucky(wx.Frame):

    def __init__(self,parent,id):
        wx.Frame.__init__(self,parent,id,'title',size=(300,200))
        panel=wx.Panel(self)
        #test=wx.TextEntryDialog(None,"what up?","titel","default text")
        #if test.ShowModal()==wx.ID_OK:
        #    apples=test.GetValue()
        #wx.StaticText(panel,-1,apples,(10,10))
        #box=wx.TextEntryDialog(None,"Ener name","Title","default text")
        #parameters= parent,message, title,default text
        #if box.ShowModal()==wx.ID_OK:
         #   answer=box.GetValue()
        #store data only if they clicked ok
        #box=wx.SingleChoiceDialog(None,'question','title',['option#1','options#2']))
        #in squeare brackets-list of options
        #if box.ShowModal()==wx.ID_OK:
         #   answer=box.GetStringSelection()
        #store data only if they clicked ok
        #wx.StaticText(panel,-1,"this is static text",(10,10))
        #parameter=parent, id num,text,position(x,y) from top left corner
        #custom=wx.StaticText(panel,-1,"different pos",(30,30),(260,-1),wx.ALIGN_CENTER)
        #(260,-1),wx.ALIGN_CENTER=align,-1=size of your text
        #custom.SetForegroundColour('white')
        #custom.SetBackgroundColour('red')
        #make sure color is spelled the british way
        #pic=wx.Image("duck.bmp",wx.BITMAP_TYPE_BMP).ConvertToBitmap()
        #self.button=wx.BitmapButton(panel,-1,pic,pos=(50,50))
        #self.Bind(wx.EVT_BUTTON,self.doME,self.button)
        #self.button.SetDefault()
        # number tutorials 10
        #slider=wx.Slider(panel,-1,5,1,10,pos=(10,10),size=(250,100),style=wx.SL_AUTOICKS | wx.SL_LABLES)
        #paramters:parent,id,default value,min on slider, max amount, position,size,style
        #slider.SetTickFreq(5,1);#always put 1 for the 2nd parameter
        #auto ticks=disaply tick marks(like in java),labels-dispalys labels for min/max value
        #spinner=wx.SPinCtrl(panel,-1,"",(40,40),(90,-1))
        #parameters-parent,id,empty String,pos,size(-1=defualt height)
        #spinner.SetRange(1,100)
        #spinner.SetValue(10)
        #default value
        #wraps arou, 99,100,1,2
        #spinner.SP_WRAP
        wx.CheckBox(panel,-1,"apples",(20,20),(160,-1))
        mylist=['a','b','c']
        cunt=wx.ListBox(panel,-1,(20,20),(80,60),mylist,wx.LB_SINGLE)
        cunt.SetSelection(3)
        #default selection


if  __name__=='__main__':
    app=wx.PySimpleApp()
    frame=bucky(parent=None,id=-1)
    frame.Show()
    app.MainLoop()
    
        
        
def doME(self,event):
    self.Destroy()
    
        
#also do number 14
