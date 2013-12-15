import pyglet

#config = pyglet.gl.Config(alpha_size=8)
#window = pyglet.window.Window(config=config)

#or this
""" platform = pyglet.window.get_platform()
display = platform.get_default_display()
screen = display.get_default_screen()

template = pyglet.gl.Config(alpha_size=8)
config = screen.get_best_config(template)
context = config.create_context(None)
window = pyglet.window.Window(context=context)
"""
#or this
"""template = gl.Config(sample_buffers=1, samples=4)
try:
    config = screen.get_best_config(template)
except pyglet.window.NoSuchConfigException:
    template = gl.Config()
    config = screen.get_best_config(template)
window = pyglet.window.Window(config=config)
"""

window = pyglet.window.Window()
context = window.context
config = context.config
print config.double_buffer
print config.stereo
print config.sample_buffers
platform = pyglet.window.get_platform()
display = platform.get_default_display()
for screen in display.get_screens():
    print screen