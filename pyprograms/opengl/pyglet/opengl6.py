import pyglet
from pyglet.window import key,mouse
window = pyglet.window.Window()

window.push_handlers(pyglet.window.event.WindowEventLogger())

@window.event
def on_mouse_press(x, y, button, modifiers):
    if button == mouse.LEFT:
        print 'The left mouse button was pressed.'
    #if MOD_CTRL==modifiers
    print x," :",y
@window.event
def on_key_press(symbol, modifiers):
    print 'A key was pressed'
    if symbol == key.A:
        print 'The "A" key was pressed.'
    elif symbol == key.LEFT:
        print 'The left arrow key was pressed.'
    elif symbol == key.ENTER:
        print 'The enter key was pressed.'
@window.event
def on_draw():
    window.clear()

pyglet.app.run()