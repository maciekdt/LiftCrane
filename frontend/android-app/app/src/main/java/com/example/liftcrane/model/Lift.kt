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

) : Serializable, Comparable<Lift> {

    constructor(lift: Map<String, Any?>, id: String) : this(
        id,
        lift["nrfab"] as String?,
        lift["udt"] as String?,
        (lift["name"] as String?)?.uppercase(),
        lift["loc"] as String?,
        lift["kg"] as String?,
        lift["prod"] as String?
    )

    fun containsString(query: String): Boolean{
        if(name != null && query.lowercase() in name.lowercase())
            return true
        if(serialNumber != null && query.lowercase() in serialNumber.lowercase())
            return true
        if(localization != null && query.lowercase() in localization.lowercase())
            return true
        return false
    }

    override fun compareTo(other: Lift): Int {
        return name!!.compareTo(other.name!!)
    }
}
