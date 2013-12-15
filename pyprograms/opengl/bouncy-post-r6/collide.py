import euclid

from OpenGL.GL import *
from OpenGL.GLUT import *

XPOS = euclid.Vector3(1., 0, 0)
XNEG = euclid.Vector3(-1., 0, 0)
YPOS = euclid.Vector3(0, 1., 0)
YNEG = euclid.Vector3(0, -1., 0)
ZPOS = euclid.Vector3(0, 0, 1.)
ZNEG = euclid.Vector3(0, 0, -1.)

class AABox(object):
    '''Axis-Aligned Box centered on "center" with dimensions x, y, z'''
    def __init__(self, c, dx, dy, dz):
        self.dx, self.dy, self.dz = dx, dy, dz
        self.c = c

    @classmethod
    def from_extents(cls, minx, maxx, miny, maxy, minz, maxz):
        '''Return an AABox created from minx, max, miny, maxy, minz and 
        maxz dimensions.
        '''
        dx = (maxx - minx) * 0.5
        dy = (maxy - miny) * 0.5
        dz = (maxz - minz) * 0.5
        c = (minx + self.dx, miny + self.dy, minz + self.dz)
        return cls(c, dx, dy, dz)


    def __add__(self, b):
        '''Adds another bounding box volume, the new box covers both volumes.
        '''
        extents = (min(self.minx, b.minx), max(self.maxx, b.maxx),
          min(self.miny, b.miny), max(self.maxy, b.maxy),
          min(self.minz, b.minz), max(self.maxz, b.maxz))
        return self.from_extents(*extents)

    def get_c(self):
        '''Just return the value.'''
        return self.__c

    def set_c(self, c):
        '''Set the extents based on the center value and our dimensions.
        '''
        if isinstance(c, euclid.Point3):
            pass
        elif isinstance(c, euclid.Vector3):
            c = euclid.Point3(c.x, c.y, c.z)
        else:
            c = euclid.Point3(*c)
        c = self.__c = c

        x = (c.x - self.dx/2, c.x + self.dx/2)
        self.xmin = min(x)
        self.xmax = max(x)

        y = (c.y - self.dy/2, c.y + self.dy/2)
        self.ymin = min(y)
        self.ymax = max(y)

        z = (c.z - self.dz/2, c.z + self.dz/2)
        self.zmin = min(z)
        self.zmax = max(z)

    c = property(get_c, set_c, None)

    def __repr__(self):
        a = tuple(self.__c) + (self.dx, self.dy, self.dz)
        return '<AABox c=%s,%s,%s d=%s,%s,%s>'%a

    def is_intersecting(self, other):
        '''Does the other thing (currently just AABox and euclid.Point3)
        intersect this box?
        '''
        if isinstance(other, euclid.Point3):
            return (self.xmin < other.x < self.xmax and
                    self.ymin < other.y < self.ymax and
                    self.zmin < other.z < self.zmax)
        elif isinstance(other, AABox):
            c = other.c
            hdx, hdy, hdz = other.dx/2, other.dy/2, other.dz/2
            return (self.xmin-hdx < c.x < self.xmax+hdx and
                self.ymin-hdy < c.y < self.ymax+hdy and
                self.zmin-hdz < c.z < self.zmax+hdz)
        else:
            raise NotImplementedError

    def resolve_collision(self, other):
        '''An AABox has been determined to have collided with this AABox
        by is_intersecting() above. This method is to be called with the
        AABox in its non-intersecting state, and will move it as far as
        it can such that it touches this AABox (which is stationary).

        Returns (other AABox new center postion, velocity modifier)

        Velocity modifier indicates which axis we modified and thus
        which velocity axis should be zeroed.
        '''
        if not isinstance(other, AABox): raise NotImplementedError
        c = other.c

        # test from middle of closest edges
        s = self.__c
        sx = s.x; cx = c.x
        if other.c.x > self.c.x:
            s.x = self.xmax
            c.x = other.xmin
            v = (c - s).normalize()
            x = v.dot(XPOS)
        else:
            s.x = self.xmin
            c.x = other.xmax
            v = (c - s).normalize()
            x = v.dot(XNEG)
        s.x = sx; c.x = cx
        sy = s.y; cy = c.y
        if other.c.y > self.c.y:
            s.y = self.ymax
            c.y = other.ymin
            v = (c - s).normalize()
            y = v.dot(YPOS)
        else:
            s.y = self.ymin
            c.y = other.ymax
            v = (c - s).normalize()
            y = v.dot(YNEG)
        s.y = sy; c.y = cy
        sz = s.z; cz = c.z
        if other.c.z < self.c.z:
            s.z = self.zmax
            c.z = other.zmin
            v = (c - s).normalize()
            z = v.dot(ZPOS)
        else:
            s.z = self.zmin
            c.z = other.zmax
            v = (c - s).normalize()
            z = v.dot(ZNEG)
        s.z = sz; c.z = cz

        # now see which axis has the dot product closest to 1
        if y > x and y > z:
            c.y = self.ymax + other.dy/2
            vmod = euclid.Vector3(1, 0, 1)
        elif x > z:
            hdx = other.dx/2
            if c.x > self.c.x: c.x = self.xmax + hdx
            else: c.x = self.xmin - hdx
            vmod = euclid.Vector3(0, 1, 1)
        else:
            hdz = other.dz/2
            if c.z > self.c.z: c.z = self.zmax + hdz
            else: c.z = self.zmin - hdz
            vmod = euclid.Vector3(1, 1, 0)

        return c, vmod

    def render(self):
        glPushMatrix()
        glTranslate(self.__c.x, self.__c.y, self.__c.z)
        glScalef(self.dx, self.dy, self.dz)
        glColor(1, .5, .5)
        glutWireCube(1)
        glPopMatrix()


class Sphere(euclid.Sphere):
    def render(self):
        glPushMatrix()
        glTranslate(self.c.x, self.c.y, self.c.z)
        glColor(1, 1, 1)
        glutWireSphere(self.r, 8, 8)
        glPopMatrix()


