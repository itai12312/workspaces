# -*- coding: UTF-8 -*- 
sprite = pyglet.sprite.Sprite(image)
sprite.dx = 10.0

def update(dt):
    sprite.x += sprite.dx * dt
pyglet.clock.schedule_interval(update, 1/60.0) # update at 60Hz

fps_display = pyglet.clock.ClockDisplay()

@window.event
def on_draw():
    window.clear()
    fps_display.draw()
    

    
window = pyglet.window.Window()
label = pyglet.text.Label('Hello, world',
                          font_name='Times New Roman',
                          font_size=36,
                          x=window.width//2, y=window.height//2,
                          anchor_x='center', anchor_y='center')

@window.event
def on_draw():
    window.clear()
    label.draw()

pyglet.app.run()


""" editable text?
import pyglet
window = pyglet.window.Window()
document = pyglet.text.document.FormattedDocument()
layout = pyglet.text.layout.IncrementalTextLayout(document, width, height)
caret = pyglet.text.caret.Caret(layout)
window.push_handlers(caret)
"""