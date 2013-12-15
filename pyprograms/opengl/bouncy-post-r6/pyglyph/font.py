#!/usr/bin/env python

'''
'''

__docformat__ = 'restructuredtext'
__version__ = '$Id: font.py,v 1.4 2006/05/25 14:14:40 alex Exp $'

import os

import pygame
import pygame.font
from OpenGL.GL import *

import pyglyph.layout
import pyglyph.ttf

MAX_TEXTURE_WIDTH = 1024
MIN_TEXTURE_CHARACTER_SPACE = 2

DEFAULT_CHARSET = 'abcdefghijklmnopqrstuvwxyz' +\
                  'ABCDEFGHIJKLMNOPQRSTUVWXYZ' +\
                  '1234567890;:,.!?%&+-~()[]{}\\/"\' '

class BaseFontFactory:
    """Abstract base class for collection of fonts.

    A font factory constructs fonts from some resource.  The only
    user-accessible method is get_font, used to construct a font.
    Subclasses must override the `impl_get_font` method.  See
    `LocalFontFactory` for a concrete example.

    This class provides a font cache to avoid reloading a font
    of a given size and style.
    """
    def __init__(self, allow_fake_bold=True, allow_fake_italic=True):
        """Construct the cache for a font factory.

        :Parameters:
            `allow_fake_bold`
                Whether to allow Pygame's "fake" bold when a suitable font
                cannot be found.  If this is False and a bold font is
                requested that cannot be found, an exception is raised.
                Defaults to True.
            `allow_fake_italic`
                The same as `allow_fake_bold`, but for fake italic.
        """

        self._cache = {}
        self.allow_fake_bold = allow_fake_bold
        self.allow_fake_italic = allow_fake_italic

    def get_font(self, family, size, bold=False, italic=False):
        """Create a font with the given characteristics.

        :Parameters:
            `family`
                The font's family name, e.g. "times new roman".  This is
                case insensitive.  Required.
            `size`
                Size of the font, in points (e.g., 12 is a standard readable
                size for paragraph text).  Required.
            `bold`, `italic`
                Boolean bold and italic characteristics.  Default to False.

        If successful, a `FontInstance` instance is returned.  If the
        requested font cannot be created, an exception is raised.
        """

        try:
            return self._cache[(family, size, bold, italic)]
        except KeyError:
            pass
        font = self.impl_get_font(family, size, bold, italic)
        self._cache[(family, size, bold, italic)] = font
        return font

    def impl_get_font(self, family, size, bold, italic):
        """Subclasses should override just this method.

        Implementations of this method should follow the contract
        specified in `get_font`.
        """
        raise NotImplementedError()

class LocalFontFactory(BaseFontFactory):
    """Font factory for a collection of font files loaded from disk.
    
    Typical applications will distribute Truetype font files alongside
    the executable or source code.  In this case, use this font factory
    and instantiate it with either a list of files or directories::
        
        fonts = LocalFontFactory('./fonts')

    or::

        fonts = LocalFontFactory(['times.ttf', 'arial.ttf'])

    Additional fonts can be searched later with the `add` method.
    """
    def __init__(self, path=None, **kwargs):
        """Construct a font factory for a set of font files.

        :Parameters:
            `path`
                A file, directory, or list of files or directories to
                search for fonts.  Only files with the extension ``.ttf``
                will be read, unless named explicitly.
            ``allow_fake_bold``
                See `BaseFontFactory.__init__`
            ``allow_fake_italic``
                See `BaseFontFactory.__init__`
        """
        BaseFontFactory.__init__(self, **kwargs)
        self._fonts = {}
        if path:
            self.add(path)

    def add(self, path):
        """Add a path or list of paths to search.

        A path can be either a truetype file or a directory containing
        truetype files.  Directories are not searched recursively."""
        if type(path) == list:
            for p in path:
                self.add(p)
        elif os.path.isdir(path):
            for file in os.listdir(path):
                if os.path.splitext(file)[1].lower() == '.ttf':
                    self.add(os.path.join(path, file))
        else:
            info = pyglyph.ttf.TruetypeInfo(path)
            metrics = FontMetrics(info)
            self._fonts[(info.get_name('family').lower(), 
                         info.is_bold(),
                         info.is_italic())] = (path, metrics)
            info.close()
        
    def impl_get_font(self, family, size, bold, italic):
        attempts = [(bold, italic)]
        if bold and self.allow_fake_bold:
            attempts.append((False, italic))
        if italic and self.allow_fake_italic:
            attempts.append((bold, False))
        if len(attempts) >= 3:
            attempts.append((False, False))

        filename = None
        for b, i in attempts:
            try:
                filename, metrics = self._fonts[(family.lower(), b, i)]
                if filename:
                    break
            except:
                pass
        if not filename:
            raise Exception, 'Font \"%s\" (bold=%s,italic=%s) not found' % \
                (family, bold, italic)
        fake_bold = bold and not b
        fake_italic = italic and not i

        return FontInstance(filename, size, 
                            metrics=metrics,
                            fake_bold=fake_bold,
                            fake_italic=fake_italic)

