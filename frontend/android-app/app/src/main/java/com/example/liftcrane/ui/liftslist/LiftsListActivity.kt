package com.example.liftcrane.ui.liftslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.liftcrane.R
import com.example.liftcrane.databinding.ActivityLiftsListBinding
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.Lift
import com.example.liftcrane.ui.LIFT_INTENT_FLAG
import com.example.liftcrane.ui.account.AccountActivity
import com.example.liftcrane.ui.lift.LiftActivity
import com.example.liftcrane.ui.qrscanner.QRScannerActivity
import com.example.liftcrane.ui.reviewslist.ReviewsListActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class LiftsListActivity : AppCompatActivity() {

    private val fireStore = FirestoreService()

    private lateinit var binding: ActivityLiftsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiftsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomBar()
        initListView()
    }

    private fun initListView(){
        fun resolve(lifts : MutableList<Lift>){
            binding.list.layoutManager = LinearLayoutManager(this)
            val adapter = LiftRecyclerAdapter(lifts.toTypedArray())
            adapter.onItemClick = { lift ->
                startReviewActivity(lift)
            }
            binding.list.adapter = adapter
        }

        fun reject(e:Exception){
        }

        fireStore.getAllLifts(
            {lifts -> resolve(lifts)},
            {e -> reject(e)}
        )
    }

    private fun setBottomBar(){
        binding.bottomNavigation.selectedItemId = R.id.lifts

        binding.bottomNavigation.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.account -> {
                        startActivity(Intent(applicationContext, AccountActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.scanner -> {
                        startActivity(Intent(applicationContext, QRScannerActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.reviews -> {
                        startActivity(Intent(applicationContext, ReviewsListActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.lifts -> return@OnNavigationItemSelectedListener true
                }
                false
            })
    }

    private fun startReviewActivity(lift:Lift){
        val intent = Intent(this, LiftActivity::class.java)
        intent.putExtra(LIFT_INTENT_FLAG, lift)
        startActivity(intent)
    }
}