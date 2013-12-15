class ClankingWidget(pyglet.event.EventDispatcher):
    def clank(self):
        self.dispatch_event('on_clank')

    def click(self, clicks):
        self.dispatch_event('on_clicked', clicks)

    def on_clank(self):
        print 'Default clank handler.'

ClankingWidget.register_event_type('on_clank')
ClankingWidget.register_event_type('on_clicked')

widget = ClankingWidget()

@widget.event
def on_clank():
    pass

@widget.event
def on_clicked(clicks):
    pass

def override_on_clicked(clicks):
    pass

widget.push_handlers(on_clicked=override_on_clicked)

# The subject
class ClockTimer(pyglet.event.EventDispatcher):
    def tick(self):
        self.dispatch_events('on_update')
ClockTimer.register_event('on_update')

# Abstract observer class
class Observer(object):
    def __init__(self, subject):
        subject.push_handlers(self)

# Concrete observer
class DigitalClock(Observer):
    def on_update(self):
        pass

# Concrete observer
class AnalogClock(Observer):
    def on_update(self):
        pass

timer = ClockTimer()
digital_clock = DigitalClock(timer)
analog_clock = AnalogClock(timer)

import sys

class MyDispatcher(object):
    if getattr(sys, 'is_epydoc'):
        def on_update():
            '''The object was updated.

            :event:
            '''
def on_key_press(symbol, modifiers):
    pass

def on_key_release(symbol, modifiers):
    pass
    
    
from pyglet.window import key

window = pyglet.window.Window()
keys = key.KeyStateHandler()
window.push_handlers(keys)

# Check if the spacebar is currently pressed:
if keys[key.SPACE]:
    pass
    
    
    
def on_text_motion(motion):
    pass
    
#mouse moves    
def on_mouse_motion(x, y, dx, dy):
    pass
def on_mouse_press(x, y, button, modifiers):
    pass

def on_mouse_release(x, y, button, modifiers):
    pass

def on_mouse_drag(x, y, dx, dy, buttons, modifiers):
    pass
#button can be pyglet.window.mouse.LEFT/MIDDLE/RIGHT
def on_mouse_drag(x, y, dx, dy, buttons, modifiers):
    if buttons & mouse.LEFT:
        pass
def on_mouse_enter(x, y):
    pass

def on_mouse_leave(x, y):
    pass
def on_mouse_scroll(x, y, scroll_x, scroll_y):
    pass
    
    
cursor = window.get_system_mouse_cursor(win.CURSOR_HELP)
window.set_mouse_cursor(cursor)
#or
image = pyglet.image.load('cursor.png')
cursor = pyglet.window.ImageMouseCursor(image, 16, 8)
window.set_mouse_cursor(cursor)


def update(dt):#dt=time passed
    # ...
pyglet.clock.schedule_interval(update, 0.1)#run every 0.1 second


If you are writing a benchmarking program or otherwise wish to simply run at the highest possible frequency, use schedule:
def update(dt):
    # ...
pyglet.clock.schedule(update)

def dismiss_dialog(dt):
    # ...

# Dismiss the dialog after 5 seconds.
pyglet.clock.schedule_once(dismiss_dialog, 5.0)



