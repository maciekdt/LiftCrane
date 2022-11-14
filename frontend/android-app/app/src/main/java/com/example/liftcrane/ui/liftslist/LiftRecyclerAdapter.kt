package com.example.liftcrane.ui.liftslist

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.liftcrane.R
import com.example.liftcrane.model.Lift
import com.google.android.material.card.MaterialCardView
import com.google.firebase.Timestamp
import java.time.Period
import java.util.*


class LiftRecyclerAdapter(private val lifts: MutableList<Lift>) :
    RecyclerView.Adapter<LiftRecyclerAdapter.ViewHolder>() {

    var onItemClick: ((Lift) -> Unit)? = null
    var filterLifts = lifts.sorted().toMutableList()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemName: TextView = view.findViewById(R.id.reviewListItemTextViewDate)
        var itemAddress: TextView = view.findViewById(R.id.reviewListItemTextViewUser)
        var itemSerial: TextView = view.findViewById(R.id.liftListItemTextViewSerial)
        var itemUdtDiff: TextView = view.findViewById(R.id.textViewUdtDiff)
        var itemDtrDiff: TextView = view.findViewById(R.id.textViewDtrDiff)
        var card: MaterialCardView = view as MaterialCardView

        init {
            view.setOnClickListener {
                onItemClick?.invoke(filterLifts[adapterPosition])
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lift_list_item, parent, false)
        return ViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val lift = filterLifts[position]

        viewHolder.itemName.text = lift.name
        viewHolder.itemAddress.text = lift.localization
        viewHolder.itemSerial.text = lift.serialNumber

        val currentTime = Timestamp.now().toDate()

        if(lift.udtPeriod != null && lift.lastUdt != null) {
            val cal = Calendar.getInstance()
            cal.time = Date(lift.lastUdt!!)
            cal.add(Calendar.YEAR, lift.udtPeriod!!)
            val udtDeadline = cal.time
            val diffDays = (udtDeadline.time - currentTime.time)/(24 * 60 * 60 * 1000)
            if(diffDays>0) {
                viewHolder.itemUdtDiff.text = "UDT(+$diffDays)"
                viewHolder.itemUdtDiff.setTextColor(
                    ContextCompat.getColor(viewHolder.card.context, R.color.ok_color))
            }
            else{
                viewHolder.itemUdtDiff.text = "UDT($diffDays)"
                viewHolder.itemUdtDiff.setTextColor(
                    ContextCompat.getColor(viewHolder.card.context, R.color.warning_color))
            }
        }
        else{
            viewHolder.itemUdtDiff.text = "UDT(?)"
            viewHolder.itemUdtDiff.setTextColor(
                ContextCompat.getColor(viewHolder.card.context, R.color.neutral_color))
        }


        if(lift.lastDtr != null) {
            val cal = Calendar.getInstance()
            cal.time = Date(lift.lastDtr!!)
            cal.add(Calendar.MONTH, 1)
            val dtrDeadline = cal.time
            val diffDays = (dtrDeadline.time - currentTime.time)/(24 * 60 * 60 * 1000)
            if(diffDays>0) {
                viewHolder.itemDtrDiff.text = "DTR(+$diffDays)"
                viewHolder.itemDtrDiff.setTextColor(
                    ContextCompat.getColor(viewHolder.card.context, R.color.ok_color))
            }
            else{
                viewHolder.itemDtrDiff.text = "DTR($diffDays)"
                viewHolder.itemDtrDiff.setTextColor(
                    ContextCompat.getColor(viewHolder.card.context, R.color.warning_color))
            }
        }
        else{
            viewHolder.itemDtrDiff.text = "DTR(?)"
            viewHolder.itemDtrDiff.setTextColor(
                ContextCompat.getColor(viewHolder.card.context, R.color.neutral_color))
        }

        if(lift.working != null && !lift.working!! ) {
            viewHolder.card.setCardBackgroundColor(
                ContextCompat.getColor(viewHolder.card.context, R.color.malfunction_card_color))
        }
    }


    override fun getItemCount() = filterLifts.size


    @SuppressLint("NotifyDataSetChanged")
    fun filterByQuery(query: String){
        filterLifts = lifts.mapNotNull{
            if(it.containsString(query)) it else null }
            .toMutableList()
        filterLifts.sort()
        notifyDataSetChanged()
    }
}