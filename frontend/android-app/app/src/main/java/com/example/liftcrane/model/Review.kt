package com.example.liftcrane.model

data class Review(
    val liftId : String,
    val reviewerId : String,
    val malfunction : Boolean,
    val date : Int
)
