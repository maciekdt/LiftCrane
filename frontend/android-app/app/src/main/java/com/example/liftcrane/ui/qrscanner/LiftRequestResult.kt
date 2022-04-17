package com.example.liftcrane.ui.qrscanner

import com.example.liftcrane.model.Lift

data class LiftRequestResult(
    val lift: Lift? = null,
    val error: Int? = null
)
