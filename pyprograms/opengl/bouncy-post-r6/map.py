import math, csv, sys, time, os
import pygame
from pygame.locals import *
from OpenGL.GL import *
from OpenGL.GLU import *

import euclid, collide, objects, pyglyph, fonts
import rabbit, farmer, astar, ui, collide, textures

class excel_colon(csv.excel):
    delimiter = ':'
class Map(ui.UserInterface):
    farmer = None
    def __init__(self, viewport, filename):
        self.viewport = viewport
        self.grid = astar.AStarGrid()

        reader = csv.reader(open(filename), excel_colon)
        self.holes = []
        self.pieces = []
        for name, position, rotation in list(reader)[1:]:
            if name[0] == '#': continue
            position = map(float, position.split(','))
            rotation = float(rotation)

            if name == 'player':
                self.rabbit = rabbit.Rabbit(position, rotation)
            elif name == 'farmer':
                self.farmer = farmer.Farmer(position, rotation)
            else:
                elem = objects.map_elements[name](rotation, position)
                self.pieces.append(elem)
                self.grid.add(elem)

        # add the farmer so it owns its little slice of the universe
        if self.farmer is not None:
            self.grid.addFarmer(self.farmer)

        #print self.grid

    def animate(self, ts):
        pass

    def is_intersecting(self, hitbox):
        l = []
        if isinstance(hitbox, collide.Sphere) and hitbox.c.y < hitbox.r:
            l.append(self)
        elif isinstance(hitbox, collide.AABox) and hitbox.c.y < hitbox.dy/2:
            l.append(self)
        for piece in self.pieces:
            if (piece.hitbox is not None and
                    piece.hitbox.is_intersecting(hitbox)):
                l.append(piece.hitbox)
        return l

    def resolve_collision(self, hitbox):
        # don't fall through the ground
        if isinstance(hitbox, collide.Sphere):
            hitbox.c.y = hitbox.r
            obj.velocity.y = 0
        elif isinstance(hitbox, collide.AABox):
            hitbox.c.y = hitbox.dy/2
        return hitbox.c, euclid.Vector3(1, 0, 1)

    def find_food(self, other):
        c = other.c
        r = other.r
        l = []
        for piece in self.pieces:
            if not piece.is_food: continue
            distance = abs(other.c - piece.hitbox.c) - r - 1 # TODO: 2 is magic
            if distance < 0:
                l.append((distance, piece))
        if not l:
            return None
        l.sort()
        return l[0][1]

    def eat_food(self, food):
        i = self.pieces.index(food)
        next = food.next
        if next is None:
            del self.pieces[i]
        else:
            self.pieces[i] = next(food.rotation, food.position)

    def add_hole(self, position):
        self.holes.append(objects.Hole(0, position))

    def render(self, show_collide=False):
        if show_collide:
            s = float(self.grid.resolution)/2
            glDisable(GL_LIGHTING)
            for x, z in self.grid.m.keys():
                if self.grid.m[(x, z)] is self.grid.IMPASSABLE:
                    glColor(1, 0, 0, .5)
                else:
                    glColor(0, 1, 0, .5)
                glPushMatrix()
                glTranslate(x+s, 0, z+s)
                glBegin(GL_QUADS)
                glVertex(-s,0,-s)
                glVertex(-s,0,s)
                glVertex(s,0,s)
                glVertex(s,0,-s)
                glEnd()
                glPopMatrix()

            glColor(1, 1, 0, .5)
            glPushMatrix()
            x, z = self.grid.findClosest(self.rabbit.position.x,
                self.rabbit.position.z)
            glTranslate(x+s, 0, z+s)
            glBegin(GL_QUADS)
            glVertex(-s,1,-s)
            glVertex(-s,1,s)
            glVertex(s,1,s)
            glVertex(s,1,-s)
            glEnd()
            glPopMatrix()

            if self.farmer is not None:
                glColor(0, 1, 1, .5)
                glPushMatrix()
                x, z = self.grid.findClosest(self.farmer.position.x,
                    self.farmer.position.z)
                glTranslate(x+s, 0, z+s)
                glBegin(GL_QUADS)
                glVertex(-s,1,-s)
                glVertex(-s,1,s)
                glVertex(s,1,s)
                glVertex(s,1,-s)
                glEnd()
                glPopMatrix()
            glEnable(GL_LIGHTING)

        if 1:
            for piece in self.pieces:
                piece.render(show_collide)
            for hole in self.holes:
                hole.render(show_collide)

    def setup_scene(self, overhead=False, clear=(0.32, .48, .27)):
        # set up camera
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        width, height = self.viewport
        gluPerspective(60.0, width/float(height), 1, 100.0)
        p = self.rabbit.position
        if overhead:
            glRotate(90, 1, 0, 0)
            glTranslate(-p.x, -80, -p.z)
        else:
            glRotate(45, 1, 0, 0)
            glTranslate(-p.x, -25, -p.z-15)

        # now draw everything
        glMatrixMode(GL_MODELVIEW)
        clear += (0,)
        glClearColor(*clear)
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
        glLoadIdentity()

        glEnable(GL_LIGHTING)
        glEnable(GL_DEPTH_TEST)

    def play(self, difficulty):
        if difficulty == 1:
            self.rabbit.hunger = {'lettuce': 8., 'carrot': 8., 'tomato': 8.}
            self.max_hunger = {'lettuce': 8., 'carrot': 8., 'tomato': 8.}
        elif difficulty == 2:
            self.rabbit.hunger = {'lettuce': 12., 'carrot': 12., 'tomato': 12.}
            self.max_hunger = {'lettuce': 12., 'carrot': 12., 'tomato': 12.}
        else:
            self.rabbit.hunger = {'lettuce': 16., 'carrot': 16., 'tomato': 16.}
            self.max_hunger = {'lettuce': 16., 'carrot': 16., 'tomato': 16.}
        if self.farmer is not None:
            self.farmer.setDifficult(difficulty)

        #glClearColor(0, 0, 0, 0)
        #glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
        #self.ok_dialog('Get Ready!')

        rx, ry = (0,0)
        tx, ty = (0,0)
        zpos = 6
        rotate = move = False
        clock = pygame.time.Clock()
        k_right = k_left = k_up = k_down = 0
        move = dig = eat = False
        show_collide = False
        overhead = False
        paused = False
        jpos = [0,0]
        while 1:
            ts = clock.tick(60)
            turn = 0
            for e in pygame.event.get():
                if e.type == QUIT: sys.exit()
                down = e.type == KEYDOWN

                if e.type == JOYAXISMOTION:
                    jpos[e.axis] = e.value
                    k_right = k_left = k_up = k_down = 0
                elif e.type == JOYBUTTONDOWN:
                    if e.button == 1: dig = True
                    elif e.value == 2: eat = True
                    elif e.value == 3: overhead = not overhead
                    elif e.value == 4: paused = not paused
                elif e.type == JOYBUTTONUP:
                    if e.value == 1: dig = False
                    elif e.value == 2: eat = False

                if jpos[0] < -.5: k_left = True
                elif jpos[0] > .5: k_right = True
                if jpos[1] < -.5: k_up = True
                elif jpos[1] > .5: k_down = True

                if not hasattr(e, 'key'): continue
                if e.key == K_ESCAPE: return False
                
                if e.key == K_UP: k_up = down
                if e.key == K_RIGHT: k_right = down
                if e.key == K_LEFT: k_left = down
                if e.key == K_DOWN: k_down = down
                if e.key == K_d: dig = down
                if e.key == K_o and down: overhead = not overhead
                if e.key == K_p and down: paused = not paused
                if e.key == K_e: eat = down
                if down and e.key == K_h: show_collide = not show_collide

            if not paused:
                move = [euclid.Vector3(0, 0, 0)]
                if k_right: move.append(euclid.Vector3(1, 0, 0))
                if k_left: move.append(euclid.Vector3(-1, 0, 0))
                if k_up: move.append(euclid.Vector3(0, 0, -1))
                if k_down: move.append(euclid.Vector3(0, 0, 1))
                if move:
                    if len(move) > 1:
                        m = move[0]
                        for n in move[1:]: m += n
                        self.rabbit.move(m)
                    else:
                        self.rabbit.move(move[0])

                if dig: self.rabbit.dig()
                else: self.rabbit.stop_dig()
                if eat: self.rabbit.eat()
                else: self.rabbit.stop_eat()

                # ANIMATE
                self.animate(ts)
                try:
                    self.rabbit.animate(ts, self)
                except self.rabbit.RabbitFed:
                    return True
                if self.farmer is not None:
                    try:
                        self.farmer.animate(ts, self)
                    except self.farmer.RabbitCaught:
                        self.caught_anim()
                        return False

            # SETUP OGL
            self.setup_scene(overhead)

            # RENDER
            if objects.toon_program is not None:
                objects.shaders.glUseProgramObjectARB(objects.toon_program)
            self.render(show_collide)
            self.rabbit.render(show_collide)
            if self.farmer is not None:
                self.farmer.render(show_collide)
            if objects.toon_program is not None:
                objects.shaders.glUseProgramObjectARB(0)

            self.render_hud(show_collide and clock.get_fps())

            pygame.display.flip()

    def caught_anim(self):
        clock = pygame.time.Clock()
        timeout = 0

        pie = objects.Pie(-90, (0,0,0))
        exiting = False
        while 1:
            ts = clock.tick(60)
            turn = 0
            for e in pygame.event.get():
                if e.type == QUIT: sys.exit()
                if not hasattr(e, 'key'): continue
                if timeout > 1000:
                    exiting = True
                    timeout = 0

            if timeout > 1000 and exiting:
                return

            # RENDER
            if timeout < 1000 and not exiting:
                self.setup_scene()
                self.render()
                self.rabbit.render()
                self.farmer.render()
            else:
                self.rabbit.position = euclid.Point3(0, 0, 0)
                self.setup_scene(clear=(0,0,0))
                glLoadIdentity()
                p = self.rabbit.position
                pie.position = euclid.Vector3(p.x/3., 4, p.z/3.+3)
                glScalef(3, 3, 3)
                glRotate(-15, 1, 0, 0)
                pie.render()
                pie.rotation += 1

            timeout += ts
            if timeout < 1500 or exiting:
                self.fade('out', timeout, 750)
            else:
                self.fade('in', timeout-1500, 1000)

            pygame.display.flip()

    def render_hud(self, fps=None):
        # set up 2d mode
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        x, y = self.viewport
        glOrtho(0, x, 0, y, -20, 20)
        glMatrixMode(GL_MODELVIEW)
        glLoadIdentity()

        glDisable(GL_LIGHTING)
        glDisable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        glColor4f(.5, .5, .5, .5)
        glBegin(GL_QUADS)
        glVertex(0, 0, 5)
        glVertex(404, 0, 5)
        glVertex(404, 138, 5)
        glVertex(0, 138, 5)
        glEnd()

        glPushMatrix()
        glColor4f(1, 1, 1, 1)
        glTranslate(10, 0, 10)
        glScalef(.5, .5, 0)
        for i, (name, value) in enumerate(self.rabbit.hunger.items()):
            target = self.max_hunger[name]
            ratio = value / float(target)
            textures.textures[name].render(1 - ratio)
            glTranslate(256, 0, 0)
        glPopMatrix()

        if fps:
            text = pyglyph.layout_text(str(int(fps)), font=fonts.mono20)
            pyglyph.begin()
            text.draw(pos=(0, y),
                anchor=(pyglyph.Align.top, pyglyph.Align.left))
            pyglyph.end()

        glDisable(GL_BLEND)
        glEnable(GL_LIGHTING)
        glEnable(GL_DEPTH_TEST)

