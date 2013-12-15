import os
import pygame
from OpenGL.GL import *

textures = {
    'bouncy-title': None,
    'carrot': (10, 241),
    'lettuce': (36, 201),
    'tomato': (0, 248),
}

def load():
    for name, args in textures.items():
        if args:
            image1 = pygame.image.load(os.path.join('data', name + '.png'))
            image2 = pygame.image.load(os.path.join('data', name + '-grey.png'))
            textures[name] = MixedTextureSurf(image1, image2, args)
        else:
            image = pygame.image.load(os.path.join('data', name + '.png'))
            textures[name] = TextureSurf(image)

class TextureSurf:
    def __init__(self, surf):
        w, h = surf.get_width(), surf.get_height()
        self.width = w
        self.height = h
        surf_data = pygame.image.tostring(surf, 'RGBA', 1)
        self.texture = glGenTextures(1)
        glBindTexture(GL_TEXTURE_2D, self.texture)
        glPixelStorei(GL_UNPACK_ROW_LENGTH, 0)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h,
             0, GL_RGBA, GL_UNSIGNED_BYTE, surf_data)

    def render(self):
        glEnable(GL_TEXTURE_2D)
        glBindTexture(GL_TEXTURE_2D, self.texture)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glBegin(GL_QUADS)
        glColor(1, 1, 1, 1)
        glTexCoord2f(0, 0)
        glVertex2f(0, 0)
        glTexCoord2f(1, 0)
        glVertex2f(self.width, 0)
        glTexCoord2f(1, 1)
        glVertex2f(self.width, self.height)
        glTexCoord2f(0, 1)
        glVertex2f(0, self.height)
        glEnd()
        glDisable(GL_TEXTURE_2D)


class MixedTextureSurf:
    def __init__(self, surf1, surf2, yrange=(0,0)):
        w, h = surf1.get_width(), surf1.get_height()
        self.yrange = yrange
        self.width = w
        self.height = h
        self.textures = []
        for surf in (surf1, surf2):
            surf_data = pygame.image.tostring(surf, 'RGBA', 1)
            self.textures.append(glGenTextures(1))
            glBindTexture(GL_TEXTURE_2D, self.textures[-1])
            glPixelStorei(GL_UNPACK_ROW_LENGTH, 0)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h,
                 0, GL_RGBA, GL_UNSIGNED_BYTE, surf_data)

    def render(self, ratio=1.):
        ratio = max(0., min(1., ratio))
        if ratio == 1:
            draw = [(0, 0, 1, 0, self.height)]
        elif ratio == 0:
            draw = [(1, 0, 1, 0, self.height)]
        else:
            b, t = self.yrange
            mid = b + (t-b) * ratio
            tmid = mid / self.height
            draw = [
                (0, 0, tmid, 0, mid),
                (1, tmid, 1, mid, self.height)
            ]
        glEnable(GL_TEXTURE_2D)
        for texidx, vs, ve, ys, ye in draw:
            glBindTexture(GL_TEXTURE_2D, self.textures[texidx])
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            glBegin(GL_QUADS)
            glTexCoord2f(0, vs)
            glVertex2f(0, ys)
            glTexCoord2f(1, vs)
            glVertex2f(self.width, ys)
            glTexCoord2f(1, ve)
            glVertex2f(self.width, ye)
            glTexCoord2f(0, ve)
            glVertex2f(0, ye)
            glEnd()
        glDisable(GL_TEXTURE_2D)

