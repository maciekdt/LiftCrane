package com.example.liftcrane.ui.liftslist

import com.example.liftcrane.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.liftcrane.model.Lift



class LiftRecyclerAdapter(private val lifts: Array<Lift>) :
    RecyclerView.Adapter<LiftRecyclerAdapter.ViewHolder>() {

    var onItemClick: ((Lift) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemName: TextView = view.findViewById(R.id.reviewListItemTextViewDate)
        var itemAddress: TextView = view.findViewById(R.id.reviewListItemTextViewUser)
        var itemSerial: TextView = view.findViewById(R.id.liftListItemTextViewSerial)

        init {
            view.setOnClickListener {
                onItemClick?.invoke(lifts[adapterPosition])
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lift_list_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val lift = lifts[position]

        viewHolder.itemName.text = lift.name
        viewHolder.itemAddress.text = lift.localization
        viewHolder.itemSerial.text = lift.serialNumber
    }


    override fun getItemCount() = lifts.size

}