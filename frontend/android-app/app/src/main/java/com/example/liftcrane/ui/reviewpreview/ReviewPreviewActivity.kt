package com.example.liftcrane.ui.reviewpreview

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.core.view.children
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.liftcrane.R
import com.example.liftcrane.databinding.ActivityReviewPreviewBinding
import com.example.liftcrane.endpoints.CloudStorage
import com.example.liftcrane.endpoints.FirebaseAuthService
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.CustomImage
import com.example.liftcrane.model.Review
import com.example.liftcrane.ui.IMG_URI_INTENT_FLAG
import com.example.liftcrane.ui.LIFT_INTENT_FLAG
import com.example.liftcrane.ui.REVIEW_ID_INTENT_FLAG
import com.example.liftcrane.ui.imgpreview.ImagePreviewActivity
import com.example.liftcrane.ui.lift.LiftActivity
import com.example.liftcrane.ui.liftslist.LiftRecyclerAdapter
import com.example.liftcrane.ui.review.ImgRecyclerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat

class ReviewPreviewActivity : AppCompatActivity() {

    private val fireStore = FirestoreService()
    private val auth = FirebaseAuthService()
    private val storage = CloudStorage()
    private val images = mutableListOf<CustomImage>()

    private lateinit var review : Review
    private lateinit var binding: ActivityReviewPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTopBar()
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

        var pattern = "EEE d MMM yyyy"
        var simpleDateFormat = SimpleDateFormat(pattern)
        var date: String = simpleDateFormat.format(review.date.toDate())
        binding.textViewDate.text = date

        pattern = "hh:mm"
        simpleDateFormat = SimpleDateFormat(pattern)
        date = simpleDateFormat.format(review.date.toDate())
        binding.textViewTime.text = date

        if(review.malfunction) binding.textViewMalfunction.text = "Tak"
        else binding.textViewMalfunction.text = "Nie"

        if(review.dtr) binding.textViewDtr.text = "Tak"
        else binding.textViewDtr.text = "Nie"

        if(review.udt) binding.textViewUdt.text = "Tak"
        else binding.textViewUdt.text = "Nie"
    }

    private fun initListView() {
        binding.imgList.layoutManager = LinearLayoutManager(this)
        val adapter = ImgPreviewRecyclerAdapter(review.images.toTypedArray())
        adapter.onClickDelete = {position ->
            val intent = Intent(this, ImagePreviewActivity::class.java)
            intent.putExtra(IMG_URI_INTENT_FLAG, review.images[position])
            startActivity(intent)
        }
        binding.imgList.adapter = adapter
    }

    private fun setTopBar(){
        binding.topMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_to_lift -> {
                    val activity = this
                    binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                        val lift = fireStore.getLiftById(review.liftId)
                        val intent = Intent(activity, LiftActivity::class.java)
                        intent.putExtra(LIFT_INTENT_FLAG, lift)
                        startActivity(intent)
                    }
                    true
                }
                R.id.action_delete -> {
                    if(auth.getSignInUserUid() == review.reviewerId)
                        showDeleteDialog()
                    else
                        showNoPermissionDialog()
                    true
                }
                else -> false
            }
        }
    }


    private fun showDeleteDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Czy na pewno chcesz usunąć serwis")
            .setMessage("Ta operacja nieodwracalnie usunie z historii twój serwis windy " + review.liftName)
            .setNegativeButton("Anuluj", null)
            .setPositiveButton("Potwierdź") { dialog, which ->
                GlobalScope.launch {
                    fireStore.deleteReviewById(review.id!!)
                    finish()
                }
            }
            .setCancelable(false)
            .show()
    }

    private fun showNoPermissionDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Nie można usunąć")
            .setMessage("Nie jesteś autorem tego wpisu")
            .setNegativeButton("Anuluj", null)
            .setCancelable(false)
            .show()
    }
}