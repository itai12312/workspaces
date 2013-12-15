import pyglyph

# load up fonts
fonts = pyglyph.font.LocalFontFactory('data')
sans20 = fonts.get_font(family='bitstream vera sans',
    size=20, bold=False, italic=False)

sans40 = fonts.get_font(family='bitstream vera sans',
    size=40, bold=False, italic=False)

mono20 = fonts.get_font(family='bitstream vera sans mono',
    size=20, bold=False, italic=False)

