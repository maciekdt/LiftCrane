package com.example.liftcrane.model

data class Lift(
    val id : String,
    val serialNumber : String,
    val name : String,
    val address : String,
){
    constructor(lift : Map<String, Any>) : this(
        lift["id"] as String,
        lift["serialNumber"] as String,
        lift["name"] as String,
        lift["address"] as String
    )
}
