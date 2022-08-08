package com.example.liftcrane.model

import java.io.Serializable

data class Lift(
    var id : String,
    val serialNumber : String?,
    val udtSerialNumber : String?,
    val name : String?,
    val localization : String?,
    val liftingCapacity : String?,
    val producer : String?

) : Serializable{
    constructor(lift: Map<String, Any?>, id: String) : this(
        id,
        lift["id"] as String?,
        lift["udt"] as String?,
        lift["name"] as String?,
        lift["loc"] as String?,
        lift["kg"] as String?,
        lift["prod"] as String?
    )
}
