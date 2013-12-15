import urllib
pageText = urllib.urlopen("http://www.spam.org/eggs.html").read()
print pageText
Get and post methods can be used, too.
import urllib
params = urllib.urlencode({"plato":1, "socrates":10, "sophokles":4, "arkhimedes":11})
 
# Using GET method
pageText = urllib.urlopen("http://international-philosophy.com/greece?%s" % params).read()
print pageText
 
# Using POST method
pageText = urllib.urlopen("http://international-philosophy.com/greece", params).read()
print pageText
Downloading files[edit]

To save the content of a page on the internet directly to a file, you can read() it and save it as a string to a file object, or you can use the urlretrieve function:
import urllib
urllib.urlretrieve("http://upload.wikimedia.org/wikibooks/en/9/91/Python_Programming.pdf", "pythonbook.pdf")
This will download the file from here and save it to a file "pythonbook.pdf" on your hard drive.
Other functions[edit]

The urllib module includes other functions that may be helpful when writing programs that use the internet:
>>> plain_text = "This isn't suitable for putting in a URL"
>>> print urllib.quote(plain_text)
This%20isn%27t%20suitable%20for%20putting%20in%20a%20URL
>>> print urllib.quote_plus(plain_text)
This+isn%27t+suitable+for+putting+in+a+URL