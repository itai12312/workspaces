import wx
class bucky(wx.Frame):

    def __init__(self,parent,id):
        wx.Frame.__init__(self,parent,id,'title',size=(300,200))
        panel=wx.Panel(self)
        button=wx.Button(panel,label="exit",pos=(130,10),size=(60,60))
        self.Bind(wx.EVT_BUTTON,self.closebutton,button)
        #to bind actions to buttons, evt_button-whenver a button is cicked,
        #go to self.cose button, apply method to where id=button
        self.Bind(wx.EVT_CLOSE,self.closewindow)
        #self.Bind(wx.EVT_KEY_DOWN,self.pressed("Key down"))
        #if hit the x button, it calls the self.closewindow function
        status=self.CreateStatusBar()
        menubar=wx.MenuBar()
        firstcol=wx.Menu()
        secod=wx.Menu()
        box=wx.MessageDialog(None,'Am i good?','title',wx.OK)
        # ok box
        box=wx.MessageDialog(None,'Am i good?','title',wx.YES_NO)
                answer=box.ShowModal()
        #value of yes or no in the variable
        box.Destroy()
        firstcol.Append(wx.NewId(),"title1","displayed on status bar")
        firstcol.Append(wx.NewId(),"title2","lol")
        menubar.Append(firstcol,"File")
        menubar.Append(secod,"Edit")
        self.SetMenuBar(menubar)
        #self.Bind(wx.EVT_MENU,self.pressed("opened a  menu"),firstcol)
        #sets the menu bar
        #status=name, NewId=make new item,append adds stuff to menu 

    def pressed(self, a):
        print a
        
    
    def closebutton(self,event):
        self.Close(True)
        #close using a button

    def closewindow(self,event):
        self.Destroy()
        #close using the x button

if  __name__=='__main__':
    app=wx.PySimpleApp()
    frame=bucky(parent=None,id=-1)
    frame.Show()
    app.MainLoop()
    

