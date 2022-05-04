package com.example.liftcrane.ui.menu

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.liftcrane.databinding.ActivityMainMenuBinding
import com.example.liftcrane.endpoints.FirebaseAuthService
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.ui.GoogleSignInUI
import com.example.liftcrane.ui.REQ_CAM_PERM
import com.example.liftcrane.ui.REQ_ONE_TAP
import com.example.liftcrane.ui.qrscanner.QRScannerActivity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException

class MainMenuActivity : AppCompatActivity() {

    private val auth = FirebaseAuthService()
    private val googleSignIn = GoogleSignInUI()

    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var oneTapClient: SignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(!auth.isUserSignIn())
            oneTapClient = googleSignIn.startOneTapClient(this)

        binding.scanQRButton.setOnClickListener {
            startScanning()
        }

        binding.noQRreviewButton.setOnClickListener {
        }
    }


    private fun startScanning() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCameraWithScanner()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQ_CAM_PERM
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_CAM_PERM && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraWithScanner()
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivityForResult(intent, REQ_CAM_PERM)
            }
        }
    }

    private fun openCameraWithScanner() {
        val intent = Intent(this, QRScannerActivity::class.java)
        startActivity(intent)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CAM_PERM) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCameraWithScanner()
            }
        }

        else if (requestCode == REQ_ONE_TAP){
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    Log.d("MyInfo", "Got ID token.")
                    auth.signInGoogle(idToken)
                }
                else Log.d(TAG, "No ID token or password!")
            }
            catch (e: ApiException) { Log.d("MyInfo", e.toString()) }
        }
    }
}