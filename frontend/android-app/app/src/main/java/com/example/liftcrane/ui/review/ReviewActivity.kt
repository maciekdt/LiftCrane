package com.example.liftcrane.ui.review

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.liftcrane.R
import com.example.liftcrane.databinding.ActivityReviewBinding
import com.example.liftcrane.endpoints.FirebaseAuthService
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.Lift
import com.example.liftcrane.model.Review
import com.example.liftcrane.ui.liftslist.LiftsListActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import java.net.URLEncoder

class ReviewActivity : AppCompatActivity() {

    private val fireStore = FirestoreService()
    private val auth = FirebaseAuthService()

    private lateinit var lift : Lift
    private lateinit var binding: ActivityReviewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lift = intent.extras?.get("lift") as Lift

        binding.acceptButton.setOnClickListener {
            showReviewDialog()
        }

        binding.dismissButton.setOnClickListener {
            finish()
        }
    }

    private fun uploadReview(){
        val userUid = auth.getSignInUserUid() ?: return
        fireStore.getUserById(
            { user ->
                if(user != null) {
                    val review = Review(
                        lift.id,
                        lift.name ?: "",
                        userUid,
                        user.firstName + " " + user.lastName,
                        binding.malfunctionCheckBox.isChecked,
                        Timestamp.now(),
                        binding.descriptionEditText.text.toString()
                    )

                    fun resolve(id: String) {
                        //Snackbar.make(binding.constraintLayout, "Dodano zgłoszenie", Snackbar.LENGTH_SHORT)
                        //.show()
                        //Toast.makeText(this, "Dodano zgłoszenie", Toast.LENGTH_LONG).show()
                    }

                    fun reject(e: Exception) {
                        Toast.makeText(this, "Niespodziewany błąd", Toast.LENGTH_LONG).show()
                    }
                    fireStore.uploadReview(
                        { id -> resolve(id) },
                        { e -> reject(e) },
                        review
                    )
                }

            },
            { e ->
                Toast.makeText(this, "Niespodziewany błąd", Toast.LENGTH_LONG).show()
            },
            userUid
        )



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
}