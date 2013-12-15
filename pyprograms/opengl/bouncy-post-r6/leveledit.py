import sys, pygame, csv, shutil, os
from pygame.locals import *
from pygame.constants import *

from OpenGL.GL import *
from OpenGL.GLU import *
from OpenGL.GLUT import *

pygame.init()
glutInit(sys.argv[1:])
viewport = (1024, 768)
pygame.display.set_mode(viewport, OPENGL | DOUBLEBUF)

import objects

import pyglyph

class excel_colon(csv.excel):
    delimiter = ':'

class LevelEditor:
    def __init__(self, viewport):
        self.viewport = viewport
        self.width, self.height = viewport

        # set up 2d mode
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        x, y = self.viewport
        glViewport(0, 0, x, y)
        glOrtho(0, x, 0, y, -50, 50)
        glLightfv(GL_LIGHT0, GL_POSITION,  (500, 500, 500, 0.0))
        glLightfv(GL_LIGHT0, GL_AMBIENT, (0.2, 0.2, 0.2, 1.0))
        glLightfv(GL_LIGHT0, GL_DIFFUSE, (0.5, 0.5, 0.5, 1.0))
        glEnable(GL_LIGHT0)
        glEnable(GL_COLOR_MATERIAL)
        glShadeModel(GL_SMOOTH)

        # load the objects
        objects.load()

        self.active_add = None
        self.undo_move = None
        self.mode = 'props'

        # load up fonts
        fonts = pyglyph.font.LocalFontFactory('data')
        self.sans20 = fonts.get_font(family='bitstream vera sans',
            size=20, bold=False, italic=False)

        self.sans40 = fonts.get_font(family='bitstream vera sans',
            size=40, bold=False, italic=False)

        # button labels and callback funcs
        self.buttons = [
            (pyglyph.layout_text(label, font=self.sans20), func,
                    Rect(0, i*32, 120, 32))
            for i, (label, func) in enumerate([
                ('Quit (ESC)', self.do_quit),
                ('Reset (L)', self.do_reset),
                ('Save (S)', self.do_save),
                ('Props (P)', lambda: self.set_mode('props')),
                ('Food (F)', lambda: self.set_mode('food')),
            ])
        ]

        self.prop_buttons = [
            (pyglyph.layout_text(label, font=self.sans20), func,
                    Rect(0, (i+len(self.buttons)+1)*32, 120, 32))
            for i, (label, func) in enumerate([
                ('Fence', lambda: self.set_active_add(objects.Fence)),
                ('Gate', lambda: self.set_active_add(objects.Gate)),
                ('Row', lambda: self.set_active_add(objects.Row)),
                ('Farmer', lambda: self.set_active_add(objects.Farmer)),
                ('Player', lambda: self.set_active_add(objects.Player)),
                ('Tree', lambda: self.set_active_add(objects.Tree)),
                ('Hedge', lambda: self.set_active_add(objects.Hedge)),
                ('Long hedge', lambda: self.set_active_add(objects.LongHedge)),
                ('Scarecrow', lambda: self.set_active_add(objects.Scarecrow)),
                ('Bucket', lambda: self.set_active_add(objects.Bucket)),
                ('Pie', lambda: self.set_active_add(objects.Pie)),
            ])
        ]

        self.food_buttons = [
            (pyglyph.layout_text(label, font=self.sans20), func,
                    Rect(0, (i+len(self.buttons)+1)*32, 120, 32))
            for i, (label, func) in enumerate([
                ('Carrot', lambda: self.set_active_add(objects.Carrot)),
                ('Lettuce', lambda: self.set_active_add(objects.Lettuce)),
                ('Tomato', lambda: self.set_active_add(objects.Tomato)),
            ])
        ]

        self.pick_menu_buttons = [
            (pyglyph.layout_text(label, font=self.sans20), func,
                    Rect(0, i*32, 150, 32))
            for i, (label, func) in enumerate([
                ('Delete', self.delete_pick),
                ('Move', self.move_pick),
                ('Rotate CW', self.rotate_pick_cw),
                ('Rotate CCW', self.rotate_pick_ccw),
            ])
        ]

    def set_mode(self, mode): self.mode = mode
    def set_active_add(self, cls): self.active_add = cls

    def do_quit(self):
        self.running = False

    def do_save(self):
        if os.path.exists(self.filename):
            shutil.copyfile(self.filename, self.filename+'.bak')

        writer = csv.writer(open(self.filename+'.tmp', 'w'), excel_colon)
        writer.writerow(['object', 'position', 'rotation'])
        for object in self.objects:
            writer.writerow([object.name, ','.join(map(str, object.position)),
                object.rotation])
        os.rename(self.filename+'.tmp', self.filename)

        self.ok_dialog('Map saved, press a key')

    def do_reset(self):
        self.objects = []

        if not os.path.exists(self.filename): return

        reader = csv.reader(open(self.filename), excel_colon)
        for name, position, rotation in list(reader)[1:]:
            if name[0] == '#': continue
            position = map(float, position.split(','))
            rotation = float(rotation)
            object = objects.map_elements[name](rotation, position)
            self.objects.append(object)

    def delete_pick(self):
        for i, object in enumerate(self.objects):
            if object is self.picked_obj:
                del self.objects[i]
        self.picked_obj = None
    def move_pick(self):
        self.active_add = self.picked_obj.__class__
        self.obj_rotation = self.picked_obj.rotation
        self.undo_move = self.picked_obj
        self.delete_pick()
    def rotate_pick_cw(self):
        self.picked_obj.rotation -= 90
        self.picked_obj = None
    def rotate_pick_ccw(self):
        self.picked_obj.rotation += 90
        self.picked_obj = None

    def draw_menu(self, menu, hover=None, center=None, click=False):
        if center:
            all = menu[0][2]
            for x, x, rect in menu[1:]:
                all = all.union(rect)
            all.center = center
            all = all.clamp((0, 0, self.width, self.height))
        handled = False
        for label, func, rect in menu:

            if center:
                ox, oy = all.topleft
                rect = Rect(rect)
                rect.x += ox
                rect.y += oy

            if rect.collidepoint(hover):
                glColor4f(1., 1., .7, .6)
                if click:
                    func()
                    handled = True
            else:
                glColor4f(.9, .9, .9, .6)
            glBegin(GL_QUADS)
            glVertex2f(rect.left + 1, self.height - (rect.top + 1))
            glVertex2f(rect.left + rect.width + 1, self.height - (rect.top + 1))
            glVertex2f(rect.left + rect.width + 1, self.height - (rect.top + 31))
            glVertex2f(rect.left + 1, self.height - (rect.top + 31))
            glEnd()

            pyglyph.begin()
            label.draw(pos=(rect.left + 5, self.height - (rect.top + 2)),
                anchor=(pyglyph.Align.left, pyglyph.Align.top))
            pyglyph.end()
        return handled

    def run(self, filename):
        self.filename = filename

        clock = pygame.time.Clock()
        self.running = True
        self.picked_obj = None
        rotation = 0
        scale = 20
        self.obj_rotation = 0

        self.do_reset()

        move = False
        vx, vy = (self.width/2, self.height/2)
        while self.running:
            ts = clock.tick(30)
            turn = 0
            hover = None
            click = menu_click = False
            for e in pygame.event.get():
                if e.type == QUIT: sys.exit()
                elif e.type == MOUSEBUTTONDOWN:
                    if e.button == 1: click = True
                    elif e.button == 2: move = True
                    elif e.button == 3:
                        if self.active_add is not None:
                            self.active_add = None
                            if self.undo_move is not None:
                                self.objects.append(self.undo_move)
                                self.undo_move = None
                        else:
                            menu_click = True
                    elif e.button == 4: scale += 1
                    elif e.button == 5: scale = max(1, scale-1)
                    continue
                elif e.type == MOUSEBUTTONUP:
                    if e.button == 2: move = False
                elif e.type == MOUSEMOTION:
                    i, j = e.rel
                    if move:
                        vx += i
                        vy -= j
                    continue
                if not hasattr(e, 'key'):
                    continue
                if e.key == K_ESCAPE: return
                down = e.type == KEYDOWN
                if e.key == K_LEFT and down: self.obj_rotation += 90
                if e.key == K_RIGHT and down: self.obj_rotation -= 90

            # set up projection
            glMatrixMode(GL_PROJECTION)
            glLoadIdentity()
            x, y = self.viewport
            glViewport(0, 0, x, y)
            glOrtho(0, x, 0, y, -50, 50)
            glMatrixMode(GL_MODELVIEW)

            # init drawing
            glClearColor(0.32, .48, .27, 0)
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
            glLoadIdentity()

            # now into perspective view for the map
            glEnable(GL_DEPTH_TEST)
            glEnable(GL_LIGHTING)

            glPushMatrix()
            glTranslate(vx, vy, 0)
            glScalef(scale, scale, 1)
            glRotate(90, 1, 0, 0)

            # draw objs
            for n, object in enumerate(self.objects):
                object.render()

            if self.active_add is not None and x > 120:
                x, y = pygame.mouse.get_pos()
                w2, h2 = self.width/2, self.height/2
                offx, offy = w2 - vx, h2 - vy
                tx, ty = (x - w2 + offx)/scale, (y - h2 - offy)/scale
                glTranslate(tx, 0, ty)
                glRotate(self.obj_rotation, 0, 1, 0)
                glCallList(self.active_add.obj.gl_list)

                if click:
                    if self.active_add is objects.Player:
                        for i, object in enumerate(self.objects):
                            if isinstance(object, objects.Player):
                                del self.objects[i]
                                break
                    self.objects.append(self.active_add(self.obj_rotation,
                        (tx, 0, ty)))
                    self.active_add = None
                    click = False
            glPopMatrix()

            # draw grid
            glDisable(GL_LIGHTING)
            glEnable(GL_BLEND)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
            glPushMatrix()
            left = -4*scale + vx%(4 * scale)
            top = -4*scale + vy%(4 * scale)
            glColor(1., 1., 1., .3)
            glBegin(GL_LINES)
            for x in range(left, left + self.width  + 4*scale, 4*scale):
                glVertex(x, top, 30)
                glVertex(x, top+self.height + 4*scale, 30)
            for y in range(top, top + self.height + 4*scale, 4*scale):
                glVertex(left, y, 30)
                glVertex(left+self.width + 4*scale, y, 30)
            glEnd()
            glPopMatrix()
            glDisable(GL_BLEND)

            glDisable(GL_DEPTH_TEST)
            x,y = pygame.mouse.get_pos()
            # draw buttons
            if self.draw_menu(self.buttons, hover=(x,y), click=click):
                click = False
                if self.picked_obj is not None:
                    self.picked_obj = None

            # secondary menu
            hover = None
            if self.mode == 'props': secondary = self.prop_buttons
            elif self.mode == 'food': secondary = self.food_buttons
            if self.draw_menu(secondary, hover=(x,y), click=click):
                click = False
                if self.picked_obj is not None:
                    self.picked_obj = None

            # object menu
            if self.picked_obj is not None:
                handled = self.draw_menu(self.pick_menu_buttons, hover=(x,y),
                    center=self.pick_center, click=click)
                if click and not handled:
                    self.picked_obj = None

            # object picking
            if menu_click and self.active_add is None:
                glMatrixMode(GL_PROJECTION)
                glLoadIdentity()
                glInitNames()
                gluPickMatrix(x, self.height-y, 5, 5,
                    (0, 0, self.width, self.height))
                glOrtho(0, self.width, 0, self.height, -50, 50)
                glSelectBuffer(512)
                glRenderMode(GL_SELECT)

                glMatrixMode(GL_MODELVIEW)
                glTranslate(vx, vy, 0)
                glScalef(scale, scale, 1)
                glRotate(90, 1, 0, 0)
                for n, object in enumerate(self.objects):
                    glPushName(n + 1)
                    object.render()

                pick = glRenderMode(GL_RENDER)
                if pick:
                    for a, b, names in pick:
                        pick = self.objects[names[-1] - 1]
                        break
                    self.picked_obj = pick
                    self.pick_center = (x, y)
                else:
                    self.picked_obj = None

            pygame.display.flip()

    def ok_dialog(self, text):
        text = pyglyph.layout_text(text, font=self.sans40)

        # set up 2d mode
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        x, y = self.viewport
        glViewport(0, 0, x, y)
        glOrtho(0, x, 0, y, -50, 50)
        glMatrixMode(GL_MODELVIEW)
        glLoadIdentity()

        glDisable(GL_LIGHTING)
        glDisable(GL_DEPTH_TEST)

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

        clock = pygame.time.Clock()
        while 1:
            ts = clock.tick(10)
            for e in pygame.event.get():
                if e.type == MOUSEBUTTONDOWN: return
                if not hasattr(e, 'key'): continue
                if e.type == KEYDOWN: return
            pygame.display.flip()

if __name__ == '__main__':
    editor = LevelEditor(viewport)
    editor.run(sys.argv[1])

