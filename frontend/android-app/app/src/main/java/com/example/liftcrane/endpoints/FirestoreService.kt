package com.example.liftcrane.endpoints

import android.util.Log
import com.example.liftcrane.model.Lift
import com.example.liftcrane.model.Review
import com.example.liftcrane.model.User
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirestoreService {

    private val client = Firebase.firestore
    private val devicesCollectionPath = "devices"
    private val reviewsCollectionPath = "reviews"
    private val usersCollectionPath = "users"


    suspend fun getLiftById(liftId: String): Lift?{
        val document = client
            .collection(devicesCollectionPath)
            .document(liftId)
            .get()
            .await()

        if(document.data != null)
            return Lift(nullTransformMap(document.data as Map<String, Any>), document.id)
        return null
    }

    suspend fun updateLift(lift: Lift){
        client
            .collection(devicesCollectionPath)
            .document(lift.id)
            .set(lift.toHashMap())
            .await()
    }

    suspend fun deleteLift(liftId: String){
        client
            .collection(devicesCollectionPath)
            .document(liftId)
            .delete()
            .await()
    }


    suspend fun getAllLifts(): MutableList<Lift> {
        val documents = client
            .collection(devicesCollectionPath)
            .get()
            .await()

        val resultList = mutableListOf<Lift>()
        for (document in documents) {
            val lift = Lift(nullTransformMap(document.data), document.id)
            try {
                resultList.add(lift)
            }
            catch (e:NullPointerException) {
                Log.e("MyInfo", "Null lift :${document.data}")
            }
        }
        return resultList
    }


    suspend fun getUserById(userId: String): User? {
        val document = client
            .collection(usersCollectionPath)
            .document(userId)
            .get()
            .await()

        if(document.data != null)
            return User(document.data!!)
        return null
    }


    suspend fun getReviewById(reviewId: String): Review?{
        val document = client
            .collection(reviewsCollectionPath)
            .document(reviewId)
            .get()
            .await()

        var review : Review? = null
        if(document.data != null) {
            review = Review(document.data!!, document.id)
        }
        return review
    }


    suspend fun uploadReview(review: Review){
        client
            .collection(reviewsCollectionPath)
            .add(review.toHashMap())
            .await()
    }


    suspend fun deleteReviewById(reviewId: String){
        client
            .collection(reviewsCollectionPath)
            .document(reviewId)
            .delete()
            .await()
    }


    suspend fun getAllReviewsForLift(liftId: String): MutableList<Review> {
        val documents = client
            .collection(reviewsCollectionPath)
            .whereEqualTo("liftId", liftId)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()

        val resultList = mutableListOf<Review>()
        for (document in documents) {
            val review = Review(document.data, document.id)
            try {
                resultList.add(review)
            }
            catch (e:NullPointerException) {
                Log.e("MyInfo", "Null lift :${document.data}")
            }
        }
        return resultList
    }





    suspend fun getAllReviews(): MutableList<Review> {
        val documents = client
            .collection(reviewsCollectionPath)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()

        val resultList = mutableListOf<Review>()
        for (document in documents) {
            val review = Review(document.data, document.id)
            try {
                resultList.add(review)
            }
            catch (e:NullPointerException) {
                Log.e("MyInfo", "Null lift :${document.data}")
            }
        }
        return resultList
    }


    fun onChangeReviews(action:(reviews : MutableList<Review>) -> Unit){
        client.collection(reviewsCollectionPath)
            //.orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { result, exception ->
                if(exception == null && result != null){
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


    fun onChangeReviewsForLift(action:(reviews : MutableList<Review>) -> Unit, liftId: String){
        client.collection(reviewsCollectionPath)
            .whereEqualTo("liftId", liftId)
            //.orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { result, exception ->
                if(exception == null && result != null){
                    val resultList = mutableListOf<Review>()
                    for (document in result) {
                        val review = Review(document.data, document.id)
                        try {
                            resultList.add(review)
                        }
                        catch (e:NullPointerException) {
                            Log.e("MyInfo", "Null lift :${document.data}")
                        }
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