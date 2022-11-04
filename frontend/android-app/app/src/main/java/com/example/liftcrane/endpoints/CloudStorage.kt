package com.example.liftcrane.endpoints

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.liftcrane.application.GlobalApplication
import com.example.liftcrane.model.CustomImage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream


class CloudStorage {

    private val client = Firebase.storage
    private val domain = "https://firebasestorage.googleapis.com/v0/b/liftcrane-bfea5.appspot.com/o/reviews_images%2F"
    private val tasksFlag = "tasks"
    private val sharedPref = GlobalApplication
        .getAppContext()
        .getSharedPreferences("shared preferences", Context.MODE_PRIVATE)



    fun uploadReviewImage(img : CustomImage){
        client
            .reference
            .child("reviews_images")
            .child(img.id)
            .putFile(img.uri)
            .addOnFailureListener {
                Log.e("MyStorageInfoFailure", it.toString()) }
            .addOnSuccessListener { taskSnapshot ->
                deleteTask(img.uri.toString(), img.id) }

        //addTask(img.uri.toString(), img.id)
    }

    fun getImageUrl(imageId : String): String {
        return "$domain$imageId?alt=media"
    }


    fun rerunUnfinishedTasks(){
        deleteAllTasks()
        val tasks = getUnfinishedTasks()
        for(img in tasks){
            val file = File(GlobalApplication.getAppContext().cacheDir, img["id"] as String)
            val bitmap = Gson().fromJson(file.readText(), Bitmap::class.java)

            /*client
                .reference
                .child("reviews_images")
                .child(img["id"] as String)
                .put*/
        }
    }


    private fun getUnfinishedTasks(): MutableList<Map<String, String>> {
        val tasksEncoded = sharedPref.getString(tasksFlag, null)
        return if (tasksEncoded != null)
            Json.decodeFromString(tasksEncoded)
        else mutableListOf()
    }


    private fun addTask(imgUri: String, imgId: String) {
        val cacheFile = File
            .createTempFile(imgId, null, GlobalApplication.getAppContext().cacheDir)
        //val f = File(imgUri)
        //f.copyTo()
        val bitmap: Bitmap = MediaStore
            .Images
            .Media
            .getBitmap(GlobalApplication.getAppContext().contentResolver, Uri.parse(imgUri))
        cacheFile.writeText(Gson().toJson(bitmap))
        val cacheImgUri = cacheFile.toURI().toString()

        val tasks = getUnfinishedTasks()
        tasks.add(mapOf(Pair("uri", cacheImgUri), Pair("id", imgId)))
        with(sharedPref.edit()) {
            putString(tasksFlag, Json.encodeToString(tasks))
            apply()
        }
    }


    private fun deleteTask(imgUri: String, imgId: String){
        val tasks = getUnfinishedTasks()
        tasks.remove(mapOf(Pair("uri", imgUri), Pair("id", imgId)))
        with(sharedPref.edit()) {
            putString(tasksFlag, Json.encodeToString(tasks))
            apply()
        }
        File(GlobalApplication.getAppContext().cacheDir, imgId).delete()
    }

    private fun deleteAllTasks(){
        with(sharedPref.edit()) {
            putString(tasksFlag, Json.encodeToString(mutableListOf<Map<String, String>>()))
            apply()
        }
    }
}