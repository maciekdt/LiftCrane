package com.example.liftcrane.ui.menu

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
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
import com.example.liftcrane.ui.qrscanner.QRScannerActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException

class MainMenuActivity : AppCompatActivity() {

    private val cameraPermissionRequestCode = 1
    private val fireStore = FirestoreService()
    private val auth = FirebaseAuthService()
    private lateinit var binding: ActivityMainMenuBinding

    //TEST
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true
    //END_TEST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scanQRButton.setOnClickListener {
            startScanning()
        }


        //TEST

        val clientID = "18795223195-9d8u2b4e2j7ldlsvr8h21r6pfhkr2p5u.apps.googleusercontent.com"
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(clientID)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        binding.noQRreviewButton.setOnClickListener {
            //auth.signIn("maciek.fi88@gmail.com", "password")
            //auth.signOut()
            fireStore.test()
        }
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d("MyInfo", e.localizedMessage)
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
                cameraPermissionRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionRequestCode && grantResults.isNotEmpty()) {
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
                startActivityForResult(intent, cameraPermissionRequestCode)
            }
        }
    }

    private fun openCameraWithScanner() {
        val intent = Intent(this, QRScannerActivity::class.java)
        startActivity(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraPermissionRequestCode) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCameraWithScanner()
            }
        }

        //TEST
        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    //val username = credential.id
                    //val password = credential.password
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            Log.d("MyInfo", "Got ID token.")
                            auth.signInGoogle(idToken)

                        }
                        /*password != null -> {
                            // Got a saved username and password. Use them to authenticate
                            // with your backend.
                            Log.d(TAG, "Got password.")
                        }*/
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    Log.d("MyInfo", e.toString())
                }
            }
        }
    }
}