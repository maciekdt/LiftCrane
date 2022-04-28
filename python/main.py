import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import json
import base64

# BUFFER0 - nazwa i kod!!!
# BuFFER 1 - ADRES i wlasciciel i nip

cred = credentials.Certificate("liftcrane-bfea5-firebase-adminsdk-fo2gl-c374ac1430.json")
firebase_admin.initialize_app(cred)
db = firestore.client()
d = '{"HELP":342,"FIELD 0":"AMW HOTELE SP. Z O.O","BUFFER0":{"$binary": "ICAgK1RZICAgIEsgICA+SzAgICAgQU1XIEhPVEVMRSBTUC4gWiBPLk8gICD1X0kgICAge1xydGYxXGFuc2lcZGVmZjBcbm91aWNvbXBhdHtcZm9udHRibHtcZjBcZm5pbFxmY2hhcnNldDIzOCBBcmlhbDt9fQ0Ke1xjb2xvcnRibCA7XHJlZDBcZ3JlZW4wXGJsdWUwO30NCntcKlxnZW5lcmF0b3IgUmljaGVkMjAgMTAuMC4xNDM5M31cdmlld2tpbmQ0XHVjMSANClxwYXJkXGNmMVxmMFxmczIwXGxhbmcxMDQ1IE5vd291dHdvcnpvbnlccGFyDQp9DQog", "$type": "00"},"BUFFER1":{"$binary": "ICArUjggICAgMSAgICtyayAgICAwICAgK3JrICAgITAgICArcmsgICAiMCAgICtyayAgICMwICAgNERBICAgIDxTVEFOREFSRD4gICAwSUUgICAgNzM2OTEwICAgQ0MwICAgIHBwaHd2d2FqZkswMTAwMDAwMDAwMDAwMDAgICA0REggICAgMjAzMDM4OTEyOCAgIDBMRCAgICA3MzcwNzIgICAvTFEgICAgNDM5ODAgICAtTE0gICAgODkxICAgLlIzICAgIDAuMDAgICAuUjEgICAgMC4wMCAgIC5SMiAgICAwLjAwICAgLFI0ICAgIDIxICAgN1VJICAgIDY3", "$type": "00"},"BUFFER2":{"$binary": "Ni0yMjctODQtMDYgICAtV1ogICAgUExOICAgP04xICAgIEFNVyBIb3RlbGUgU3AuIHogby5vLiAgIENONCAgICBVbC4gjHcuIEphY2thIE9kcm93ub9hIDE1ICAgOU42ICAgIDAzLTMxMCBXYXJzemF3YSAgIDtEMSAgICBTemFub3duaSBQYfFzdHdvLCAgIDlOOSAgICAwMy0zMTAgV2Fyc3phd2EgICAwS1YgICAgNzM3MTA1", "$type": "00"},"BUFFER4":{"$binary": "ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg", "$type": "00"},"BUFFER5":{"$binary": "ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA==", "$type": "00"},"STATUS":" ","FIELD 1":"K                             AMW HOTELE SP. Z O.O","FIELD 2":"03-310 Warszawa               AMW HOTELE SP. Z O.O","FIELD 14":"pphwvwajfK010000000000000     AMW HOTELE SP. Z O.O"}'
data = json.loads(d)
base64_message = data["BUFFER1"]["$binary"]
base64_bytes = base64_message.encode('ISO-8859-1')
message_bytes = base64.b64decode(base64_bytes)
message = message_bytes.decode('ISO-8859-1')
message = message.replace(u"\x9c", "ś") ##Naprawia bo nie bylo ś
adres = message.split("  ")
print(adres)
#czasem jebany w dupe id jest na 11 indeksie kurwa jego mac kto to tak napisal jebane downy
#juz w drugim przypadku jest chujowo. jakis chujowy napis nowoutworzony jest w 0 - to moze pomoc
def getNazwa(text):
    x = text.index("  ",10) #zwraca pozycje "  " dwóch spacji, szuka od 10 znaku
    return text[10:x]       #zwraca tekst od 10 znaku do x zanku (koniec nazwy)

def getInfo (data):
    #Wyciaga nazwe
    base64_message = data["BUFFER0"]["$binary"]
    base64_bytes = base64_message.encode('ISO-8859-2')
    message_bytes = base64.b64decode(base64_bytes)
    message = message_bytes.decode('ISO-8859-2')
    nazwa = data["FIELD 0"]

    z = message.split("  ")
    # z[30] = "N"+z[30]

    base64_message = data["BUFFER1"]["$binary"]
    base64_bytes = base64_message.encode('ISO-8859-2')
    message_bytes = base64.b64decode(base64_bytes)
    message = message_bytes.decode('ISO-8859-2')
    message = message.replace(u"\x9c", "ś") ##Naprawia bo nie bylo ś
    adres = message.split("  ")
    miejscowosc = adres[6]+adres[7]
    # miejscowosc = miejscowosc.strip()
    ulica = adres[3] + adres[4]

    

    try:
        if(z[30].isnumeric() == 0):
            z[30] = 'null'
        z = 'N'+z[30]
    except IndexError:
        try:
            z = 'N'+z[11]
        except IndexError:
                z = 'N'+adres[24]

    
    # print("NAZWA: "+data["FIELD 0"])
    # print("ID: "+z[30])
    # print("ADRES: "+ miejscowosc + " "+ ulica)
    return data["FIELD 0"],z, miejscowosc + " "+ ulica # dodaj!

#23 pozycja w binary0
# def getID(text):

f = open('adres_main_json.txt','r', encoding="ISO-8859-2")
lines = f.readlines()
for line in lines:
    data = json.loads(line)
    print(getInfo(data))
    nazwa = getInfo(data)[0]
    ID = getInfo(data)[1]
    Adres = getInfo(data)[2]
    doc_ref = db.collection(u'lifts').document(nazwa)
    doc_ref.set({
    u'Nazwa': nazwa,
    u'ID': ID,
    u'Adres': Adres
    })
# print(data["BUFFER0"]["$binary"])

# base64_message = data["BUFFER1"]["$binary"]
# base64_bytes = base64_message.encode('ISO-8859-2')
# message_bytes = base64.b64decode(base64_bytes)
# message = message_bytes.decode('ISO-8859-2')

# print(message)



# data = json.loads(d)

# base64_message = data["BUFFER0"]["$binary"]
# base64_bytes = base64_message.encode('ISO-8859-2')
# message_bytes = base64.b64decode(base64_bytes)
# message = message_bytes.decode('ISO-8859-2')

# print(message)
