import numpy
mydata = [numpy.random.normal(0,1) for i in range(10000) ]
h, n = numpy.histogram( mydata , 100, (-5,5) )