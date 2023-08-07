from operator import indexOf
from unicodedata import numeric
from unittest import result
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import json
import base64

#c

#Ustawianie firebase :-)
cred = credentials.Certificate("liftcrane-firebase.json")
firebase_admin.initialize_app(cred)
db = firestore.client()

def getName (data):
    nazwa = data["FIELD 0"]
    return nazwa

def getInfo (data):
    base64_message = data["BUFFER2"]["$binary"]
    base64_bytes = base64_message.encode('utf-8')
    message_bytes = base64.b64decode(base64_bytes)
    message = message_bytes.decode('ISO-8859-2')
    message = message.replace("Ľ", "Ą")
    message = message.replace("š", "ą")
    message = message.replace(u"\x9c", "ś")
    message = message.replace(u"\x8c", "Ś")
    message = message.replace(u"\x9f", "ź")

    name = message.replace('    ','   ')
    name = name.split('   ')
    b= 0
    for idx, n in enumerate(name):
        try:
            if (n[0]=='N' and len(n)>=11 and len(n)<=12):
                name = n
                b = idx
                break
        except IndexError:
            pass
    # if len(name) == 2:
    #     return None
    # else:
    print()
    return b


    # miejscowosc = miejscowosc.strip()

    #Walka z ID (chujstwo sie nie zgadza. UDT maja zawsze 10 cyfr!!!
    #  A tu często w bd nie ma takich bajerów)
    #Możliwe, że będzie trzeba ręcznie z dokumentów przepisać
    # try:
    #     if(z[30].isnumeric() == 0):
    #         z[30] = 'null'
    #     z = 'N'+z[30]
    # except IndexError:
    #     try:
    #         z = 'N'+z[11]
    #     except IndexError:
    #             z = 'tesst'#+adres[24]

    # if(len(z) < 8):
    #         for pole in adres:
    #             if len(pole) == 9 or len(pole) == 10:
    #                 z = 'N'+pole
    
    # if(len(z) < 8 or z[1:].isnumeric == 0):
    #         for pole in temp:
    #             if len(pole) == 9 and pole.isnumeric:
    #                 z = 'N'+pole
    


##################################################################################
f = open('output.txt','r', encoding="utf-8")
lines = f.readlines()
IDs = []

for line in lines:
    data = json.loads(line)

    if(getName(data)[:3] == 'US0'):
        res = getInfo(data)
        print(getInfo(data))
        if type(res) is list:
            res = ''.join(res.split())
            IDs.append(res)
            # print(res)
            if not res[1].isdigit(): ## daj 1
                IDs.remove(res)
            x = x+1
        else:
            pass
IDs = list( dict.fromkeys(IDs))
print(IDs)
x = 1
# for line in lines:
#     data = json.loads(line)

#     if(getName(data)[:3] == 'US0'):
#         res = getInfo(data)
#         print(getInfo(data))
#         if type(res) is str:
#             res = ''.join(res.split())
#             IDs.append(res)
#             # print(res)
#             if not res[1].isdigit(): ## daj 1
#                 IDs.remove(res)
#             x = x+1
#         else:
#             pass
# IDs = list( dict.fromkeys(IDs))
# # chuj = IDs[275]
# print(IDs)
# print(len(IDs))


    # print(getInfo(data))
    # nazwa = getInfo(data)[0]
    # Adres = getInfo(data)[1]
    # print(str(x)+'.' +"="*100)
    # x=x+1

    # Gówno niżej to request do bd - Zrób jak kompletne

    # doc_ref = db.collection(u'lifts').document(ID)
    # doc_ref.set({
    # u'name': nazwa,
    # u'id': ID,
    # u'deviceAdress': Adres
    # u'clientAdress': Adres
    # u'deviceType': Adres
    # u'deviceCapacity': Adres
    # u'clientAdress': Adres

    #  })


# data = json.loads(d)
# base64_message = data["BUFFER4"]["$binary"]
# base64_bytes = base64_message.encode('ISO-8859-2')
# message_bytes = base64.b64decode(base64_bytes)
# message = message_bytes.decode('ISO-8859-2')
# message = message.replace(u"\x9c", "ś") ##Naprawia bo nie bylo ś
# adres = message.split("  ")
# print(adres)
# print(data["BUFFER0"]["$binary"])



#Łukaszek Koding 2022