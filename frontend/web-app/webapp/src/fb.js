import firebase from "firebase/app";
import 'firebase/auth';
import "firebase/firestore";


const firebaseConfig = {
  apiKey: "AIzaSyDHLd9-eYC4TDqvpQDjA_r3FoG_XGtaYh8",
  authDomain: "liftcrane-bfea5.firebaseapp.com",
  projectId: "liftcrane-bfea5",
  storageBucket: "liftcrane-bfea5.appspot.com",
  messagingSenderId: "18795223195",
  appId: "1:18795223195:web:2d034f763cb0e4c1c5ce3e",
  measurementId: "G-GH19K00X9B"

};

firebase.initializeApp(firebaseConfig)

export const db = firebase.firestore()
export const auth = firebase.auth();

