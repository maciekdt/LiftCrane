package com.example.liftcrane.ui.imgpreview

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.liftcrane.databinding.ActivityImagePreviewBinding
import com.example.liftcrane.endpoints.CloudStorage
import com.example.liftcrane.ui.IMG_URI_INTENT_FLAG


class ImagePreviewActivity : AppCompatActivity() {

    private val storage = CloudStorage()
    private lateinit var binding: ActivityImagePreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgUri = intent.extras?.get(IMG_URI_INTENT_FLAG) as String

        Glide.with(this)
            .load(storage.getImageUrl(imgUri))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
                    return false
                }
                override fun onResourceReady(
                    p0: Drawable?,
                    p1: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    p4: Boolean
                ): Boolean {
                    binding.progressBar3.visibility = View.GONE
                    return false
                }
            })
            .into(binding.imageView2)
    }
}