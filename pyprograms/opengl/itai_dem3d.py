import OpenGL.GL as gl
import OpenGL.GLUT as glut
from numpy import array

gr_squares=[-0.5,0.0,0.5, 0.5,0.0,0.5, 0.5,0.0,-0.5, -0.5,0.0,-0.5]
gr_trigs=[-0.5,0.0,0.5, 0.5,0.0,0.5, 0.0,0.5,0.0,
            0.5,0.0,0.5, 0.5,0.0,-0.5, 0.0,0.5,0.0,
            0.5,0.0,-0.5, -0.5,0.0,-0.5,  0.0,0.5,0.0,
            -0.5,0.0,-0.5, -0.5,0.0,0.5, 0.0,0.5,0.0]

cl_squares=[0.5,0.5,0.5, 0.5,0.5,0.5, 0.5,0.5,0.5, 0.5,0.5,0.5]
cl_trigs=[0.5,0.0,0.5, 0.5,0.0,0.5, 0.5,0.0,0.5,
            0.0,0.5,0.0, 0.0,0.5,0.0, 0.0,0.5,0.0,
            1.0,1.0,1.0, 1.0,1.0,1.0, 1.0,1.0,1.0, 
            0.0,0.0,1.0,0.0,0.0,1.0,0.0,0.0,1.0]

def display():
    gl.glClear(gl.GL_DEPTH_BUFFER_BIT)
    gl.glVertexAttribPointer(pos_num,3,gl.GL_FLOAT,gl.GL_TRUE,0,array(gr_squares,'f'))  #in java, the format of pointer is different
    gl.glVertexAttribPointer(col_num,3,gl.GL_FLOAT,gl.GL_TRUE,0,array(cl_squares,'f'))  #in java, the format of pointer is different
    gl.glDrawArrays(gl.GL_QUADS,0,4)
    gl.glVertexAttribPointer(pos_num,3,gl.GL_FLOAT,gl.GL_TRUE,0,array(gr_trigs,'f'))  #in java, the format of pointer is different
    gl.glVertexAttribPointer(col_num,3,gl.GL_FLOAT,gl.GL_TRUE,0,array(cl_trigs,'f'))  #in java, the format of pointer is different
    gl.glDrawArrays(gl.GL_TRIANGLES,0,12)
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
gl.glShaderSource(vert,"varying vec2 xy; varying vec3 rgb; attribute vec3 position; attribute vec3 color; void main() {rgb=color;xy=position.xy; gl_Position=gl_ModelViewProjectionMatrix*vec4(position.x+0.5,position.y-0.3,position.z,position.z+1.0);}")
gl.glShaderSource(frag,"varying vec2 xy; varying vec3 rgb; float shiny; void main() {shiny=pow(cos(sqrt(dot(xy,xy))),30); gl_FragColor=vec4(rgb.rgb,1.0);}")
gl.glCompileShader(vert)
gl.glCompileShader(frag)
gl.glAttachShader(prog,vert)
gl.glAttachShader(prog,frag)
print gl.glGetShaderInfoLog(vert)
gl.glLinkProgram(prog)
gl.glUseProgram(prog)

pos_num=gl.glGetAttribLocation(prog,'position')
col_num=gl.glGetAttribLocation(prog,'color')
gl.glEnableVertexAttribArray(pos_num)
gl.glEnableVertexAttribArray(col_num)

gl.glEnable(gl.GL_DEPTH_TEST)
glut.glutMainLoop()