import wx
class bucky(wx.Frame):

    def __init__(self,parent,id):
        wx.Frame.__init__(self,parent,id,'title',size=(300,200))
        panel=wx.Panel(self)


if  __name__=='__main__':
    app=wx.PySimpleApp()
    names=['a','b','c']
    #frame=bucky(parent=None,id=-1)
    modal=wx.SingleChoiceDialog(None,"d","title",names)
    if(modal.ShowModal==wx.Id_OK):
        print "%s " % modal.getStringSelection()
        modal.Destroy()
        
#parent,caption
    #frame.Show()
    #app.MainLoop()
    
        
        
def doME(self,event):
    self.Destroy()
    
        
#also do number 14
