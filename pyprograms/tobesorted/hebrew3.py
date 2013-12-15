# -*- coding: utf-8-*-
array  = u'àáâãäåæçèéêëìíîïðñóôõöøùúûüýþÿ'
array1 = u'אבגדהוזחטיךכלםמןנסעףפץצקרשת'
#print array.decode('cp1255',errors='replace')
print(array.encode('iso-8859-1').decode('cp1255',errors='replace'))