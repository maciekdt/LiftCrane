package com.example.liftcrane.model

import java.io.Serializable

data class User(
    val firstName : String,
    val lastName : String,
    val email : String,
) : Serializable{
    constructor(user : Map<String, Any>) : this(
        user["firstName"] as String,
        user["lastName"] as String,
        user["email"] as String
    )
}