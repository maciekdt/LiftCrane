package com.example.liftcrane.ui.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.liftcrane.R
import com.example.liftcrane.databinding.ActivityAccountBinding
import com.example.liftcrane.databinding.ActivityLiftsListBinding
import com.example.liftcrane.endpoints.FirebaseAuthService
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.User
import com.example.liftcrane.ui.menu.MainMenuActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AccountActivity : AppCompatActivity() {

    private val auth = FirebaseAuthService()
    private val fireStore = FirestoreService()

    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setBottomBar()
        setUserFirstLetter(auth.getSignInUserUid()!!)
    }


    private fun setBottomBar(){
        binding.bottomNavigation.selectedItemId = R.id.account

        binding.bottomNavigation.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.review -> {
                        startActivity(Intent(applicationContext, MainMenuActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.account -> return@OnNavigationItemSelectedListener true
                }
                false
            })
    }

    private fun setUserFirstLetter(userId : String){
        fun resolve(user: User?){
            if(user!=null){
                binding.accountFirstLetter.text = user.firstName[0].toString().uppercase()
            }
        }
        fun reject(e:Exception){
            //toast.cancel()
            //toast.show()
        }
        fireStore.getUserById(
            {user -> resolve(user)},
            {e -> reject(e)},
            userId
        )
    }
}