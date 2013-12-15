import zipfile
f=open("E:\\programs\\programming\\pythonprograms\\r.zip",'rb')
zf = zipfile.ZipFile("E:\\programs\\programming\\pythonprograms\\r\\r\\r.zip",mode='r',compression=zipfile.ZIP_DEFLATED)
for i in range(100000):
    newzip=zf.open("r/r.zip")
    f=open("r"+str(i)+".zip","wb")
    f.write(newzip.read())
    f.close()
    zf = zipfile.ZipFile("r"+str(i)+".zip")