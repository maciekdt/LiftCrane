package com.example.liftcrane.model

import com.google.firebase.Timestamp
import java.io.Serializable

data class Lift(
    var id : String,
    var serialNumber : String?,
    var udtSerialNumber : String?,
    var name : String?,
    var localization : String?,
    var liftingCapacity : String?,
    var producer : String?,
    var udtPeriod: Int?,
    var lastUdt: Long?,
    var lastDtr: Long?,
    var working: Boolean?

) : Serializable, Comparable<Lift> {

    constructor(lift: Map<String, Any?>, id: String) : this(
        id,
        lift["nrfab"] as String?,
        lift["udt"] as String?,
        (lift["name"] as String?)?.uppercase(),
        lift["loc"] as String?,
        lift["kg"] as String?,
        lift["prod"] as String?,
        if(lift.containsKey("udtTime") && lift["udtTime"] != null)
            (lift["udtTime"] as Long).toInt() else null,
        if(lift.containsKey("lastUdt") && lift["lastUdt"] != null)
            lift["lastUdt"] as Long else null,
        if(lift.containsKey("lastDtr") && lift["lastDtr"] != null)
            lift["lastDtr"] as Long else null,
        if(lift.containsKey("working") && lift["working"] != null)
            lift["working"] as Boolean else null,
    )

    fun containsString(query: String): Boolean{
        if(name != null && query.lowercase() in name!!.lowercase())
            return true
        if(serialNumber != null && query.lowercase() in serialNumber!!.lowercase())
            return true
        if(localization != null && query.lowercase() in localization!!.lowercase())
            return true
        return false
    }

    override fun compareTo(other: Lift): Int {
        return name!!.compareTo(other.name!!)
    }

    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "liftId" to id,
            "name" to (name ?: ""),
            "nrfab" to (serialNumber ?: ""),
            "udt" to (udtSerialNumber ?: ""),
            "loc" to (localization ?: ""),
            "prod" to (producer ?: ""),
            "kg" to (liftingCapacity ?: ""),
            "udtTime" to udtPeriod,
            "lastUdt" to lastUdt,
            "lastDtr" to lastDtr,
            "working" to working
        )
    }
}
