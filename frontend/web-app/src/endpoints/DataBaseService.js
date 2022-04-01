import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
import { collection, getDocs, query, where, doc, setDoc, Timestamp } from "firebase/firestore";
import { v4 as uuidv4 } from 'uuid';

class DataBaseService {
    firebaseConfig = {
        apiKey: "AIzaSyDHLd9-eYC4TDqvpQDjA_r3FoG_XGtaYh8",
        authDomain: "liftcrane-bfea5.firebaseapp.com",
        projectId: "liftcrane-bfea5",
        storageBucket: "liftcrane-bfea5.appspot.com",
        messagingSenderId: "18795223195",
        appId: "1:18795223195:web:2d034f763cb0e4c1c5ce3e",
        measurementId: "G-GH19K00X9B"
    };

    firebaseApp = initializeApp(this.firebaseConfig)
    firestore = getFirestore(this.firebaseApp)


    async getReviews() {
        let q = query(collection(this.firestore, "reviews"), where("malfunction", "==", false));
        let querySnapshot = await getDocs(q);
        querySnapshot.forEach((doc) => {
            console.log(doc.data());
        });
    }

    async addReview() {
        await setDoc(doc(this.firestore, "reviews", uuidv4()), {
            deviceName: "Windna komercyjna LIFTER UU",
            date: Timestamp.fromMillis(Date.now()),
            serialNumber: "YT6785",
            malfunction: true
        });
    }
}

export default new DataBaseService();