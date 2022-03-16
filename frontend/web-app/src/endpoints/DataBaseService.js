import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
import { collection, getDocs } from "firebase/firestore";

const firebaseConfig = {
    apiKey: "AIzaSyDG_pFjKssAOFtBc19A9eJvEbuNHcQnOYU",
    authDomain: "test-f0349.firebaseapp.com",
    projectId: "test-f0349",
    storageBucket: "test-f0349.appspot.com",
    messagingSenderId: "110874552011",
    appId: "1:110874552011:web:a2d27359c97503d5619d4e",
    measurementId: "G-PTN9MMT7EM"
};

const firebaseApp = initializeApp(firebaseConfig);
const firestore = getFirestore(firebaseApp);

let service = {
    test: async function() {
        let querySnapshot = await getDocs(collection(firestore, "Tasks"));
        querySnapshot.forEach((doc) => {
            console.log(`${doc.id} => ${doc.data().liftName}`);
        });
    }
}

export default service;