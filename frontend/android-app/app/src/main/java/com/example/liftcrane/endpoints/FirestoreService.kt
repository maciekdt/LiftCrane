package com.example.liftcrane.endpoints

import android.content.ContentValues.TAG
import android.util.Log
import com.example.liftcrane.model.Lift
import com.example.liftcrane.model.Review
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreService {

    private val client = Firebase.firestore

    fun test(){
        val review = Review("windex", "maciek", true, 5675465)
        val collectionPath = "review"
        client.collection(collectionPath)
            .add(review.toHashMap())
            .addOnSuccessListener { documentReference ->

            }
            .addOnFailureListener { e ->
                Log.e("MyInfo", e.toString())
            }
    }

    fun uploadReview(resolve:(id : String) -> Unit,
                     reject:(e:Exception) -> Unit,
                     review: Review){

        val collectionPath = "review"
        client.collection(collectionPath)
            .add(review.toHashMap())
            .addOnSuccessListener { documentReference ->
                resolve(documentReference.id)
            }
            .addOnFailureListener { e ->
                reject(e)
            }
    }


    fun getLiftById(resolve:(lift : Lift) -> Unit,
                    reject:(e : Exception) -> Unit,
                    liftId: String){

        val collectionPath = "lifts"
        client.collection(collectionPath)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.id == liftId)
                        resolve(Lift(document.data))
                }
            }
            .addOnFailureListener { e ->
                reject(e)
            }
    }
}