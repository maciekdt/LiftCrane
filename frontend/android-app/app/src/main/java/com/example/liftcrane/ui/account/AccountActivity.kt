package com.example.liftcrane.ui.account

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.liftcrane.R
import com.example.liftcrane.databinding.ActivityAccountBinding
import com.example.liftcrane.databinding.ActivityLiftsListBinding
import com.example.liftcrane.endpoints.FirebaseAuthService
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.User
import com.example.liftcrane.ui.liftslist.LiftsListActivity
import com.example.liftcrane.ui.menu.MainMenuActivity
import com.example.liftcrane.ui.qrscanner.QRScannerActivity
import com.example.liftcrane.ui.reviewslist.ReviewsListActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class AccountActivity : AppCompatActivity() {

    private val auth = FirebaseAuthService()
    private val fireStore = FirestoreService()

    private lateinit var user: User
    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val activity = this

        binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            activity.user = fireStore.getUserById(auth.getSignInUserUid()!!)!!
            setLabels()
            setBottomBar()

        }

        binding.buttonLogOut.setOnClickListener {
            auth.signOut()
            finishAffinity()
        }
    }

    override fun onResume() {
        super.onResume()
        setBottomBar()
    }


    @SuppressLint("SetTextI18n")
    private fun setLabels(){
        if(this::user.isInitialized){
            binding.accountFirstLetter.text = user.firstName[0].toString().uppercase()
            binding.firstName.text = user.firstName
            binding.name.text = user.lastName
            binding.email.text = user.email
            binding.fullNameLabel.text = user.firstName + " " + user.lastName
        }
    }


    private fun setBottomBar(){
        binding.bottomNavigation.selectedItemId = R.id.account
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
                    R.id.reviews -> {
                        startActivity(Intent(applicationContext, ReviewsListActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.account -> return@OnNavigationItemSelectedListener true
                }
                false
            })
    }
}