package com.example.liftcrane.ui.reviewpreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.liftcrane.databinding.ActivityReviewPreviewBinding
import com.example.liftcrane.endpoints.CloudStorage
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.CustomImage
import com.example.liftcrane.model.Review
import com.example.liftcrane.ui.REVIEW_ID_INTENT_FLAG
import com.example.liftcrane.ui.review.ImgRecyclerAdapter
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat

class ReviewPreviewActivity : AppCompatActivity() {

    private val fireStore = FirestoreService()
    private val storage = CloudStorage()
    private val images = mutableListOf<CustomImage>()

    private lateinit var review : Review
    private lateinit var binding: ActivityReviewPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val activity = this

        binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            val reviewId = intent.extras?.get(REVIEW_ID_INTENT_FLAG) as String
            val review = fireStore.getReviewById(reviewId)
            if (review != null) {
                activity.review = review
                displayReviewTextData()
                initListView()
            }
        }
    }

    private fun displayReviewTextData(){
        binding.textViewLift.text = review.liftName
        binding.textViewDescription.text = review.description
        binding.textViewReviewerName.text = review.reviewerName

        val pattern = "EEE d MMM yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(review.date.toDate())
        binding.textViewDate.text = date

        if(review.malfunction)
            binding.textViewMalfunction.text = "Tak"
        else
            binding.textViewMalfunction.text = "Nie"
    }

    private fun initListView() {
        binding.imgList.layoutManager = LinearLayoutManager(this)
        val adapter = ImgPreviewRecyclerAdapter(review.images.toTypedArray())
        /*adapter.onClickDelete = {position ->
            images.removeAt(position)
            initListView()
        }*/
        binding.imgList.adapter = adapter
    }
}