package com.example.liftcrane.ui.reviewpreview

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.liftcrane.R
import com.example.liftcrane.endpoints.CloudStorage
import javax.sql.DataSource


class ImgPreviewRecyclerAdapter(private val imagesID: Array<String>) :
    RecyclerView.Adapter<ImgPreviewRecyclerAdapter.ViewHolder>() {

    private val storage = CloudStorage()

    var onClickDelete: ((Int) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var card = view
        var itemImg: ImageView = view.findViewById(R.id.reviewImage)
        var progressBarItem: ProgressBar = view.findViewById(R.id.progressBar2)
        /*var deleteButton: Button = view.findViewById(R.id.imgItemDeleteButton)

        init {
            deleteButton.setOnClickListener {
                onClickDelete?.invoke(adapterPosition)
            }
        }*/
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.img_preview_list_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(viewHolder.card.context)
            .load(storage.getImageUrl(imagesID[position]))
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
                    viewHolder.progressBarItem.visibility = View.GONE
                    return false
                }
            })
            .into(viewHolder.itemImg)


    }


    override fun getItemCount() = imagesID.size

}