package com.example.liftcrane.ui.reviewslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.liftcrane.R
import com.example.liftcrane.databinding.ActivityReviewBinding
import com.example.liftcrane.databinding.ActivityReviewsListBinding
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.Review
import com.example.liftcrane.ui.account.AccountActivity
import com.example.liftcrane.ui.lift.ReviewRecyclerAdapter
import com.example.liftcrane.ui.liftslist.LiftsListActivity
import com.example.liftcrane.ui.qrscanner.QRScannerActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ReviewsListActivity : AppCompatActivity() {

    private val fireStore = FirestoreService()

    private lateinit var binding: ActivityReviewsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListView()
        setBottomBar()
    }


    private fun initListView(){
        fun resolve(reviews : MutableList<Review>){
            binding.list.layoutManager = LinearLayoutManager(this)
            val adapter = ExtendReviewRecyclerAdapter(reviews.toTypedArray())
            //adapter.onItemClick = { lift ->
            //startReviewActivity(lift)
            //}
            binding.list.adapter = adapter
        }

        fun reject(e:Exception){
        }

        fireStore.getAllReviews(
            {reviews -> resolve(reviews)},
            {e -> reject(e)}
        )
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