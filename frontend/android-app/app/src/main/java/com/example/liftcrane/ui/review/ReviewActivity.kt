package com.example.liftcrane.ui.review

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.liftcrane.databinding.ActivityReviewBinding
import com.example.liftcrane.endpoints.CloudStorage
import com.example.liftcrane.endpoints.FirebaseAuthService
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.Lift
import com.example.liftcrane.model.Review
import com.example.liftcrane.model.CustomImage
import com.example.liftcrane.ui.IMG_CAMERA
import com.example.liftcrane.ui.IMG_GALLERY
import com.example.liftcrane.ui.LIFT_INTENT_FLAG
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ReviewActivity : AppCompatActivity() {

    private val fireStore = FirestoreService()
    private val auth = FirebaseAuthService()
    private val storage = CloudStorage()
    private val images = mutableListOf<CustomImage>()

    private lateinit var lift : Lift
    private lateinit var binding: ActivityReviewBinding
    private lateinit var currentPhotoPath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lift = intent.extras?.get(LIFT_INTENT_FLAG) as Lift

        binding.floatingActionButtonAccept.setOnClickListener {
            uploadReview()
            finish()
        }

        binding.imageButtonGallery.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, IMG_GALLERY)
        }

        binding.imageButtonCamera.setOnClickListener{
            dispatchTakePictureIntent()
        }
    }

    private fun uploadReview(){
        for(img in images) {
            storage.uploadReviewImage(img)
        }

        GlobalScope.launch{
            val userUid = auth.getSignInUserUid()
            val user = fireStore.getUserById(userUid!!)
            if(user != null){
                val review = Review(
                    null,
                    lift.id,
                    lift.name ?: "",
                    userUid,
                    user.firstName + " " + user.lastName,
                    binding.malfunctionCheckBox.isChecked,
                    binding.dtrCheckBox.isChecked,
                    binding.udtCheckBox.isChecked,
                    Timestamp.now(),
                    binding.descriptionEditText.text.toString(),
                    images.map{it.id}.toList(),
                    binding.liftWorkSwitch.isChecked
                )
                fireStore.uploadReview(review)
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMG_GALLERY) {
            val imageUri = data?.data
            if(imageUri != null) {
                images.add(CustomImage(UUID.randomUUID().toString(), imageUri))
                initListView()
            }
        }
        else if (requestCode == IMG_CAMERA) {
            val uri = Uri.fromFile(File(currentPhotoPath))
            galleryAddPic(uri)
            images.add(CustomImage(UUID.randomUUID().toString(), uri))
            initListView()
        }
    }

    private fun initListView() {
        binding.imgList.layoutManager = LinearLayoutManager(this)
        val adapter = ImgRecyclerAdapter(images.toTypedArray())
        adapter.onClickDelete = {position ->
            images.removeAt(position)
            initListView()
        }
        binding.imgList.adapter = adapter
    }


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, IMG_CAMERA)
                }
            }
        }
    }

    private fun galleryAddPic(uri: Uri) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            mediaScanIntent.data = uri
            sendBroadcast(mediaScanIntent)
        }
    }

}