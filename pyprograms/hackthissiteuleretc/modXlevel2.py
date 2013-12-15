string=open("level2.txt","r").read()
for off in [25,26,27,28,29,59]:
    print "\n\nwith offset",off
    decoded=""
    for c in string:
        if off+ord(c) in range(32,127):
            decoded+=chr(off+ord(c))
    print decoded