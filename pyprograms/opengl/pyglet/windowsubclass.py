import pyglet

class HelloWorldWindow(pyglet.window.Window):
    def __init__(self):
        super(HelloWorldWindow, self).__init__()

        self.label = pyglet.text.Label('Hello, world!')

    def on_draw(self):
        self.clear()
        self.label.draw()

if __name__ == '__main__':
    window = HelloWorldWindow()
    pyglet.app.run()
    
def start_game():
    def on_key_press(symbol, modifiers):
        print 'Key pressed in game'
        return True

    def on_mouse_press(x, y, button, modifiers):
        print 'Mouse button pressed in game'
        return True

    window.push_handlers(on_key_press, on_mouse_press)

def end_game():
    window.pop_handlers()