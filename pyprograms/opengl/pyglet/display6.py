import pyglet
from pyglet.gl import *
print pyglet.version
window = pyglet.window.Window(resizable=True)#,visible=False)
# ... perform some additional initialisation
#window.set_visible()
window.set_minimum_size(320, 200)
window.set_maximum_size(1024, 768)
window.set_size(800, 600)
#screens = display.get_screens()
#window = pyglet.window.Window(fullscreen=True, screens[1])
#window.set_fullscreen(False)
@window.event
def on_resize(width, height):
    print 'The window was resized to %dx%d' % (width, height)

#x, y = window.get_location()
#window.set_location(x + 20, y + 20)