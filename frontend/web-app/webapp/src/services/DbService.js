import { addDoc, collection } from "firebase/firestore"

export class DbService{
    constructor (db) {this.db = db}
    async addLog(log){
        await addDoc(collection(this.db,'Logs'),{
            log:log
        })
    }
}