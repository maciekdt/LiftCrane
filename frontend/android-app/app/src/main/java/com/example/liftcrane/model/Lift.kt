package com.example.liftcrane.model

import java.io.Serializable

data class Lift(
    var id : String,
    val serialNumber : String,
    val name : String,
    val address : String,
) : Serializable{
    constructor(lift: Map<String, Any>, id: String) : this(
        id,
        lift["ID"] as String,
        lift["Nazwa"] as String,
        lift["Adres"] as String
    )
}
