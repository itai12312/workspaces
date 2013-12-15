#!/usr/bin/env python

'''
'''

__docformat__ = 'restructuredtext'
__version__ = '$Id: __init__.py,v 1.14 2006/05/25 14:14:40 alex Exp $'

from OpenGL.GL import *

import pyglyph.font
import pyglyph.html
import pyglyph.layout

Align = pyglyph.layout.Align

def begin():
    """Push a text-rendering mode onto the OpenGL stack.

    This sets up the rendering context so it's ready for text rendering.
    Specifically, it enables texturing, alpha-blending and texture
    modulation.  The changes are pushed onto the attribute stack and
    the previous state can be recalled by calling `end`.

    Typical usage is::

        begin()
        draw_text('Hello')
        end()

    Since state changes in OpenGL are relatively expensive, you should
    group all text drawing calls into a single begin/end block each
    frame.  If your drawing context has texturing and alpha blending
    enabled and setup in the usual way anyway, there is no need to
    call this function.
    """
    glPushAttrib(GL_ENABLE_BIT | GL_TEXTURE_BIT)
    glEnable(GL_TEXTURE_2D)
    glEnable(GL_BLEND)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE)

def end():
    """Pop the text-rendering mode off the OpenGL stack.

    See `begin` for details.
    """
    glPopAttrib()

_default_font_factory = None
def layout_html(text, width=-1, font_factory=None):
    """Layout HTML markup ready for drawing in OpenGL.

    :Parameters:
        `text` : str
            HTML markup text to render
        `width` : int
            Width, in pixels, at which to wrap lines.  Defaults to not
            wrapping.
        `font_factory` : pyglyph.font.BaseFontFactory
            Font factory to use for instantiating fonts.  If unspecified,
            search the current directory for Truetype files.
    """
    if not font_factory:
        global _default_font_factory
        if not _default_font_factory:
            _default_font_factory = pyglyph.font.LocalFontFactory('.')
        font_factory = _default_font_factory
    runs = pyglyph.html.parse(text, font_factory)
    layout = pyglyph.layout.OpenGLTextLayout(width)
    layout.layout(runs)
    return layout
    
_default_font = None
def layout_text(text, width=-1, font=None, color=(0,0,0,1)):
    """Layout plain text ready for drawing in OpenGL.

    :Parameters:
        `text` : str
            Text string to render
        `width` : int
            Width, in pixels, at which to wrap lines.  Defaults to not
            wrapping.
        `font` : pyglyph.font.FontInstance
            FontInstance to render the text with.  If unspecified,
            the pygame default font is used at size 16pt.
        `color` : tuple of size 3 or 4
            Color to render the text in (passed to glColor).
    """
    if not font:
        global _default_font
        if not _default_font:
            _default_font = pyglyph.font.FontInstance(None, 16)
        font = _default_font
    style = pyglyph.layout.Style(font, color)
    run = pyglyph.layout.StyledRun(text, style)
    layout = pyglyph.layout.OpenGLTextLayout(width)
    layout.layout([run])
    return layout
