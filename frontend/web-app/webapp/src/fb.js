// Import the functions you need from the SDKs you need
import firebase from "firebase/app";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyDHLd9-eYC4TDqvpQDjA_r3FoG_XGtaYh8",
  authDomain: "liftcrane-bfea5.firebaseapp.com",
  projectId: "liftcrane-bfea5",
  storageBucket: "liftcrane-bfea5.appspot.com",
  messagingSenderId: "18795223195",
  appId: "1:18795223195:web:2d034f763cb0e4c1c5ce3e",
  measurementId: "G-GH19K00X9B"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);
const db = firebase.firestore();
const auth = firebase.auth();

const storage = firebase.storage();
const storageRef = storage.ref();
const imagesRef = storageRef.child('reviews_images')

db.enablePersistence();

export {auth, db, imagesRef};


