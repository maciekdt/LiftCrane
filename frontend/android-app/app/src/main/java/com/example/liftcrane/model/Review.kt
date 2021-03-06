package com.example.liftcrane.model

import com.google.firebase.Timestamp
import java.io.Serializable

data class Review(
    val liftId : String,
    val reviewerId : String,
    val malfunction : Boolean,
    val date : Timestamp,
    val description : String
): Serializable {

    constructor(review: Map<String, Any>) : this(
        review["liftId"] as String,
        review["reviewerId"] as String,
        review["malfunction"] as Boolean,
        review["date"] as Timestamp,
        review["description"] as String

    )
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
