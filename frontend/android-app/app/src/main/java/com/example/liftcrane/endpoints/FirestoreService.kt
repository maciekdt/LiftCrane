package com.example.liftcrane.endpoints

import android.content.ContentValues.TAG
import android.util.Log
import com.example.liftcrane.model.Lift
import com.example.liftcrane.model.Review
import com.example.liftcrane.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreService {

    private val client = Firebase.firestore

    fun uploadReview(resolve:(id : String) -> Unit,
                     reject:(e:Exception) -> Unit,
                     review: Review){

        val collectionPath = "reviews"
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
                    if(document.id == liftId){
                        lift = Lift(document.data, document.id)
                    }
                }
                resolve(lift)
            }
            .addOnFailureListener { e ->
                reject(e)
            }
    }

    fun getAllLifts(resolve:(lifts : MutableList<Lift>) -> Unit,
                    reject:(e : Exception) -> Unit){

        Log.d("MyInfo", "XDDD")
        val collectionPath = "lifts"
        client.collection(collectionPath)
            .get()
            .addOnSuccessListener { result ->
                val resultList = mutableListOf<Lift>()
                for (document in result) {
                    val lift = Lift(document.data, document.id)

                    try {resultList.add(lift)}
                    catch (e:NullPointerException) {Log.e("MyInfo", "Null lift :${document.data}")}
                }
                resolve(resultList)
            }
            .addOnFailureListener { exception ->
                reject(exception)
            }
    }

    fun getUserById(resolve:(user : User?) -> Unit,
                    reject:(e : Exception) -> Unit,
                    userId: String){

        val collectionPath = "users"
        client.collection(collectionPath)
            .get()
            .addOnSuccessListener { result ->
                var user:User? = null
                for (document in result) {
                    if(document.id == userId)
                        user = User(document.data)
                }
                resolve(user)
            }
            .addOnFailureListener { e ->
                reject(e)
            }
    }
}