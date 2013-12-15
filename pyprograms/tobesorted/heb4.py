# -*- coding: utf-8 -*-
import sys
from optparse import OptionParser
def translate(sentence):
    sent_l = list(sentence)
    #if options.reverse: sent_l.reverse()
    for c in sent_l:
        if 224 <= ord(c) <= 250:
            sys.stdout.write(unichr(ord(u"א")+ord(c)-ord(u'à')).encode('utf-8'))
        else:
            sys.stdout.write(c.encode('utf-8'))
    print
#parser = OptionParser()
#parser.add_option("-r","--reverse",action="store_false", dest="reverse",default="True",help="reverse output")
#(options,args) = parser.parse_args()
#sentence = " ".join(["%s" % (s) for s in args])
sentence="à áï éäåãä"
translate(sentence.decode('utf-8'))
