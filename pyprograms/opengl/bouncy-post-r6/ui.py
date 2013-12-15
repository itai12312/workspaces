import sys, pygame
from pygame.locals import *
from OpenGL.GL import *
from OpenGL.GLU import *
from OpenGL.GLUT import *

import fonts, pyglyph

class UserInterface:

    def twod_setup(self, colour=(0,0,0)):
        # set up 2d mode
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        x, y = self.viewport
        glViewport(0, 0, x, y)
        glOrtho(0, x, 0, y, -50, 50)
        glMatrixMode(GL_MODELVIEW)
        colour += (0,)
        glClearColor(*colour)
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
        glLoadIdentity()
        glDisable(GL_LIGHTING)
        glDisable(GL_DEPTH_TEST)


    def ok_dialog(self, text):
        text = pyglyph.layout_text(text, font=fonts.sans40)
        x, y = self.viewport

        self.twod_setup()

        glBegin(GL_QUADS)
        glColor4f(.9, .9, .9, 1)
        glVertex2f(x/2 - 300, y/2-100)
        glVertex2f(x/2 + 300, y/2-100)
        glVertex2f(x/2 + 300, y/2+100)
        glVertex2f(x/2 - 300, y/2+100)
        glEnd()

        pyglyph.begin()
        text.draw(pos=(x/2, y/2),
            anchor=(pyglyph.Align.center, pyglyph.Align.center))
        pyglyph.end()

        pygame.display.flip()

        while 1:
            for e in pygame.event.get():
                if e.type == QUIT: sys.exit()
                if e.type == MOUSEBUTTONDOWN: return
                if not hasattr(e, 'key'): continue
                if e.key == K_RETURN: return
                if e.key == K_ESCAPE: return

    def fade(self, dir, ts, ts_max):
            # fade to black
            glMatrixMode(GL_PROJECTION)
            glLoadIdentity()
            x, y = self.viewport
            glOrtho(0, x, 0, y, -1, 1)
            glMatrixMode(GL_MODELVIEW)
            glDisable(GL_LIGHTING)
            glDisable(GL_DEPTH_TEST)
            glEnable(GL_BLEND)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
            glLoadIdentity()

            glBegin(GL_QUADS)
            if dir == 'out':
                glColor(0, 0, 0, min(1, float(ts)/ts_max))
            else:
                glColor(0, 0, 0, max(0, 1 - float(ts)/ts_max))
            glVertex(0, 0, 0)
            glVertex(0, y, 0)
            glVertex(x, y, 0)
            glVertex(x, 0, 0)
            glEnd()

