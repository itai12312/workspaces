# -*- coding: utf-8-*-
array=["à","á","â","ã","ä","å","æ","ç","è","é","ê","ë","ì","í","î","ï","ð","ñ","ó","ô","õ","ö","ø","ù","ú","û","ü","ý","þ",'ÿ"]
array1=["א","ב","ג","ד","ה","ו","ז","ח","ט","י","ך","כ","ל","ם","מ","ן","נ","ס","ע","ף","פ","ץ","צ","ק","ר","ש","ת"]
str="áï éäåãä"
message=""
for i in range(0,len(str)):
    s=str[i]
    index=-1
    for j in range(0,len(array)):
        if(array[j]==s):
            index=j
            break
    if(index!=-1):
    message+=array1[index]
    print array1[index]
print message