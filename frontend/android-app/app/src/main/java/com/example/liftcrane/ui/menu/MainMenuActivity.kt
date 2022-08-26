package com.example.liftcrane.ui.menu

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.liftcrane.databinding.ActivityMainMenuBinding
import com.example.liftcrane.endpoints.FirebaseAuthService
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
        else
            getCameraPermission()
    }


    private fun getCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startQRScannerActivity()
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
                startQRScannerActivity()
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

    private fun startQRScannerActivity() {
        if(auth.isUserSignIn())
            finish()
            startActivity(Intent(this, QRScannerActivity::class.java))
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
                startQRScannerActivity()
            }
        }

        else if (requestCode == REQ_ONE_TAP){
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    fun resolve(userUid: String?){
                        getCameraPermission()
                    }
                    fun reject(e:Exception){
                    }
                    auth.signInGoogle(
                        {userUid -> resolve(userUid)},
                        {e -> reject(e)},
                        idToken)
                }
                else Log.d("MyInfo", "No ID token or password!")
            }
            catch (e: ApiException) { Log.d("MyInfo", e.toString()) }
        }
    }
}