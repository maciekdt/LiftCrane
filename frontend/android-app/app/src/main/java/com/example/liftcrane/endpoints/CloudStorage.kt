package com.example.liftcrane.endpoints

import android.util.Log
import com.example.liftcrane.model.CustomImage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class CloudStorage {

    private val client = Firebase.storage
    private val domain = "https://firebasestorage.googleapis.com/v0/b/liftcrane-bfea5.appspot.com/o/reviews_images%2F"

    fun uploadReviewImage(img : CustomImage){
        val reviewImgRef = client
            .reference
            .child("reviews_images")
            .child(img.id)

        val uploadTask = reviewImgRef.putFile(img.uri)
        uploadTask.addOnFailureListener {
            Log.e("MyStorageInfo", it.toString())
        }
            .addOnSuccessListener { taskSnapshot ->
                Log.i("MyStorageInfo", "Dodano zdjęcie")
        }
    }

    fun getImageUrl(imageId : String): String {
        return "$domain$imageId?alt=media"
    }
}