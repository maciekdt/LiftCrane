package com.example.liftcrane.ui.reviewslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.liftcrane.R
import com.example.liftcrane.databinding.ActivityReviewsListBinding
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.Review
import com.example.liftcrane.ui.REVIEW_ID_INTENT_FLAG
import com.example.liftcrane.ui.account.AccountActivity
import com.example.liftcrane.ui.liftslist.LiftsListActivity
import com.example.liftcrane.ui.qrscanner.QRScannerActivity
import com.example.liftcrane.ui.reviewpreview.ReviewPreviewActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ReviewsListActivity : AppCompatActivity() {

    private val fireStore = FirestoreService()

    private lateinit var binding: ActivityReviewsListBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListView(this)
        setBottomBar()
    }


    private fun initListView(activity: AppCompatActivity){
        binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            val reviews = fireStore.getAllReviews()
            binding.list.layoutManager = LinearLayoutManager(activity)
            val adapter = ExtendReviewRecyclerAdapter(reviews.toTypedArray())
            adapter.onItemClick = { review ->
                val intent = Intent(activity, ReviewPreviewActivity::class.java)
                intent.putExtra(REVIEW_ID_INTENT_FLAG, review.id)
                startActivity(intent)
            }
            binding.list.adapter = adapter
        }
    }

    private fun setBottomBar(){
        binding.bottomNavigation.selectedItemId = R.id.reviews
        binding.bottomNavigation.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.lifts -> {
                        startActivity(Intent(applicationContext, LiftsListActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.scanner -> {
                        startActivity(Intent(applicationContext, QRScannerActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.account -> {
                        startActivity(Intent(applicationContext, AccountActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.reviews -> return@OnNavigationItemSelectedListener true
                }
                false
            })
    }
}