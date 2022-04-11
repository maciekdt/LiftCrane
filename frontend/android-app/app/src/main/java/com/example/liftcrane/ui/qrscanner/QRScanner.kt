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
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class QRScanner : AppCompatActivity() {


    private lateinit var binding: ActivityQrscannerBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService
    private var flashEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrscannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {

        if (isDestroyed || isFinishing) {
            //This check is to avoid an exception when trying to re-bind use cases but user closes the activity.
            //java.lang.IllegalArgumentException: Trying to create use case mediator with destroyed lifecycle.
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
                // Monitors orientation values to determine the target rotation value
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

        //switch the analyzers here, i.e. MLKitBarcodeAnalyzer, ZXingBarcodeAnalyzer
        class ScanningListener : ScanningResultListener {
            override fun onScanned(result: String) {
                runOnUiThread {
                    imageAnalysis.clearAnalyzer()
                    cameraProvider?.unbindAll()
                    Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()

                    /*ScannerResultDialog.newInstance(
                        result,
                        object : ScannerResultDialog.DialogDismissListener {
                            override fun onDismiss() {
                                bindPreview(cameraProvider)
                            }
                        })
                        .show(supportFragmentManager, ScannerResultDialog::class.java.simpleName)*/
                }
            }
        }

        val analyzer: ImageAnalysis.Analyzer = MLKitBarcodeAnalyzer(ScanningListener())

        /*if (scannerSDK == ScannerSDK.ZXING) {
            analyzer = ZXingBarcodeAnalyzer(ScanningListener())
        }*/

        imageAnalysis.setAnalyzer(cameraExecutor, analyzer)

        preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

        val camera =
            cameraProvider?.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)

        /*if (camera?.cameraInfo?.hasFlashUnit() == true) {
            binding.ivFlashControl.visibility = View.VISIBLE

            binding.ivFlashControl.setOnClickListener {
                camera.cameraControl.enableTorch(!flashEnabled)
            }

            camera.cameraInfo.torchState.observe(this) {
                it?.let { torchState ->
                    if (torchState == TorchState.ON) {
                        flashEnabled = true
                        binding.ivFlashControl.setImageResource(R.drawable.ic_round_flash_on)
                    } else {
                        flashEnabled = false
                        binding.ivFlashControl.setImageResource(R.drawable.ic_round_flash_off)
                    }
                }
            }
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        // Shut down our background executor
        cameraExecutor.shutdown()
    }

}