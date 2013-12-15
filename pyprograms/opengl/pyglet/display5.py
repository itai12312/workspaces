import pyglet
from pyglet.gl import *
class CustomGroup(pyglet.graphics.Group):
    def set_state(self):
        glEnable(texture.target)
        glBindTexture(texture.target, texture.id)
    def unset_state(self):
        glDisable(texture.target)
window = pyglet.window.Window()
custom_group = CustomGroup()
batch = pyglet.graphics.Batch()
vertex_list = batch.add(2, pyglet.gl.GL_LINES, custom_group,
    ('v2i', (10, 15, 30, 35)),
    ('c3B', (0, 0, 255, 0, 255, 0))
)
#To remove a vertex list from a batch, call VertexList.delete.
background = pyglet.graphics.OrderedGroup(0)
foreground = pyglet.graphics.OrderedGroup(1)

batch.add(4, GL_QUADS, foreground, 'v2f')
batch.add(4, GL_QUADS, background, 'v2f')
batch.add(4, GL_QUADS, foreground, 'v2f')
batch.add(4, GL_QUADS, background, 'v2f', 'c4B')

sprites = [pyglet.sprite.Sprite(image, batch=batch) for i in range(100)]

@window.event
def on_draw():
    batch.draw()
    #The Batch ensures that the appropriate set_state and unset_state methods are called before and after the vertex lists that use them.
@window.event
def on_resize(width, height):
    #default
    glViewport(0, 0, width, height)
    glMatrixMode(gl.GL_PROJECTION)
    glLoadIdentity()
    glOrtho(0, width, 0, height, -1, 1)
    glMatrixMode(gl.GL_MODELVIEW)
pyglet.app.run()




"""
class TextureEnableGroup(pyglet.graphics.Group):
    def set_state(self):
        glEnable(GL_TEXTURE_2D)

    def unset_state(self):
        glDisable(GL_TEXTURE_2D)

texture_enable_group = TextureEnableGroup()

class TextureBindGroup(pyglet.graphics.Group):
    def __init__(self, texture):
        super(TextureBindGroup, self).__init__(parent=texture_enable_group)
        assert texture.target = GL_TEXTURE_2D
        self.texture = texture

    def set_state(self):
        glBindTexture(GL_TEXTURE_2D, self.texture.id)

    # No unset_state method required.

    def __eq__(self, other):
        return (self.__class__ is other.__class__ and
                self.texture == other.__class__)

batch.add(4, GL_QUADS, TextureBindGroup(texture1), 'v2f', 't2f')
batch.add(4, GL_QUADS, TextureBindGroup(texture2), 'v2f', 't2f')
batch.add(4, GL_QUADS, TextureBindGroup(texture1), 'v2f', 't2f')


A.set_state()
# Draw A vertices
B.set_state()
# Draw B vertices
B.unset_state()
C.set_state()
# Draw C vertices
C.unset_state()
A.unset_state()
for this created textures classes
"""