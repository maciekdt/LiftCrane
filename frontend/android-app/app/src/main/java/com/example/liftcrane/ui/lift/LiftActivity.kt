package com.example.liftcrane.ui.lift

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.liftcrane.R
import com.example.liftcrane.databinding.ActivityLiftBinding
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.Lift
import com.example.liftcrane.model.Review
import com.example.liftcrane.ui.LIFT_INTENT_FLAG
import com.example.liftcrane.ui.REVIEW_ID_INTENT_FLAG
import com.example.liftcrane.ui.editlift.EditLiftActivity
import com.example.liftcrane.ui.review.ReviewActivity
import com.example.liftcrane.ui.reviewpreview.ReviewPreviewActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URLEncoder


class LiftActivity : AppCompatActivity() {

    private val fireStore = FirestoreService()

    private lateinit var lift : Lift
    private lateinit var binding: ActivityLiftBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lift = intent.extras?.get("lift") as Lift
        setLiftData()
        initListView(this)
        setTopBar()

        fireStore.onChangeReviewsForLift(
            { reviews -> onChangeReviews(reviews) }, lift.id)

        /*binding.mapButton.setOnClickListener {
            launchGoogleMap()
        }*/

        binding.floatingActionButtonReview.setOnClickListener {
            startReviewActivity()
        }
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

    private fun startReviewActivity(){
        val intent = Intent(this, ReviewActivity::class.java)
        intent.putExtra(LIFT_INTENT_FLAG, lift)
        startActivity(intent)
    }

    private fun initListView(activity: AppCompatActivity){
        binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch{
            val reviews = fireStore.getAllReviewsForLift(lift.id)
            binding.reviewsList.layoutManager = LinearLayoutManager(activity)
            val adapter = ReviewRecyclerAdapter(reviews)
            adapter.onItemClick = { review ->
                val intent = Intent(activity, ReviewPreviewActivity::class.java)
                intent.putExtra(REVIEW_ID_INTENT_FLAG, review.id)
                startActivity(intent)
            }
            binding.reviewsList.adapter = adapter
        }
    }

    private fun onChangeReviews(reviews : MutableList<Review>){
        val reviewsNumber = binding.reviewsList.adapter?.itemCount
        binding.reviewsList.layoutManager = LinearLayoutManager(this)
        val adapter = ReviewRecyclerAdapter(reviews)
        binding.reviewsList.adapter = adapter
        val activity = this
        adapter.onItemClick = { review ->
            val intent = Intent(activity, ReviewPreviewActivity::class.java)
            intent.putExtra(REVIEW_ID_INTENT_FLAG, review.id)
            startActivity(intent)
        }

        if(reviewsNumber != null && reviews.size > reviewsNumber) {
            Snackbar.make(binding.constraintLayout, "Dodano zgłoszenie", Snackbar.LENGTH_SHORT)
                .show()
        }
        else if(reviewsNumber != null && reviews.size < reviewsNumber){
            Snackbar.make(binding.constraintLayout, "Usunięto zgłoszenie", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun setTopBar(){
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_edit_lift -> {
                    val intent = Intent(this, EditLiftActivity::class.java)
                    intent.putExtra(LIFT_INTENT_FLAG, lift)
                    startActivity(intent)
                    true
                }
                R.id.action_delete_lift -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Czy na pewno chcesz usunąć dźwig")
                        .setMessage("Ta operacja nieodwracalnie usunie dźwig " + lift.name)
                        .setNegativeButton("Anuluj", null)
                        .setPositiveButton("Potwierdź") { dialog, which ->
                            GlobalScope.launch {
                                fireStore.deleteLift(lift.id)
                                finish()
                            }
                        }
                        .setCancelable(false)
                        .show()
                    true
                }
                else -> false
            }
        }
    }
}