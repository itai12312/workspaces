import pyglet
from pyglet.gl import *
context = pyglet.gl.get_current_context()
object_space = context.object_space
object_space.my_game_objects_loaded = True
print glGetString(GL_VERSION)