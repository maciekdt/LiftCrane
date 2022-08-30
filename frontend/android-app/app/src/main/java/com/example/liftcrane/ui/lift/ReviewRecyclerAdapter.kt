package com.example.liftcrane.ui.lift

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.liftcrane.R
import com.example.liftcrane.model.Review
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat


class ReviewRecyclerAdapter(private val reviews: Array<Review>) :
    RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder>() {

    var onItemClick: ((Review) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemDate: TextView = view.findViewById(R.id.reviewListItemTextViewDate)
        var itemReviewer: TextView = view.findViewById(R.id.reviewListItemTextViewUser)
        var itemMalfunction: TextView = view.findViewById(R.id.reviewListItemTextViewMalfunction)
        var card: MaterialCardView = view as MaterialCardView

        init {
            view.setOnClickListener {
                onItemClick?.invoke(reviews[adapterPosition])
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_list_item, parent, false)
        return ViewHolder(view)
    }


    @SuppressLint("SimpleDateFormat", "ResourceAsColor")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val review = reviews[position]

        val pattern = "EEE d MMM yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(review.date.toDate())

        viewHolder.itemDate.text = date
        viewHolder.itemReviewer.text = review.reviewerName

        if(review.malfunction) {
            viewHolder.itemMalfunction.text = "Awaria"
            viewHolder.card.setCardBackgroundColor(
                ContextCompat.getColor(viewHolder.card.context, R.color.malfunction_card_color))
        }
        else {
            viewHolder.itemMalfunction.text = "Sprawna"
            viewHolder.card.setCardBackgroundColor(
                ContextCompat.getColor(viewHolder.card.context, R.color.no_malfunction_card_color))
        }


    }


    override fun getItemCount() = reviews.size

}