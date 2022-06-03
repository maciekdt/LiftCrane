package com.example.liftcrane.ui.liftslist
import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.liftcrane.R
import com.example.liftcrane.model.Lift

class LiftsListAdapter(private val context: Activity,
                       private val data:  MutableList<Lift>) :
    ArrayAdapter<Lift>(context, R.layout.lift_list_item, data) {

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, view: View?, parent: ViewGroup): View{
            val inflater = context.layoutInflater
            val rowView = inflater.inflate(R.layout.lift_list_item, null, true)

            val titleText = rowView.findViewById(R.id.textView) as TextView
            val subtitleText = rowView.findViewById(R.id.textView2) as TextView
            val addressText = rowView.findViewById(R.id.textView3) as TextView

            titleText.text = data[position].name
            subtitleText.text = data[position].serialNumber
            addressText.text = data[position].address

            return rowView
        }
}