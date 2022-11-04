package com.example.liftcrane.endpoints

import android.R.attr.bitmap
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.liftcrane.application.GlobalApplication
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class CloudStorage {

    private val client = Firebase.storage
    private val domain = "https://firebasestorage.googleapis.com/v0/b/liftcrane-bfea5.appspot.com/o/reviews_images%2F"
    private val tasksFlag = "tasks"
    private val sharedPref = GlobalApplication
        .getAppContext()
        .getSharedPreferences("shared preferences", Context.MODE_PRIVATE)



    fun uploadReviewImage(imgBitmap: Bitmap, imgId: String){
        val stream = ByteArrayOutputStream()
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 1, stream)
        val data = stream.toByteArray()
        client
            .reference
            .child("reviews_images")
            .child(imgId)
            .putBytes(data)
            .addOnFailureListener {
                Log.e("MyStorageInfoFailure", it.toString()) }
            .addOnSuccessListener { taskSnapshot ->
                //deleteTask(img.uri.toString(), img.id)
            }

        //addTask(img.uri.toString(), img.id)
    }

    fun getImageUrl(imageId : String): String {
        return "$domain$imageId?alt=media"
    }
}