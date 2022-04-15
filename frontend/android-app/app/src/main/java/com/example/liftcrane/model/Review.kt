package com.example.liftcrane.model

data class Review(
    val liftId : String,
    val reviewerId : String,
    val malfunction : Boolean,
    val date : Int
){
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "liftId" to liftId,
            "reviewerId" to reviewerId,
            "malfunction" to malfunction,
            "date" to date
        )
    }
}
