import math, os
import euclid, objloader, collide, objects
from OpenGL.GL import *
from OpenGL.GLUT import *

class Farmer:
    def load(cls):
        cls.models = {}
        for name in ['farmer', 'alert', 'question']:
            obj = objloader.OBJ(os.path.join('data', '%s.obj'%name),
                outline=objects.toon_program is not None)
            cls.models[name] = obj
    load = classmethod(load)

    ST_STANDING = 'State: STANDING'
    ST_SEARCHING = 'State: SEARCHING'
    ST_ALARMED = 'State: ALARMED'
    ST_MOVING = 'State: MOVING'
    ST_OUTSIDE = 'State: RABBIT OUTSIDE'

    hit_offset = euclid.Vector3(0, 5, 0)
    def __init__(self, position, rotation):
        self.position = euclid.Point3(*position)
        self.velocity = euclid.Vector3(0, 0, 0)
        self.destination = None
        self.direction = rotation
        self.model = self.models['farmer']
        self.state = self.ST_SEARCHING
        self.hitbox = collide.AABox(self.position + self.hit_offset,
            2., 10., 2.)
        self.hunger = 0
        self.path = None

    def setDifficult(self, difficulty):
        self.speed = .05 * difficulty

    class RabbitCaught:
        pass


    GRAVITY = 1.5/1000.
    def animate(self, ts, level):
        # gravity is a constant
        self.velocity.y -= self.GRAVITY * ts

        # needed since there's no global collision detection used on farmer
        if self.position.y < 0:
            self.velocity.y = 0

        # no gravity for the farmer
        if self.state is self.ST_OUTSIDE:
            raise NotImplementedError

        rabbit = level.rabbit

        # raise endpoint for intersection
        if (not rabbit.is_underground() and 
                rabbit.hitbox.is_intersecting(self.hitbox)):
            raise self.RabbitCaught

        if self.state is self.ST_SEARCHING:
            self.direction += 5
            if self.direction > 360: self.direction -= 360
            if rabbit.is_underground(): return
            r = euclid.Matrix4.new_rotatey(self.direction * math.pi / 180)
            dir = r * euclid.Vector3(0, 0, 1)
            rvec = (rabbit.position - self.position).normalize()
            angle = dir.dot(rvec)
            if angle > 0.9:
                self.velocity.y = .5
                self.state = self.ST_ALARMED
                self.alarm_count = 1000.
            return

        if rabbit.is_underground():
            self.velocity = euclid.Vector3(0, 0, 0)
            self.state = self.ST_SEARCHING
            return   # NO MOVE

        if self.state is self.ST_ALARMED:
            self.alarm_count -= ts
            self.velocity += euclid.Vector3(0, .0001, 0)
            if self.alarm_count < 0:
                self.state = self.ST_MOVING

        elif self.state is self.ST_MOVING:
            new_dest = None

            # determine rabbit & farmer positions in A* land
            rabbit_pos = level.grid.findClosest(rabbit.position.x,
                rabbit.position.z)
            self_pos = level.grid.findClosest(self.position.x,
                self.position.z)

            if self_pos == rabbit_pos:
                raise self.RabbitCaught

            if self.path is None:
                #print 'PATH is NONE'
                self.path = level.grid.findPath(self_pos, rabbit_pos)
                if self.path is None:
                    self.state = self.ST_OUTSIDE
                    return   # NO MOVE
                new_dest = self.path.nodes.pop().l
            else:
                if self.path:
                    end = self.path.nodes[0].l
                else:
                    end = self.destination
                if rabbit_pos != end:
                    # little bugger moved!
                    #print 'RABBIT MOVED'
                    self.path = level.grid.findPath(self_pos, rabbit_pos)
                    if self.path is None:
                        print 'RABBIT MOVED and we have no path'
                        self.state = self.ST_OUTSIDE
                        return   # NO MOVE
                    new_dest = self.path.nodes.pop().l

            # set destination to next path point?
            if new_dest is None and self.dest_sp.intersect(self.position):
                # is the path empty?
                if not self.path:
                    #print 'EMPTY PATH'
                    self.path = level.grid.findPath(self_pos, rabbit_pos)
                    if self.path is None:
                        print 'destination and no path'
                        self.state = self.ST_OUTSIDE
                        return   # NO MOVE
                new_dest = self.path.nodes.pop().l

            # set the dest and the end-check sphere
            if new_dest is not None:
                s = level.grid.resolution / 2
                self.destination = euclid.Point3(new_dest[0]+s, 0,
                    new_dest[1]+s)
                self.dest_sp = euclid.Sphere(euclid.Point3(new_dest[0]+s,
                    -.5, new_dest[1]+s), 1.0)

            # figure velocity
            self.velocity = (self.destination - self.position).normalize()
            self.velocity *= self.speed

        if not abs(self.velocity):
            return

        # move according to velocity
        old_pos = self.position.copy()
        p = self.position + self.velocity
        self.position = euclid.Point3(p.x, p.y, p.z)
        self.hitbox.c = self.position + self.hit_offset

        if self.velocity.x or self.velocity.z:
            # update direction based on movement vector
            v1 = euclid.Vector3(self.velocity.x, 0, self.velocity.z).normalize()
            rad = self.direction/180*math.pi
            v2 = euclid.Vector3(math.sin(rad), 0, math.cos(rad))
            v3 = v1.cross(v2)
            a1 = ((math.atan2(v1.x, v1.z) * 180 / math.pi) + 360 ) % 360
            a2 = ((math.atan2(v2.x, v2.z) * 180 / math.pi) + 360 ) % 360
            a = min(5, abs(a2-a1))
            if v3.y < 0:
                self.direction += a
                if self.direction > 360: self.direction -= 360
            else:
                self.direction -= a
                if self.direction < 0: self.direction += 360

        # handle collision with the level
#        l = level.is_intersecting(self.hitbox)
#        if l:
#            # back-track
#            self.position = old_pos
#            self.hitbox.c = self.position + self.hit_offset
#
#            # now resolve
#            for hitbox in l:
#                hitbox.resolve_collision(self)
#            p = self.hitbox.c - self.hit_offset
#            self.position = euclid.Point3(p.x, p.y, p.z)

    def render(self, show_collide=False):

        if show_collide:
            self.hitbox.render()
            if self.path:
                s = 2           # XXX hard-coding half grid resolution
                glDisable(GL_DEPTH_TEST)
                glColor(1, 0, 0)
                glBegin(GL_LINE_STRIP)
                for node in self.path.nodes:
                    glVertex(node.l[0] + s, 0, node.l[1] + s)
                glEnd()
                glEnable(GL_DEPTH_TEST)

        else:
            glPushMatrix()
            glTranslate(self.position.x, self.position.y, self.position.z)
            glRotate(self.direction, 0, 1, 0)
            glCallList(self.model.gl_list)

            if self.state is self.ST_ALARMED:
                glPushMatrix()
                glDisable(GL_LIGHTING)
                glTranslate(0, 13, 0)
                glScalef(4, 4, 4)
                glCallList(self.models['alert'].gl_list)
                glEnable(GL_LIGHTING)
                glPopMatrix()

            if self.state is self.ST_SEARCHING:
                glPushMatrix()
                glDisable(GL_LIGHTING)
                glTranslate(0, 13, 0)
                glScalef(4, 4, 4)
                glCallList(self.models['question'].gl_list)
                glEnable(GL_LIGHTING)
                glPopMatrix()

            glPopMatrix()

