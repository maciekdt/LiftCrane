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
        setLiftData()

        binding.mapButton.setOnClickListener {
            launchGoogleMap()
        }

    }

    private fun uploadReview(malfunction:Boolean){
        val userUID = auth.getSignInUserUid() ?: return
        val review = Review(lift.id, userUID, malfunction, Timestamp.now(), "")
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

    @SuppressLint("SetTextI18n")
    private fun setLiftData(){
        if(lift.name != null) binding.liftName.text = lift.name
        else binding.liftName.text = ""

        if(lift.serialNumber != null) binding.serialNumber.text = lift.serialNumber
        else binding.serialNumber.text = ""

        if(lift.udtSerialNumber != null) binding.udtNumber.text = lift.udtSerialNumber
        else binding.udtNumber.text = ""

        if(lift.localization != null) binding.address.text = lift.localization
        else binding.address.text = ""

        if(lift.producer != null) binding.producer.text = lift.producer
        else binding.producer.text = ""

        if(lift.liftingCapacity != null) binding.capacity.text = lift.liftingCapacity + getString(R.string.lifting_capacity_unit)
        else binding.capacity.text = ""
    }


    private fun launchGoogleMap(){
        val domain = "https://www.google.com/maps/search/?api=1&query="
        val query = URLEncoder.encode(lift.localization, "utf-8")
        val url = domain + query

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}