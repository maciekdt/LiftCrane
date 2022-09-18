package com.example.liftcrane.ui.qrscanner


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Size
import android.view.OrientationEventListener
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.liftcrane.R
import com.example.liftcrane.analyzer.MLKitBarcodeAnalyzer
import com.example.liftcrane.analyzer.ScanningResultListener
import com.example.liftcrane.databinding.ActivityQrscannerBinding
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.Lift
import com.example.liftcrane.ui.LIFT_INTENT_FLAG
import com.example.liftcrane.ui.account.AccountActivity
import com.example.liftcrane.ui.lift.LiftActivity
import com.example.liftcrane.ui.liftslist.LiftsListActivity
import com.example.liftcrane.ui.review.ReviewActivity
import com.example.liftcrane.ui.reviewslist.ReviewsListActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class QRScannerActivity : AppCompatActivity() {

    private var isActionDone = false
    private val fireStore = FirestoreService()
    private var isFlash = false

    private lateinit var binding: ActivityQrscannerBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var camera: Camera


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrscannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))

        binding.flashButton.setOnClickListener {
            if(this::camera.isInitialized) {
                camera.cameraControl.enableTorch(!isFlash)
                isFlash = !isFlash
            }
        }
        setBottomBar()
    }


    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {

        if (isDestroyed || isFinishing) {
            return
        }
        cameraProvider?.unbindAll()
        val preview: Preview = Preview.Builder()
            .build()
        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(binding.cameraPreview.width, binding.cameraPreview.height))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        val orientationEventListener = object : OrientationEventListener(this as Context) {
            override fun onOrientationChanged(orientation : Int) {
                val rotation : Int = when (orientation) {
                    in 45..134 -> Surface.ROTATION_270
                    in 135..224 -> Surface.ROTATION_180
                    in 225..314 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageAnalysis.targetRotation = rotation
            }
        }
        orientationEventListener.enable()

        class ScanningListener : ScanningResultListener {
            override fun onScanned(result: String) {
                runOnUiThread {
                    //imageAnalysis.clearAnalyzer()
                    //cameraProvider?.unbindAll()
                    processQRCode(result, imageAnalysis, cameraProvider)
                }
            }
        }
        val analyzer: ImageAnalysis.Analyzer = MLKitBarcodeAnalyzer(ScanningListener())
        imageAnalysis.setAnalyzer(cameraExecutor, analyzer)
        preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
        camera =
            cameraProvider?.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)!!
    }


    private fun processQRCode(qrCode:String, imageAnalysis:ImageAnalysis, cameraProvider:ProcessCameraProvider?){
        binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            try {
                val lift = fireStore.getLiftById(qrCode)
                if (lift != null && !isActionDone) {
                    startReviewActivity(lift)
                } else if (lift == null && !isActionDone) {
                    showWrongQRCodeDialog()
                }
                isActionDone = true
                imageAnalysis.clearAnalyzer()
                cameraProvider?.unbindAll()
            }
            catch (e:Exception){
                showWrongQRCodeDialog()
            }
        }
    }


    private fun startReviewActivity(lift:Lift){
        val intent = Intent(this, LiftActivity::class.java)
        intent.putExtra(LIFT_INTENT_FLAG, lift)
        startActivity(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


    private fun setBottomBar(){
        binding.bottomNavigation.selectedItemId = R.id.scanner

        binding.bottomNavigation.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.account -> {
                        startActivity(Intent(applicationContext, AccountActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.lifts -> {
                        startActivity(Intent(applicationContext, LiftsListActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.reviews -> {
                        startActivity(Intent(applicationContext, ReviewsListActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.scanner -> return@OnNavigationItemSelectedListener true
                }
                false
            })
    }


    private fun showWrongQRCodeDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Niepoprawny kod QR")
            .setMessage("Kod QR odnosi się do windy nie zapisanej w bazie lub jest błędny")
            .setNegativeButton("Wybierz z listy") { dialog, which ->
                finish()
                startActivity(intent)
                startActivity(Intent(this, LiftsListActivity::class.java))
            }
            .setPositiveButton("Skanuj dalej") { dialog, which ->
                finish()
                startActivity(intent)
            }
            .setCancelable(false)
            .show()
    }
}