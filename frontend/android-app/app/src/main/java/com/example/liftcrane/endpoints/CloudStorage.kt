package com.example.liftcrane.endpoints

import android.R.attr.bitmap
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.liftcrane.application.GlobalApplication
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File


class CloudStorage {

    private val client = Firebase.storage
    private val domain = "https://firebasestorage.googleapis.com/v0/b/liftcrane-bfea5.appspot.com/o/reviews_images%2F"
    private val tasksFlag = "tasks"
    private val sharedPref = GlobalApplication
        .getAppContext()
        .getSharedPreferences("shared preferences", Context.MODE_PRIVATE)



    fun uploadReviewImage(imgBitmap: Bitmap, imgId: String){
        val stream = ByteArrayOutputStream()
        imgBitmap.compress(Bitmap.CompressFormat.JPEG,
            20, stream)
        val data = stream.toByteArray()
        client
            .reference
            .child("reviews_images")
            .child(imgId)
            .putBytes(data)
            .addOnFailureListener {
                //Log.e("MyStorageInfoFailure", it.toString())
            }
            .addOnSuccessListener {
                removeTaskFromCache(imgId)
            }
        addTaskToCache(data, imgId)
    }

    fun getImageUrl(imageId : String): String {
        return "$domain$imageId?alt=media"
    }

    fun rerunUnfinishedTasks(){
        val tasks = sharedPref.getStringSet(tasksFlag,
            mutableSetOf<String>()) as MutableSet<String>
        for(fileName in tasks){
            val data: ByteArray
            val file = File(GlobalApplication.getAppContext().filesDir, fileName)
            data = file.readBytes()
            client
                .reference
                .child("reviews_images")
                .child(fileName)
                .putBytes(data)
                .addOnFailureListener {
                    removeTaskFromCache(fileName)
                }
                .addOnSuccessListener {
                    removeTaskFromCache(fileName)
                }
        }
    }

    fun restartCache(){
        with (sharedPref.edit()) {
            putStringSet(tasksFlag, mutableSetOf())
            apply()
        }
    }

    private fun addTaskToCache(data: ByteArray, fileName: String){
        var tasks = sharedPref.getStringSet(tasksFlag,
            mutableSetOf<String>()) as MutableSet<String>
        tasks = tasks.toMutableSet()
        tasks.add(fileName)
        with (sharedPref.edit()) {
            putStringSet(tasksFlag, tasks)
            apply()
        }
        val file = File(GlobalApplication.getAppContext().filesDir, fileName)
        file.writeBytes(data)
    }

    private fun removeTaskFromCache(fileName: String){
        var tasks = sharedPref.getStringSet(tasksFlag,
            mutableSetOf<String>()) as MutableSet<String>
        tasks = tasks.toMutableSet()
        tasks.remove(fileName)
        with (sharedPref.edit()) {
            putStringSet(tasksFlag, tasks)
            apply()
        }
        File(GlobalApplication.getAppContext().filesDir, fileName).delete()
    }
}