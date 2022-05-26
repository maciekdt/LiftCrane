package com.example.liftcrane.model

import java.io.Serializable

data class Lift(
    val serialNumber : String,
    val name : String,
    val address : String,
) : Serializable{
    constructor(lift : Map<String, Any>) : this(
        lift["ID"] as String,
        lift["Nazwa"] as String,
        lift["Adres"] as String
    )
}
