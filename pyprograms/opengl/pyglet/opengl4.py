import pyglet
from pyglet.gl import *
 
win = pyglet.window.Window()
label = pyglet.text.Label('Hello, world',font_name='Times New Roman',font_size=36,x=window.width//2, y=window.height//2,anchor_x='center', anchor_y='center')
@win.event
def on_draw():
    window.clear()
    label.draw()
    """# Clear buffers
    glClear(GL_COLOR_BUFFER_BIT)
    # Draw outlines only
    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
    # Draw some stuff
    glBegin(GL_TRIANGLES)
    glVertex2i(50, 50)
    glVertex2i(75, 100)
    glVertex2i(200, 200)
    glEnd()"""
pyglet.app.run()
