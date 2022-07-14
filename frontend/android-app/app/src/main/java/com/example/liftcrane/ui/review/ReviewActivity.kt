package com.example.liftcrane.ui.review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.liftcrane.databinding.ActivityReviewBinding
import com.example.liftcrane.endpoints.FirebaseAuthService
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.Lift
import com.example.liftcrane.model.Review
import com.google.firebase.Timestamp

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

        binding.liftName.text = lift.name
        binding.address.text = lift.address
        binding.serialNumber.text = lift.serialNumber

    }

    private fun uploadReview(malfunction:Boolean){
        val userUID = auth.getSignInUserUid() ?: return
        val review = Review(lift.serialNumber, userUID, malfunction, Timestamp.now(), "")
        fun resolve(id : String){
            Toast.makeText(this, id, Toast.LENGTH_LONG).show()
        }
        fun reject(e:Exception){
            Toast.makeText(this, "Niespodziewany błąd", Toast.LENGTH_LONG).show()
        }
        fireStore.uploadReview(
            {id -> resolve(id)},
            {e -> reject(e)},
            review
        )
    }
}