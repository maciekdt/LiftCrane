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


    fun getLiftById(resolve:(lift : Lift?) -> Unit,
                    reject:(e : Exception) -> Unit,
                    liftId: String){

        val collectionPath = "lifts"
        client.collection(collectionPath)
            .get()
            .addOnSuccessListener { result ->
                var lift:Lift? = null
                for (document in result) {
                    if(document.id == liftId)
                        lift = Lift(document.data)
                }
                resolve(lift)
            }
            .addOnFailureListener { e ->
                reject(e)
            }
    }
}