import OpenGL.GL as gl
import OpenGL.GLUT as glut
from numpy import array

gr_squares=[-0.5,0.0,0.5, 0.5,0.0,0.5, 0.5,0.0,-0.5, -0.5,0.0,-0.5]
gr_trigs=[-0.5,0.0,0.5, 0.5,0.0,0.5, 0.0,0.5,0.0,
            0.5,0.0,0.5, 0.5,0.0,-0.5, 0.0,0.5,0.0,
            0.5,0.0,-0.5, -0.5,0.0,-0.5,  0.0,0.5,0.0,
            -0.5,0.0,-0.5, -0.5,0.0,0.5, 0.0,0.5,0.0]
def display():
    gl.glClear(gl.GL_DEPTH_BUFFER_BIT)
    gl.glVertexAttribPointer(pos_num,3,gl.GL_FLOAT,gl.GL_TRUE,0,array(gr_squares,'f'))  #in java, the format of pointer is different
    gl.glDrawArrays(gl.GL_QUADS,0,4)
    #gl.glVertexAttribPointer(pos_num,3,gl.GL_FLOAT,gl.GL_TRUE,0,array(gr_trigs,'f'))  #in java, the format of pointer is different
    #gl.glDrawArrays(gl.GL_TRIANGLES,0,12)
    gl.glFlush()
    glut.glutSwapBuffers()

glut.glutInit()
glut.glutInitDisplayMode(glut.GLUT_DEPTH)
glut.glutInitWindowSize(600,600)
glut.glutCreateWindow("GPU test")
glut.glutDisplayFunc(display)

prog=gl.glCreateProgram()
vert=gl.glCreateShader(gl.GL_VERTEX_SHADER)
frag=gl.glCreateShader(gl.GL_FRAGMENT_SHADER)
gl.glShaderSource(vert,"varying vec2 xy; attribute vec3 position;void main() {xy=position.xy; gl_Position=gl_ModelViewProjectionMatrix*vec4(position.x+0.1,position.y-0.3,0.0,position.z+1.0);}")
gl.glShaderSource(frag,"varying vec2 xy; float shiny; void main() {shiny=pow(cos(sqrt(dot(xy,xy))),30); gl_FragColor=vec4(1.0,0.0,0.0,1.0);}")
gl.glCompileShader(vert)
gl.glCompileShader(frag)
gl.glAttachShader(prog,vert)
gl.glAttachShader(prog,frag)
print gl.glGetShaderInfoLog(vert)
gl.glLinkProgram(prog)
gl.glUseProgram(prog)

pos_num=gl.glGetAttribLocation(prog,'position')
gl.glEnableVertexAttribArray(pos_num)
glut.glutMainLoop()