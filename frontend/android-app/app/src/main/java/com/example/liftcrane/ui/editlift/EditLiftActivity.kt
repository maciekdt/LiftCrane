package com.example.liftcrane.ui.editlift

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.children
import com.example.liftcrane.R
import com.example.liftcrane.databinding.ActivityEditLiftBinding
import com.example.liftcrane.databinding.ActivityLiftBinding
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.Lift
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditLiftActivity : AppCompatActivity() {

    private val fireStore = FirestoreService()

    private lateinit var lift : Lift
    private lateinit var binding: ActivityEditLiftBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLiftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lift = intent.extras?.get("lift") as Lift
        setLiftData()

        binding.buttonEdit.setOnClickListener{
            uploadLift()
        }

    }

    private fun setLiftData(){
        if(lift.name != null){
            binding.editName.setText(lift.name, TextView.BufferType.EDITABLE)
        }
        if(lift.serialNumber != null){
            binding.editSerial.setText(lift.serialNumber, TextView.BufferType.EDITABLE)
        }
        if(lift.udtSerialNumber != null){
            binding.editUdt.setText(lift.udtSerialNumber, TextView.BufferType.EDITABLE)
        }
        if(lift.udtSerialNumber != null){
            binding.editUdt.setText(lift.udtSerialNumber, TextView.BufferType.EDITABLE)
        }
        if(lift.localization != null){
            binding.editLoc.setText(lift.localization, TextView.BufferType.EDITABLE)
        }
        if(lift.producer != null){
            binding.editProd.setText(lift.producer, TextView.BufferType.EDITABLE)
        }
        if(lift.liftingCapacity != null){
            binding.editLiftCap.setText(lift.liftingCapacity, TextView.BufferType.EDITABLE)
        }
    }

    private fun uploadLift(){
        lift.serialNumber = binding.editSerial.text.toString()
        lift.udtSerialNumber = binding.editUdt.text.toString()
        lift.localization = binding.editLoc.text.toString()
        lift.producer = binding.editProd.text.toString()
        lift.liftingCapacity = binding.editLiftCap.text.toString()

        MaterialAlertDialogBuilder(this)
            .setTitle("Czy na pewno chcesz edytować dane dźwigu?")
            .setMessage("Ta operacja nieodwracalnie zmieni dokumentacje dźwigu " + lift.name)
            .setNegativeButton("Anuluj", null)
            .setPositiveButton("Potwierdź") { dialog, which ->
                GlobalScope.launch {
                    lift.name = binding.editName.text.toString()
                    fireStore.updateLift(lift)
                    finish()
                }
            }
            .setCancelable(false)
            .show()
    }
}