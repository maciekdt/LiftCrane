package com.example.liftcrane.ui.qrscanner


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Size
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.liftcrane.analyzer.MLKitBarcodeAnalyzer
import com.example.liftcrane.analyzer.ScanningResultListener
import com.example.liftcrane.databinding.ActivityQrscannerBinding
import com.example.liftcrane.endpoints.FirestoreService
import com.example.liftcrane.model.Lift
import com.example.liftcrane.ui.LIFT_INTENT_FLAG
import com.example.liftcrane.ui.review.ReviewActivity
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class QRScannerActivity : AppCompatActivity() {

    private var isReviewActivityLaunched = false
    private val fireStore = FirestoreService()

    private lateinit var binding: ActivityQrscannerBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private var flashEnabled = false

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
        val camera =
            cameraProvider?.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)

    }

    private fun processQRCode(qrCode:String, imageAnalysis:ImageAnalysis, cameraProvider:ProcessCameraProvider?){
        fun resolve(lift:Lift?){
            if(lift!=null && !isReviewActivityLaunched) {
                isReviewActivityLaunched = true
                imageAnalysis.clearAnalyzer()
                cameraProvider?.unbindAll()
                startReviewActivity(lift)

            }
            else if(lift==null){}

        }

        fun reject(e:Exception){
            //toast.cancel()
           //toast.show()
        }

        fireStore.getLiftById(
            {lift -> resolve(lift)},
            {e -> reject(e)},
            qrCode
        )

    }

    private fun startReviewActivity(lift:Lift){
        val intent = Intent(this, ReviewActivity::class.java)
        intent.putExtra(LIFT_INTENT_FLAG, lift)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}