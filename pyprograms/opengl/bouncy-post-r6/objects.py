import math, sys
import euclid, objloader, collide
from OpenGL.GL import *
from OpenGL.GLU import *

toon_program = None
#try:
#    import shaders
#    toon_program = shaders.toon_program()
#except:
#    print 'No shaders available:', sys.exc_info()[1]

class MapElement:
    is_food = False
    def __init__(self, rotation, position):
        self.rotation = rotation
        self.position = position
        self.hitbox = None
        self.init()
    def init(self):
        pass

    def __repr__(self):
        return '<%s at c=%r,r=%r>'%(self.__class__.__name__, self.position,
            self.rotation)

    def render(self, show_collide=False):
        glPushMatrix()
        glTranslate(*self.position)
        if self.rotation: glRotate(self.rotation, 0, 1, 0)
        glCallList(self.obj.gl_list)
        glPopMatrix()
        if show_collide and self.hitbox:
            self.hitbox.render()

class Fence(MapElement):
    file = 'data/fence.obj'
    def init(self):
        r = euclid.Matrix3.new_rotate(self.rotation * math.pi / 180)
        v = r * euclid.Vector2(1, 20)
        px, py, pz = self.position
        self.hitbox = collide.AABox((px, py+3, pz), abs(v.x), 10., abs(v.y))
class Gate(Fence):
    file = 'data/gate.obj'

class Player(MapElement):
    file = 'data/rabbit-sitting.obj'
class Farmer(MapElement):
    file = 'data/farmer.obj'
class Bucket(MapElement):
    file = 'data/bucket.obj'
    def init(self):
        px, py, pz = self.position
        self.hitbox = collide.AABox((px, py+1, pz), 2., 10, 2)
class Hole(MapElement):
    file = 'data/hole.obj'
class Tree(MapElement):
    file = 'data/tree.obj'
class Hedge(MapElement):
    file = 'data/hedge.obj'
class LongHedge(MapElement):
    file = 'data/long-hedge.obj'
class Scarecrow(MapElement):
    file = 'data/scarecrow.obj'
class Pie(MapElement):
    file = 'data/pie.obj'
class Row(MapElement):
    file = 'data/row.obj'

class Lettuce(MapElement):
    file = 'data/lettuce.obj'
    name = 'lettuce'
    is_food = True
    food_value = 1
    def init(self):
        px, py, pz = self.position
        self.hitbox = collide.AABox((px, py+1, pz), 2, 2., 2)
class LettuceEat1(Lettuce):
    file = 'data/lettuce-eat1.obj'
    food_value = 1
Lettuce.next = LettuceEat1
class LettuceEat2(Lettuce):
    file = 'data/lettuce-eat2.obj'
    food_value = 1
LettuceEat1.next = LettuceEat2
class LettuceEat3(Lettuce):
    file = 'data/lettuce-eat3.obj'
    food_value = 1
LettuceEat2.next = LettuceEat3
class LettuceEat4(LettuceEat3):
    file = 'data/lettuce-eat4.obj'
    is_food = False
LettuceEat3.next = LettuceEat4

class Tomato(MapElement):
    file = 'data/tomato.obj'
    name = 'tomato'
    is_food = True
    food_value = 1
    def init(self):
        px, py, pz = self.position
        self.hitbox = collide.AABox((px, py+2.5, pz), 2, 5., 2)
class TomatoEat1(Tomato):
    file = 'data/tomato-eat1.obj'
    food_value = 1
Tomato.next = TomatoEat1
class TomatoEat2(Tomato):
    file = 'data/tomato-eat2.obj'
    food_value = 1
TomatoEat1.next = TomatoEat2
class TomatoEat3(Tomato):
    file = 'data/tomato-eat3.obj'
    food_value = 1
TomatoEat2.next = TomatoEat3
class TomatoEat4(TomatoEat3):
    file = 'data/tomato-eat4.obj'
    is_food = False
    next = None
TomatoEat3.next = TomatoEat4

class Carrot(MapElement):
    file = 'data/carrot.obj'
    name = 'carrot'
    is_food = True
    food_value = 2
    def init(self):
        px, py, pz = self.position
        self.hitbox = collide.AABox((px, py, pz), 1, 1, 1)
class CarrotEat1(Carrot):
    file = 'data/carrot-eat1.obj'
    food_value = 2
    next = None
Carrot.next = CarrotEat1

map_elements = {
    'fence': Fence,
    'gate': Gate,
    'row': Row,
    'tree': Tree,
    'hedge': Hedge,
    'long-hedge': LongHedge,
    'bucket': Bucket,
    'scarecrow': Scarecrow,

    'pie': Pie,

    'hole': Hole,
    'player': Player,
    'farmer': Farmer,

    'lettuce': Lettuce,
    'lettuce-eat1': LettuceEat1,
    'lettuce-eat2': LettuceEat2,
    'lettuce-eat3': LettuceEat3,
    'lettuce-eat4': LettuceEat4,

    'tomato': Tomato,
    'tomato-eat1': TomatoEat1,
    'tomato-eat2': TomatoEat2,
    'tomato-eat3': TomatoEat3,
    'tomato-eat4': TomatoEat4,

    'carrot': Carrot,
    'carrot-eat1': CarrotEat1,
}

def load():
    for name, element in map_elements.items():
        element.obj = objloader.OBJ(element.file,
            outline=toon_program is not None)
        element.name = name

