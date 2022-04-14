package com.example.liftcrane.endpoints

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreService {

    private val client = Firebase.firestore

    fun testAdd(){
        val lift = hashMapOf(
            "first" to "Artur",
            "last" to "Laskowski",
            "born" to 1815
        )

        client.collection("test")
            .add(lift)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun testRead(){
        client.collection("test")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.id == "JfpgaWwoIqR4KayasmF6")
                        Log.i("MyInfo", document.data.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    //fun addNewReview


}