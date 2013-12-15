import pyglet
# Disable error checking for increased performance
pyglet.options['debug_gl'] = False
"""
if pyglet.gl.gl_info.have_extension('GL_ARB_shadow'):
    # ... do shadow-related code.
else:
    # ... raise an exception, or use a fallback method
You can also easily check the version of OpenGL:

if pyglet.gl.gl_info.have_version(1,5):
    # We can assume all OpenGL 1.5 functions are implemented.
    nVidia often release hardware with extensions before having them registered officially.
     When you import * from pyglet.gl you import only the registered extensions. 
    You can import the latest nVidia extensions with:


if sys.platform == 'linux2':
    from pyglet.gl.glx import *
    #glxCreatePbuffer(...)
elif sys.platform == 'darwin':
    from pyglet.gl.agl import *
    #aglCreatePbuffer(...)

from pyglet.gl.glext_nv import *

"""
from pyglet.gl import *
print 'Opengl version: %s' % (glGetString(GL_VERSION))
# Direct OpenGL commands to this window.
window = pyglet.window.Window()
"""
@window.event
def on_draw():
    glClear(GL_COLOR_BUFFER_BIT)
    glLoadIdentity()
    glBegin(GL_TRIANGLES)
    glVertex2f(0, 0)
    glVertex2f(window.width, 0)
    glVertex2f(window.width, window.height)
    glEnd()
"""
vertices = [
    0, 0,
    window.width, 0,
    window.width, window.height]
vertices_gl = (GLfloat * len(vertices))(*vertices)

glEnableClientState(GL_VERTEX_ARRAY)
glVertexPointer(2, GL_FLOAT, 0, vertices_gl)
@window.event
def on_draw():
    glClear(GL_COLOR_BUFFER_BIT)
    glLoadIdentity()
    glDrawArrays(GL_TRIANGLES, 0, len(vertices) // 2)
    pyglet.graphics.draw(2, pyglet.gl.GL_POINTS,('v3f', (10.0, 15.0, 0.0, 30.0, 35.0, 0.0)))
    # 4= """numer of vertices """    
    pyglet.graphics.draw_indexed(4, pyglet.gl.GL_TRIANGLES,
    [0, 1, 2, 0, 2, 3],
    ('v2i', (100, 100,
             150, 100,
             150, 150,
             100, 150)))
    #pyglet.graphics.draw(2, pyglet.gl.GL_LINES,
    #('v2i', (10, 15, 30, 35)),
    #('c3B', (0, 0, 255, 0, 255, 0)))
    
    #empty init vertex_list = pyglet.graphics.vertex_list(1024, 'v3f', 'c4B', 't2f', 'n3f')
    #pyglet.graphics.vertex_list_indexed same but with vertiecs indexed
    
    vertex_list = pyglet.graphics.vertex_list(2,
    ('v2i', (20, 25, 40, 45)),
    ('c3B', (0, 100, 255, 100, 255, 0)))
    vertex_list.vertices = [20, 25, 40, 45]
    vertex_list.vertices[:2] = [30, 35]
    vertex_list.colors[:3] = [255, 0, 0]
    # to resize- vertex_list.resize(self,count)
    """"/static"	Data is never or rarely modified after initialisation
    "/dynamic"	Data is occasionally modified (default)
    "/stream"	Data is updated every frame
    example-vertex_list = pyglet.graphics.vertex_list(1024, 'v3f/stream', 'c4B/static')"""
    vertex_list.draw(pyglet.gl.GL_LINES)
    
@window.event
def on_resize(width, height):
    #default
    glViewport(0, 0, width, height)
    glMatrixMode(gl.GL_PROJECTION)
    glLoadIdentity()
    glOrtho(0, width, 0, height, -1, 1)
    glMatrixMode(gl.GL_MODELVIEW)
    """custom
    glViewport(0, 0, width, height)
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    gluPerspective(65, width / float(height), .1, 1000)
    glMatrixMode(GL_MODELVIEW)
    return pyglet.event.EVENT_HANDLED"""
pyglet.app.run()