import math, os
import euclid, objloader, collide, objects
from OpenGL.GL import *
from OpenGL.GLUT import *

class Rabbit:
    def load(cls):
        cls.models = {}
        for name in ['rabbit-sitting', 'rabbit-hopping', 'rabbit-landing',
                'bump', 'rabbit-dig1', 'rabbit-dig2']:
            obj = objloader.OBJ(os.path.join('data', '%s.obj'%name),
                outline=objects.toon_program is not None)
            cls.models[name] = obj
    load = classmethod(load)

    ST_SITTING = 'State: SITTING'
    ST_HOPPING = 'State: HOPPING'
    ST_DIGGING = 'State: DIGGING'
    ST_EATING = 'State: EATING'
    ST_UNDERGROUND = 'State: UNDERGROUND'
    ST_SURFACING = 'State: SURFACING'

    hit_offset = euclid.Vector3(0, 1.5, 0)
    def __init__(self, position, rotation):
        self.position = euclid.Point3(*position)
        self.velocity = euclid.Vector3(0, 0, 0)
        self.direction = rotation
        self.model = self.models['rabbit-sitting']
        self.state = self.ST_SITTING
        self.hitbox = collide.AABox(self.position + self.hit_offset,
            2, 3., 2)
        self.hunger = {}
        self.eating = None

    def is_underground(self):
        return (self.state is self.ST_UNDERGROUND or
            self.state is self.ST_SURFACING)

    def move(self, move):
        if self.state is self.ST_HOPPING:
            mag = abs(euclid.Vector2(self.velocity.x, self.velocity.z))
            desired = euclid.Vector2(move.x, move.z).normalize()
            result = desired * mag
            self.velocity.x = result.x
            self.velocity.z = result.y
            if abs(result):
                self.direction = math.atan2(result.x, result.y) * 180 / math.pi
            return

        if not abs(move):
            return

        impulse = move * .15
        self.direction = math.atan2(move.x, move.z) * 180 / math.pi
        if self.state is self.ST_SITTING:
            self.model = self.models['rabbit-hopping']
            self.state = self.ST_HOPPING
            self.velocity += impulse + euclid.Vector3(0, .3, 0)
            self.position += self.velocity
            self.hitbox.c = self.position + self.hit_offset
        elif self.state is self.ST_UNDERGROUND:
            self.position += impulse
            self.hitbox.c = self.position + self.hit_offset

    def dig(self):
        if self.state is self.ST_UNDERGROUND:
            self.state = self.ST_SURFACING
            self.surface_time = 0

        elif self.state is self.ST_SITTING:
            self.state = self.ST_DIGGING
            self.model = self.models['rabbit-dig1']
            self.dig_time = 0

    def stop_dig(self):
        if self.state is self.ST_DIGGING:
            self.model = self.models['rabbit-sitting']
            self.state = self.ST_SITTING
        if self.state is self.ST_SURFACING:
            self.state = self.ST_UNDERGROUND

    def eat(self):
        if self.state is self.ST_SITTING:
            self.state = self.ST_EATING
            self.eat_time = 0

    def stop_eat(self):
        if self.state is self.ST_EATING:
            self.state = self.ST_SITTING

    class RabbitFed:
        pass

    GRAVITY = 1.5/1000.
    def animate(self, ts, level):
        # gravity is a constant
        self.velocity.y -= self.GRAVITY * ts

        if self.state is self.ST_HOPPING:
            if self.velocity.y <= 0:
                self.model = self.models['rabbit-landing']

        elif self.state is self.ST_EATING:
            food = level.find_food(self.eat_sphere)
            if food is not None and food.name.split('-')[0] in self.hunger:
                food_name = food.name.split('-')[0]
                if self.eating is not food:
                    self.eating = food
                    self.eat_time = 0
                else:
                    self.eat_time += 1
                if self.eat_time > 10:
                    self.eat_time = 0
                    level.eat_food(food)
                    self.hunger[food_name] = max(0,
                        self.hunger[food_name] - food.food_value)
                    for value in self.hunger.values():
                        if value > 0: break
                    else:
                        raise self.RabbitFed

        elif self.state is self.ST_DIGGING:
            self.dig_time += ts
            if self.dig_time%300 > 100:
                self.model = self.models['rabbit-dig2']
            else:
                self.model = self.models['rabbit-dig1']

            if self.dig_time > 300:
                self.dig_time += ts
                r = euclid.Matrix4.new_rotatey(self.direction * math.pi / 180)
                hole = r * euclid.Vector3(0., 0., 1.)
                level.add_hole(self.position + hole)

            if self.dig_time > 1200:
                self.state = self.ST_UNDERGROUND
                self.model = self.models['bump']
                r = euclid.Matrix4.new_rotatey(self.direction * math.pi / 180)
                bump = r * euclid.Vector3(0., 0., 1.)
                self.position += bump

        elif self.state is self.ST_UNDERGROUND:
            # no collision detection
            # TODO: collide with streams
            return

        elif self.state is self.ST_SURFACING:
            self.surface_time += ts
            if self.surface_time > 500:
                r = euclid.Matrix4.new_rotatey(self.direction *
                    math.pi / 180)
                hole = r * euclid.Vector3(0., 0., 1.)
                level.add_hole(self.position + hole)
                self.state = self.ST_SITTING
                self.model = self.models['rabbit-sitting']

        # move
        old_pos = self.position.copy()
        self.position = self.position + self.velocity
        self.hitbox.c = self.position + self.hit_offset

        # no level - simple animation
        if level is None:
            if self.position.y < 0:
                self.state = self.ST_SITTING
                self.position.y = 0
                self.velocity = euclid.Vector3(0., 0., 0.)
            return

        # handle collision with the level
        l = level.is_intersecting(self.hitbox)
        vy = self.velocity.y
        resting = False
        if l:
            # back-track
            self.position = old_pos
            self.hitbox.c = self.position + self.hit_offset

            # now resolve
            for hitbox in l:
                self.hitbox.c, vmod = hitbox.resolve_collision(self.hitbox)
                self.velocity = self.velocity * vmod
                if vy < 0 and vmod.y == 0: resting = True

            # now use velocity
            self.hitbox.c = self.hitbox.c + self.velocity

            # set rabbit center using hitbox offset
            p = self.hitbox.c - self.hit_offset
            self.position = euclid.Point3(p.x, p.y, p.z)

        # if we're hopping but we've not moved then revert to sitting
        if resting and self.state is self.ST_HOPPING:
            self.velocity = euclid.Vector3(0., 0., 0.)
            self.state = self.ST_SITTING
            self.model = self.models['rabbit-sitting']

        # place the eating sphere
        r = euclid.Matrix4.new_rotatey(self.direction * math.pi / 180)
        eat_vec = r * euclid.Vector3(0., 1., 2.)
        self.eat_sphere = euclid.Sphere(self.position + eat_vec, 2.)

    def render(self, show_collide=False):
        if show_collide:
            self.hitbox.render()
            glPushMatrix()
            glTranslate(self.eat_sphere.c.x, self.eat_sphere.c.y,
                self.eat_sphere.c.z)
            glColor(.5, .5, 1)
            glutWireSphere(self.eat_sphere.r, 8, 8)
            glPopMatrix()
        else:
            glPushMatrix()
            glTranslate(self.position.x, self.position.y, self.position.z)
            glRotate(self.direction, 0, 1, 0)
            glCallList(self.model.gl_list)
            glPopMatrix()