class FontMetrics:
    """Size-independent metrics for a truetype font.
    
    We avoid using TruetypeInfo directly since:
        * it would need to keep the mmap, which is ugly, and
        * maybe we'll support Postscript or bitmap fonts in the future.
    """
    def __init__(self, info=None):
        """Construct a new instance with the given Truetype information

        :Parameters:
            `info` : TruetypeInfo
                TruetypeInfo instance to get metrics from
        """
        self.advances = {}
        self.kerns = {}
        if info:
            try:
                self.kerns = info.get_character_kernings()
            except:
                pass
            try:
                self.advances = info.get_character_advances()
            except:
                pass

class FontInstance:
    """A prerendered font ready to be used.

    FontInstances are created for each family, style and size you need.
    A set of characters is rendered into a texture which is used for
    displaying during render time.  It is generally easier to use
    a FontFactory (see, for example, `LocalFontFactory`) than instantiating
    a FontInstance directly.
    """

    def __init__(self, filename, size, 
                 metrics=None, 
                 antialias=True, 
                 kerning=True, 
                 fake_bold=False,
                 fake_italic=False,
                 charset=DEFAULT_CHARSET):
        """Construct a FontInstance directly (discouraged).

        :Parameters:
            `filename` : str
                Filename of a truetype file, passed directly to pygame
                as well as used to extract metrics if none are given.
            `size` : int
                Size of the font to create, in points (e.g., 12 is a typical
                paragraph font).
            `metrics` : FontMetrics
                Metrics for character advance and pairwise kerning.
                Extracted from the Truetype file if not given.
            `antialias` : bool
                Specifies whether to render the font with pygame's
                antialiasing.  Defaults to True.
            `kerning` : bool
                Specifies whether to enable pairwise character kerning
                when laying out text.
            `fake_bold` : bool
                Enable fake bold rendering with pygame.  Defaults to False
            `fake_italic` : bool
                Enable fake italic rendering with pygame.  Defaults to False
            `charset` : str
                Characters to use for rendering.  You must specify all
                characters you are going to use at render time.  Defaults
                to `DEFAULT_CHARSET`.
        """
                
        self.filename = filename
        self.size = size
        self.charset = charset
        self.antialias = antialias
        self.kerning = kerning

        if not metrics:
            metrics = FontMetrics(filename)
        # Premultiply font metrics for this size
        self.advances = {}
        self.kerns = {}
        for ch in metrics.advances:
            self.advances[ch] = int(self.size * metrics.advances[ch])
        for pair in metrics.kerns:
            self.kerns[pair] = int(self.size * metrics.kerns[pair])

        # Load font with SDL/Pygame and get global metrics.
        self.pygame_font = pygame.font.Font(filename, size)
        self.pygame_font.set_bold(fake_bold)
        self.pygame_font.set_italic(fake_italic)
        self.ascent = self.pygame_font.get_ascent()
        self.descent = self.pygame_font.get_descent()
        self.glyph_height = self.ascent - self.descent
        self.line_height = self.pygame_font.get_linesize()

        # Determine required size of texture
        w, h = self.pygame_font.size(self.charset)
        w += len(self.charset) * MIN_TEXTURE_CHARACTER_SPACE
        if w > MAX_TEXTURE_WIDTH:
            h = (w / MAX_TEXTURE_WIDTH + 1) * h
            w = MAX_TEXTURE_WIDTH

        # Round up to valid texture size
        w, h = _pow2(w), _pow2(h)

        # Initialise empty surface to draw text onto.  Problems with
        # luminance-alpha, so using RGBA.
        data = chr(0) * w * h * 4
        surface = pygame.image.fromstring(data, (w, h), 'RGBA')

        # Render each glyph to the surface, recording each bounding box
        # in tex_boxmap.
        self.tex_boxmap = {}
        x, y = 0.0, 0.0
        for c in charset:
            render = self.pygame_font.render(c, self.antialias, (255, 255, 255))
            cw, ch = self.pygame_font.size(c)
            if x + cw >= w:
                x = 0.0
                y += self.glyph_height
            surface.blit(render, (x, y))

            # Each tex_boxmap item is a bounding box (x1,y1,x2,y2,cw)
            self.tex_boxmap[c] = (x/w, (h-y-self.glyph_height)/h, 
                                  (x+cw)/w, (h-y)/h, cw)
            if not c in self.advances:
                self.advances[c] = cw
            x += cw + MIN_TEXTURE_CHARACTER_SPACE 

        # Create OpenGL texture
        self.tex = glGenTextures(1)
        glBindTexture(GL_TEXTURE_2D, self.tex)
        glTexParameter(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexImage2D(GL_TEXTURE_2D, 0, 4, w, h,
                     0, GL_RGBA, GL_UNSIGNED_BYTE, 
                     pygame.image.tostring(surface, 'RGBA', True))

    def get_boxes(self, text):
        """Compute the render boxes for a given string.

        :Parameters:
            `text` : str
                Text to be rendered

        Pairwise kerning will be applied, if enabled.  Nothing is rendered;
        this is a precompute step.  The return value is a tuple::

            boxes, total_advance

        where boxes is a list of tuples of the form::
            
            (renderbox, texbox)

        where ``renderbox`` and ``texbox`` are 4-element tuples representing
        the render-space and texture-space rectangles for a glyph.  No
        guarantee is made about the order or number of glyphs returned
        (for example, in the future, automatic ligaturing may take place).

        Generally a user application can simply pass ``boxes`` to
        `draw_boxes`.
        """
        if not self.kerning:
            kerned_text = \
                [(c, self.tex_boxmap[c], self.advances[c]) for c in text]
        else:
            kerned_text = []
            for i in range(len(text)-1):
                c = text[i]
                pair = (c,text[i+1])
                k = pair in self.kerns and self.kerns[pair] or 0
                kerned_text.append((self.tex_boxmap[c], self.advances[c] + k))
            c = text[-1]
            kerned_text.append((self.tex_boxmap[c], self.advances[c]))
        boxes = []
        total_advance = 0
        x, y = 0, self.descent
        for box, advance in kerned_text:
            renderbox = (x, y, x + box[4], y + self.glyph_height)
            texbox = box[:4]
            boxes.append((renderbox, texbox))
            total_advance += advance
            x += advance
        return boxes, total_advance        
        
    def draw_boxes(self, boxes):
        """Draw a set of precalculated glyph boxes at the origin.

        :Parameters:
            `boxes`
                A list of (renderbox,texbox) tuples; see `get_boxes`.

        The text will be rendered at pixel-size at the origin.  Use
        ``glTranslate`` to draw the text at a different position.
        Make sure you have an orthagonal projection set up if you want
        the text to be displayed at the optimal resolution.  This
        method assumes texturing and alpha-blending is already enabled
        (see `begin` and `end`).
        """

        glBindTexture(GL_TEXTURE_2D, self.tex)
        glBegin(GL_QUADS)
        for renderbox, texbox in boxes:
            glTexCoord2f(texbox[0], texbox[1])
            glVertex(renderbox[0], renderbox[1])
            glTexCoord2f(texbox[2], texbox[1])
            glVertex(renderbox[2], renderbox[1])
            glTexCoord2f(texbox[2], texbox[3])
            glVertex(renderbox[2], renderbox[3])
            glTexCoord2f(texbox[0], texbox[3])
            glVertex(renderbox[0], renderbox[3])
        glEnd()

def _pow2(n):
    """Return the next highest power of 2"""
    x = 1
    while x < n:
        x <<= 1
    return x
