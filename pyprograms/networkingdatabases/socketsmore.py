import socket, sys
sock = socket.socket ( socket.AF_INET, socket.SOCK_STREAM )
sock.connect ( ( "google.com", 80 ) )
 
sock.send('GET / HTTP/1.1\r\n')
sock.send('User-agent: Mozilla/5.0 (wikibooks test)\r\n\r\n')
print irc.recv(4096)

also look at twisted-http://twistedmatrix.com/trac/
http://docs.python.org/2/library/urllib2