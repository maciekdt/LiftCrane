package com.example.liftcrane.ui.review

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
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
import com.example.liftcrane.ui.IMG_GALLERY
import com.example.liftcrane.ui.LIFT_INTENT_FLAG
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.util.*

class ReviewActivity : AppCompatActivity() {

    private val fireStore = FirestoreService()
    private val auth = FirebaseAuthService()
    private val storage = CloudStorage()
    private val images = mutableListOf<CustomImage>()

    private lateinit var lift : Lift
    private lateinit var binding: ActivityReviewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lift = intent.extras?.get(LIFT_INTENT_FLAG) as Lift

        binding.floatingActionButtonAccept.setOnClickListener {
            showReviewDialog()
        }

        binding.floatingActionButtonGalery.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, IMG_GALLERY)
        }
    }

    private fun uploadReview(){
        binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch{
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
                    Timestamp.now(),
                    binding.descriptionEditText.text.toString(),
                    images.map{it.id}.toList()
                )
                fireStore.uploadReview(review)
                for(img in images)
                    storage.uploadReviewImage(img)
            }
        }
    }

    private fun showReviewDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Potwierdź przeprowadzenie serwisu")
            .setMessage("Czy na pewno chcesz zgłosić przeprowadzenie serwisu windy " + lift.name + "?")
            .setNegativeButton("Anuluj", null)
            .setPositiveButton("Potwierdź") { dialog, which ->
                uploadReview()
                finish()
            }
            .setCancelable(false)
            .show()
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

}