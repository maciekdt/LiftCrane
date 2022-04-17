package com.example.liftcrane.ui.qrscanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QRScannerViewModel : ViewModel(){
    private val _state = MutableLiveData<LiftRequestResult>()
    val state: LiveData<LiftRequestResult> = _state
}