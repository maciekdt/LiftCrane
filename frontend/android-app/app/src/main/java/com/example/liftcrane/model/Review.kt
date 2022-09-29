package com.example.liftcrane.model

import com.google.firebase.Timestamp



data class Review(
    val id : String?,
    val liftId : String,
    val liftName : String,
    val reviewerId : String,
    val reviewerName : String,
    val malfunction : Boolean,
    val dtr : Boolean,
    val udt : Boolean,
    val date : Timestamp,
    val description : String,
    val images : List<String>,
){

    constructor(review: Map<String, Any>, id: String) : this(
        id,
        review["liftId"] as String,
        review["liftName"] as String,
        review["reviewerId"] as String,
        review["reviewerName"] as String,
        review["malfunction"] as Boolean,
        review["dtr"] as Boolean,
        review["udt"] as Boolean,
        review["date"] as Timestamp,
        review["description"] as String,
        review["images"] as List<String>
    )
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "liftId" to liftId,
            "liftName" to liftName,
            "reviewerId" to reviewerId,
            "reviewerName" to reviewerName,
            "malfunction" to malfunction,
            "dtr" to dtr,
            "udt" to udt,
            "date" to date,
            "description" to description,
            "images" to images,
            "seen" to false
        )
    }
}
