package com.example.liftcrane.endpoints

import android.content.ContentValues.TAG
import android.util.Log
import com.example.liftcrane.model.Lift
import com.example.liftcrane.model.Review
import com.example.liftcrane.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Array

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

        val collectionPath = "devices"
        client.collection(collectionPath)
            .document(liftId)
            .get()
            .addOnSuccessListener { document ->
                var lift:Lift? = null
                if(document.data != null)
                    lift = Lift(nullTransformMap(document.data as Map<String, Any>), document.id)
                resolve(lift)
            }
            .addOnFailureListener { e ->
                reject(e)
            }
    }


    fun getAllLifts(resolve:(lifts : MutableList<Lift>) -> Unit,
                    reject:(e : Exception) -> Unit){

        val collectionPath = "devices"
        client.collection(collectionPath)
            .get()
            .addOnSuccessListener { documentsList ->
                val resultList = mutableListOf<Lift>()
                for (document in documentsList) {
                    val lift = Lift(nullTransformMap(document.data), document.id)

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


    fun getReviewById(resolve:(review : Review?) -> Unit,
                      reject:(e : Exception) -> Unit,
                      reviewId: String){

        val collectionPath = "reviews"
        client.collection(collectionPath)
            .document(reviewId)
            .get()
            .addOnSuccessListener { document ->
                var review : Review? = null
                if(document.data != null) {
                    review = Review(document.data!!, document.id)
                }
                resolve(review)
            }
            .addOnFailureListener { exception ->
                reject(exception)
            }
    }


    fun getAllReviewsForLift(resolve:(reviews : MutableList<Review>) -> Unit,
                             reject:(e : Exception) -> Unit,
                             liftId: String){

        val collectionPath = "reviews"
        client.collection(collectionPath)
            .whereEqualTo("liftId", liftId)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val resultList = mutableListOf<Review>()
                for (document in result) {
                    val review = Review(document.data, document.id)
                    try { resultList.add(review) }
                    catch (e:NullPointerException) { Log.e("MyInfo", "Null lift :${document.data}") }
                }
                resolve(resultList)
            }
            .addOnFailureListener { exception ->
                reject(exception)
            }
    }


    fun onChangeReviewsForLift(action:(reviews : MutableList<Review>) -> Unit,
                               liftId: String){

        val collectionPath = "reviews"
        client.collection(collectionPath)
            .whereEqualTo("liftId", liftId)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { result, exception ->
                if(exception == null && result != null && result.metadata.hasPendingWrites()){
                    val resultList = mutableListOf<Review>()
                    for (document in result) {
                        val review = Review(document.data, document.id)
                        try { resultList.add(review) }
                        catch (e:NullPointerException) { Log.e("MyInfo", "Null lift :${document.data}") }
                    }
                    action(resultList)
                }
            }
    }


    fun getAllReviews(resolve:(reviews : MutableList<Review>) -> Unit,
                             reject:(e : Exception) -> Unit){

        val collectionPath = "reviews"
        client.collection(collectionPath)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val resultList = mutableListOf<Review>()
                for (document in result) {
                    val review = Review(document.data, document.id)
                    try { resultList.add(review) }
                    catch (e:NullPointerException) { Log.e("MyInfo", "Null lift :${document.data}") }
                }
                resolve(resultList)
            }
            .addOnFailureListener { exception ->
                reject(exception)
            }
    }


    fun onChangeReviews(action:(reviews : MutableList<Review>) -> Unit){
        val collectionPath = "reviews"
        client.collection(collectionPath)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { result, exception ->
                if(exception == null && result != null && result.metadata.hasPendingWrites()){
                    val resultList = mutableListOf<Review>()
                    for (document in result) {
                        val review = Review(document.data, document.id)
                        try { resultList.add(review) }
                        catch (e:NullPointerException) { Log.e("MyInfo", "Null lift :${document.data}") }
                    }
                    action(resultList)
                }
            }
    }


    private fun nullTransformMap(map : Map<String, Any>): Map<String, Any?> {
        val nullSigns = setOf("", "NA")
        val newMap = mutableMapOf<String, Any?>()
        for (entry in map) {
            if(entry.value is String  &&  nullSigns.contains(entry.value))
                newMap[entry.key] = null
            else
                newMap[entry.key] = entry.value
        }
        return newMap
    }
}