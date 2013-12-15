import pyglet
platform = pyglet.window.get_platform()
display = platform.get_default_display()
screen = display.get_default_screen()
for config in screen.get_matching_configs(pyglet.gl.Config()):
    if config.aux_buffers or config.accum_red_size:
        print config
