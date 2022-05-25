import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import json
import base64

# BUFFER0 - nazwa i kod!!!
# BuFFER 1 - ADRES i wlasciciel i nip
#W AGATAMEBLE w BUFFER2 jest adres :(
    #Jest pierdolnik - są wyjątki ktorych nie ma sensu automatyzować
    #Ręcznie będzie łatwiej

#Wez liste miejscowosci z dolnoslaskie.
#Sprawdz czy miejscowość odpowiada liście
#jak nie to szuka dalej
#przez wszystkie listy
#adres = tablica[miejscowosc] + tablica[numer]

#Ustawianie firebase :-)
cred = credentials.Certificate("liftcrane-firebase.json")
firebase_admin.initialize_app(cred)
db = firestore.client()


def getInfo (data): #Zwraca  [0] - nazwa, [1] - id, [2]-adres
    #Wyciaga nazwe
    base64_message = data["BUFFER0"]["$binary"]
    base64_bytes = base64_message.encode('ISO-8859-2')
    message_bytes = base64.b64decode(base64_bytes)
    message = message_bytes.decode('ISO-8859-2')
    message = message.replace(u"\x98", "ś") ##Naprawia bo nie bylo ś
    nazwa = data["FIELD 0"]
    z = message.split("  ") #Tworzy tablice oddielona dwoma spacjami
    temp = z

    #Wyciaga adres
    base64_message = data["BUFFER1"]["$binary"]
    base64_bytes = base64_message.encode('ISO-8859-2')
    message_bytes = base64.b64decode(base64_bytes)
    message = message_bytes.decode('ISO-8859-2')
    message = message.replace(u"\x9c", "ś") ##Naprawia bo nie bylo ś
    message = message.replace(u"Ä\x98Ĺ", "ś") ##Naprawia bo nie bylo ś

    adres = message.split("  ")
    miejscowosc = adres[6]+adres[7]
    # miejscowosc = miejscowosc.strip()
    ulica = adres[3] + adres[4]

    #Walka z ID (chujstwo sie nie zgadza. UDT maja zawsze 10 cyfr!!!
    #  A tu często w bd nie ma takich bajerów)
    #Możliwe, że będzie trzeba ręcznie z dokumentów przepisać
    try:
        if(z[30].isnumeric() == 0):
            z[30] = 'null'
        z = 'N'+z[30]
    except IndexError:
        try:
            z = 'N'+z[11]
        except IndexError:
                z = 'tesst'#+adres[24]

    if(len(z) < 8):
            for pole in adres:
                if len(pole) == 9 or len(pole) == 10:
                    z = 'N'+pole
    
    if(len(z) < 8 or z[1:].isnumeric == 0):
            for pole in temp:
                if len(pole) == 9 and pole.isnumeric:
                    z = 'N'+pole
    
    return nazwa,z, miejscowosc + " "+ ulica


##################################################################################
f = open('adres_main_json.txt','r', encoding="utf-8")
lines = f.readlines()
for line in lines:
    data = json.loads(line)
    print(getInfo(data))
    nazwa = getInfo(data)[0]
    ID = getInfo(data)[1]
    Adres = getInfo(data)[2]

    # Gówno niżej to request do bd - Zrób jak kompletne

    doc_ref = db.collection(u'lifts').document(ID)
    doc_ref.set({
    u'Nazwa': nazwa,
    u'ID': ID,
    u'Adres': Adres
     })


data = json.loads(d)
base64_message = data["BUFFER4"]["$binary"]
base64_bytes = base64_message.encode('ISO-8859-2')
message_bytes = base64.b64decode(base64_bytes)
message = message_bytes.decode('ISO-8859-2')
message = message.replace(u"\x9c", "ś") ##Naprawia bo nie bylo ś
adres = message.split("  ")
print(adres)
# print(data["BUFFER0"]["$binary"])



#Łukaszek Koding 2022