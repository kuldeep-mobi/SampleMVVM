package com.mobikasa.samplemvvm.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mobikasa.samplemvvm.R
import com.mobikasa.samplemvvm.base.BaseFragment
import com.mobikasa.samplemvvm.databinding.FragmentCameraBinding
import com.mobikasa.samplemvvm.utils.Extension.Companion.windowHeight
import com.mobikasa.samplemvvm.utils.Extension.Companion.windowWidth
import com.mobikasa.samplemvvm.viewmodels.VPViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CameraFragment : BaseFragment<FragmentCameraBinding>() {
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var mProcessCameraProvider: ListenableFuture<ProcessCameraProvider>
    private lateinit var mDataBinder: FragmentCameraBinding
    private val vpViewModel: VPViewModel by activityViewModels()
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)


    override fun getLayoutId() = R.layout.fragment_camera

    override fun initView() {
        mDataBinder = getDataBinding()
        startCamera()
        mDataBinder.cameraCaptureButton.setOnClickListener {
            takePhoto()
        }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun startCamera() {
        mProcessCameraProvider = ProcessCameraProvider.getInstance(requireContext())

        mProcessCameraProvider.addListener({
            val cameraProvider = mProcessCameraProvider.get()

            Log.d(TAG, "Screen metrics: $windowWidth x $windowHeight")
            val screenAspectRatio = aspectRatio(windowWidth, windowHeight)
            Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

            val rotation = mDataBinder.viewFinder.display.rotation
            val preview = Preview.Builder().setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation).build().also {
                    it.setSurfaceProvider(mDataBinder.viewFinder.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            imageCapture = ImageCapture.Builder().setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()


            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor, { image ->
                val rotationDegrees = image.imageInfo.rotationDegrees
                val mediaImage = image.image
                // Log.d(TAG, "$rotationDegrees")
                image.close()

                // insert your code here.
            })
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, imageAnalysis, imageCapture, preview)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))

    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".png")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    cropImage.launch(
                        options(uri = savedUri) {
                            setGuidelines(CropImageView.Guidelines.ON)
                            setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                        }
                    )
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(requireActivity().baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            })
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val imagetext = InputImage.fromFilePath(requireContext(), result.uriContent!!)
            recognizer.process(imagetext).addOnSuccessListener {
                val test = it.text
                Log.d("TAG", "Test $test")
                vpViewModel.updateImageURI(uriContent)
                vpViewModel.updateEvent(VPViewModel.TYPE.PHOTOVIEW)
            }.addOnFailureListener {

            }
        } else {
            // an error occurred
            val exception = result.error
        }
    }
}