package com.example.liftcrane.model

import android.net.Uri
import java.io.Serializable

data class CustomImage(
    val id : String,
    val uri : Uri
): Serializable
