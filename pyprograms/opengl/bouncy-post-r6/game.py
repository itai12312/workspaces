# Basic OBJ file viewer. needs objloader from:
#  http://www.pygame.org/wiki/OBJFileLoader
# LMB + move: rotate
# RMB + move: pan
# Scroll wheel: zoom in/out
import sys, os, pygame, operator
from pygame.locals import *
from OpenGL.GL import *
from OpenGL.GLU import *
from OpenGL.GLUT import *

# init before we get any further
pygame.init()
glutInit(sys.argv[1:])
viewport = (1024, 768)
screen = pygame.display.set_mode(viewport, OPENGL | DOUBLEBUF) # | FULLSCREEN)

from map import *
import ui, objects, textures, rabbit, farmer, fonts
import euclid, pyglyph

class Game(ui.UserInterface):
    def __init__(self, viewport):
        self.viewport = viewport
        hx = self.viewport[0]/2
        hy = self.viewport[1]/2
        pygame.mouse.set_visible(False)

        glLightfv(GL_LIGHT0, GL_POSITION,  (100, 200, 100, 0.0))
        glLightfv(GL_LIGHT0, GL_AMBIENT, (0.2, 0.2, 0.2, 1.0))
        glLightfv(GL_LIGHT0, GL_DIFFUSE, (0.5, 0.5, 0.5, 1.0))
        glEnable(GL_LIGHT0)
        glEnable(GL_LIGHTING)
        glEnable(GL_COLOR_MATERIAL)
        glEnable(GL_DEPTH_TEST)
        glShadeModel(GL_SMOOTH)

        # now we're initialised, load everything up
        objects.load()
        textures.load()
        rabbit.Rabbit.load()
        farmer.Farmer.load()

    def menu(self):

        instructions = pyglyph.layout_html('''
            <font face="bitstream vera sans" size="40">
                (I)nstructions <br>
                (E)asy <br>
                (N)ormal <br>
                (H)ard <br>
            Escape to quit</font>''', font_factory=fonts.fonts)

        level = 1

        clock = pygame.time.Clock()
        timeout = 0
        start = False
        r = rabbit.Rabbit((-10, 0, 0), 90)
        while 1:
            ts = clock.tick(60)
            timeout += ts
            for e in pygame.event.get():
                if e.type == QUIT: sys.exit()
                if not hasattr(e, 'key'): continue
                if not e.type == KEYDOWN: continue
                if e.key == K_i:
                    self.instructions()
                if e.key in (K_e, K_n, K_h):
                    if e.key == K_e: difficulty = 1
                    elif e.key == K_n: difficulty = 2
                    else: difficulty = 3
                    if timeout > 500:
                        start = True
                        level = 1
                        timeout = 0
                if e.key == K_ESCAPE: return

            self.twod_setup(colour=(0.52, .78, .47))
            glEnable(GL_DEPTH_TEST)

            x, y = self.viewport

            glEnable(GL_BLEND)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
            glPushMatrix()
            tex = textures.textures['bouncy-title']
            glTranslate(x/2 - tex.width/2, y-tex.height, 0)
            tex.render()
            glPopMatrix()

            #glDisable(GL_BLEND)
            pyglyph.begin()
            instructions.draw(pos=(x/2, y - tex.height),
                anchor=(pyglyph.Align.center, pyglyph.Align.top))
            pyglyph.end()

            # rabbit
#            glEnable(GL_LIGHTING)
#            glEnable(GL_BLEND)
#            if r.position.x > 20:
#                r.position.x = -20
#            if r.state is r.ST_SITTING:
#                r.move(euclid.Vector3(.5, 0, 0))
#            r.animate(ts, None)
#            glTranslate(x/2, y/2, -10)
#            glScalef(30, 30, 1)
#            glRotate(90, 1, 0, 0)
#            r.render()
#            glDisable(GL_LIGHTING)

            if start:
                if timeout < 300:
                    self.fade('out', timeout, 300)
                else:
                    start = False
                    while 1:
                        if not os.path.exists('data/level%s.csv'%level):
                            self.ok_dialog('You finished the game!')
                            level = 1
                            break
                        elif level > 1:
                            self.ok_dialog('Level complete!')
                        map = Map(self.viewport, 'data/level%s.csv'%level)
                        if not map.play(difficulty): break
                        level += 1
                    timeout = 0
            elif timeout < 300:
                self.fade('in', timeout, 300)

            pygame.display.flip()

    def instructions(self):
        instructions = pyglyph.layout_html('''
            <font face="bitstream vera sans" size="20">
<b>Bouncy the Rabbit by Richard Jones</b>
<br>
Written for the 3rd PyWeek Challenge: http://www.pyweek.org/
<br><br>
<b>YOUR MISSION:</b>
<br>
You are a hungry rabbit. Eat food to complete the level.
<br><br>
<b>CONTROLS:</b>
<br>
arrows   - move around
<br>
"e"    - eat whatever's in front of you
<br>
"d"      - dig into the ground (hold to go underground)
           also, when underground, "d"ig to surface
<br>
escape   - quit
<br><br>
Dig for long enough and you'll go into the hole.
<br>
Move around under ground - fences no longer stop you and the farmer can't
see you.
<br>
Hold "d" again to surface.
<br><br>
Press any key to return to menu.
            </font>''', font_factory=fonts.fonts)

        clock = pygame.time.Clock()
        while 1:
            ts = clock.tick(10)
            for e in pygame.event.get():
                if e.type == QUIT: sys.exit()
                if hasattr(e, 'key') and e.type == KEYDOWN: return
            self.twod_setup(colour=(0.52, .78, .47))
            x, y = self.viewport
            pyglyph.begin()
            instructions.draw(pos=(0, y),
                anchor=(pyglyph.Align.left, pyglyph.Align.top))
            pyglyph.end()
            pygame.display.flip()

if __name__ == '__main__':
    game = Game(viewport)
    game.menu()
#    map = Map(viewport, 'data/test_level.csv')
#    map = Map(viewport, 'data/level2.csv')
#    map.play(1)

