package com.example.liftcrane.ui.review
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.liftcrane.R
import com.example.liftcrane.model.CustomImage


class ImgRecyclerAdapter(private val images: Array<CustomImage>) :
    RecyclerView.Adapter<ImgRecyclerAdapter.ViewHolder>() {

    var onClickDelete: ((Int) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemImg: ImageView = view.findViewById(R.id.reviewImage)
        var deleteButton: Button = view.findViewById(R.id.imgItemDeleteButton)

        init {
            deleteButton.setOnClickListener {
                onClickDelete?.invoke(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.img_list_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val img = images[position]
        viewHolder.itemImg.setImageURI(img.uri)
    }


    override fun getItemCount() = images.size

}