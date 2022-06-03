package com.example.liftcrane.ui.liftslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.liftcrane.R
import com.example.liftcrane.databinding.ActivityLiftsListBinding
import com.example.liftcrane.databinding.ActivityMainMenuBinding
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.Lift
import com.example.liftcrane.ui.LIFT_INTENT_FLAG
import com.example.liftcrane.ui.review.ReviewActivity

class LiftsListActivity : AppCompatActivity() {

    private val fireStore = FirestoreService()

    private lateinit var binding: ActivityLiftsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiftsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListView()
    }

    private fun initListView(){
        fun resolve(lifts : MutableList<Lift>){
            val adapter = LiftsListAdapter(this, lifts)
            binding.list.adapter = adapter

            binding.list.setOnItemClickListener(){adapterView, view, position, id ->
                startReviewActivity(lifts[position])
            }
        }

        fun reject(e:Exception){
        }

        fireStore.getAllLifts(
            {lifts -> resolve(lifts)},
            {e -> reject(e)}
        )
    }

    private fun startReviewActivity(lift:Lift){
        val intent = Intent(this, ReviewActivity::class.java)
        intent.putExtra(LIFT_INTENT_FLAG, lift)
        startActivity(intent)
    }
}