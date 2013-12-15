import pyodbc
 
DBfile = '/data/MSAccess/Music_Library.mdb'
conn = pyodbc.connect('DRIVER={Microsoft Access Driver (*.mdb)};DBQ='+DBfile)
cursor = conn.cursor()
 
SQL = 'SELECT Artist, AlbumName FROM RecordCollection ORDER BY Year;'
for row in cursor.execute(SQL): # cursors are iterable
    print row.Artist, row.AlbumName
 
cursor.close()
conn.close()
#sqli-
import sqlite3
db = sqlite3.connect("/path/to/file")
cursor = db.cursor()
query = """INSERT INTO sampletable (value1, value2) VALUES (1,'test')"""
cursor.execute(query)
db.commit()
db.close()
#mysql
import MySQLdb
db = MySQLdb.connect("host machine", "dbuser", "password", "dbname")
cursor = db.cursor()
query = """SELECT * FROM sampletable"""
lines = cursor.execute(query)
data = cursor.fetchall()
db.close()
  #first 5 lines are the same as above
  while True:
    row = cursor.fetchone()
    if row == None: break
    #do something with this row of data
  db.close()