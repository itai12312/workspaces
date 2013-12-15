Bouncy the Rabbit by Richard Jones <http://mechanicalcat.net/richard/>
----------------------------------------------------------------------

Written for the 3rd PyWeek Challenge: http://www.pyweek.org/


You are a hungry rabbit. Eat food to complete the level.

CONTROLS:

   arrows   - move around
   "e"      - eat whatever's in front of you
   "d"      - dig into the ground (hold to go underground)
              also, when underground, "d"ig to surface
   escape   - quit

Dig for long enough and you'll go into the hole.
Move around under ground - fences no longer stop you and the farmer can't
see you.
Hold "d" again to surface.


SPECIAL KEYS:

   "h"    - show hitboxes and A* information
   "o"    - switch to overhead view


LEVEL EDITOR CONTROLS:

Click the object in the side menu to select and click on the map to place
it. Hit the left & right arrow keys to rotate an object as you're moving it.

Right-mouse click on a placed object and you'll get a menu of actions to
perform on that object.

A map MUST have a player. You'll only be allowed place one.

You SHOULD NOT place more than one farmer. I'm not sure what'll happen if you
do...

You MUST surround the farmer with fences.

You SHOULD NOT place trees, hedges and scarecrows inside the fence, as
those objects do not participate in collision detection, nor will the
farmer notice their existence.

The GRID in the level editor is important. Try to fit your objects neatly
into the grid. The farmer may only move onto a square that is completely
empty.



SPECIAL BONUS:

If you're curious, try uncommenting the toon_program code at the top of
objects.py :)

You will need to have hardware shader support in your video card. I'm not
sure what version is required.



LICENSES:
euclid.py    -- see module for details


PYGLYPH LICENSE: (the pyglyph/ directory)

Copyright (c) 2006 Alex Holkner

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation files
(the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge,
publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


BITSTREAM VERA TRUETYPE FONT (in the data/ directory):

Copyright (c) 2003 by Bitstream, Inc. All Rights Reserved. Bitstream Vera
is a trademark of Bitstream, Inc. 
 
Permission is hereby granted, free of charge, to any person obtaining a
copy of the fonts accompanying this license (“Fonts”) and associated
documentation files (the “Font Software”), to reproduce and distribute
the Font Software, including without limitation the rights to use, copy,
merge, publish, distribute, and/or sell copies of the Font Software, and
to permit persons to whom the Font Software is furnished to do so, subject
to the following conditions: 

The above copyright and trademark notices and this permission notice shall
be included in all copies of one or more of the Font Software typefaces.
 
The Font Software may be modified, altered, or added to, and in particular
the designs of glyphs or characters in the Fonts may be modified and
additional glyphs or characters may be added to the Fonts, only if the
fonts are renamed to names not containing either the words “Bitstream”
or the word “Vera”.
 
This License becomes null and void to the extent applicable to Fonts or
Font Software that has been modified and is distributed under the
“Bitstream Vera” names. 
 
The Font Software may be sold as part of a larger software package but no
copy of one or more of the Font Software typefaces may be sold by itself. 
 
THE FONT SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO ANY WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT
OF COPYRIGHT, PATENT, TRADEMARK, OR OTHER RIGHT. IN NO EVENT SHALL
BITSTREAM OR THE GNOME FOUNDATION BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, INCLUDING ANY GENERAL, SPECIAL, INDIRECT, INCIDENTAL,
OR CONSEQUENTIAL DAMAGES, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF THE USE OR INABILITY TO USE THE FONT
SOFTWARE OR FROM OTHER DEALINGS IN THE FONT SOFTWARE. 
 
Except as contained in this notice, the names of Gnome, the Gnome
Foundation, and Bitstream Inc., shall not be used in advertising or
otherwise to promote the sale, use or other dealings in this Font
Software without prior written authorization from the Gnome Foundation
or Bitstream Inc., respectively. For further information, contact:
fonts at gnome dot org. 


ALL OTHER ARTWORK AND CODE:

Copyright (C) 2006 Richard Jones <richard@mechanicalcat.net>

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.



KNOWN BUGS:
- farmer needs to handle ST_OUTSIDE

TODO:
- view encompasses farmer and rabbit
- YOU WIN screen...
- title screen...
- make farmer wobble/lean when moving
- farmer following paths instead of just spinning
- farmer zones (for > 1 farmer)
- A* grid could be better
- more ground decorations - maybe paths leading from field to field?


GAMEPLAY IDEAS:
- time limit in "hard" mode?
- trap the farmer?
- you also have to rescue baby bunnies from the farmer guy (fydo)
- deliver Easter Eggs (alex)
- "get to the other rabbit" (alex)
- dog that runs in circles
- rabbit grows and grows and consumes bigger and bigger things?

