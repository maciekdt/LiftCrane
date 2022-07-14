package com.example.liftcrane.model

import com.google.firebase.Timestamp

data class Review(
    val liftId : String,
    val reviewerId : String,
    val malfunction : Boolean,
    val date : Timestamp,
    val description : String
){
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "liftId" to liftId,
            "reviewerId" to reviewerId,
            "malfunction" to malfunction,
            "date" to date,
            "description" to description
        )
    }
}
