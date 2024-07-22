package com.anjuutspam.livenessfeature.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.anjuutspam.livenessfeature.R
import com.anjuutspam.livenessfeature.databinding.ActivityLivenessBinding
import com.anjuutspam.livenessfeature.db.AppDatabase
import com.anjuutspam.livenessfeature.db.Photo
import com.anjuutspam.livenessfeature.model.LivenessResponse
import com.anjuutspam.livenessfeature.viewmodel.LivenessViewModel
import com.anjuutspam.livenessfeature.viewmodel.LivenessViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class LivenessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLivenessBinding
    private lateinit var viewModel: LivenessViewModel
    private var imageCapture: ImageCapture? = null
    private var outputDirectory: File? = null
    private var currentPhotoPath: String? = null

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CAMERA_PERMISSION = 10
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLivenessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel setup
        val database = AppDatabase.getDatabase(applicationContext)
        val photoDao = database.photoDao()
        val viewModelFactory = LivenessViewModelFactory(application, photoDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LivenessViewModel::class.java)

        // Request camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            setupCamera()
        }

        binding.btnCapture.setOnClickListener {
            takePhoto()
        }
        binding.btnClose.setOnClickListener {
            finish()
        }

        outputDirectory = getOutputDirectory()
    }

    private fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Set up preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Set up image capture
            imageCapture = ImageCapture.Builder()
                .build()

            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            try {
                cameraProvider.bindToLifecycle(
                    this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(this@LivenessActivity, "Failed to bind camera", Toast.LENGTH_SHORT).show()
                finish()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Timestamp
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options for ImageCapture
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(this@LivenessActivity, "Failed to capture image: ${exc.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    currentPhotoPath = photoFile.absolutePath
                    //save photo to db
                    savePhotoToDatabase(photoFile.absolutePath)

                    // Process and send the captured image
                    processAndSendImage(savedUri)
                }
            })
    }

    private fun processAndSendImage(savedUri: Uri) {
        val file = File(savedUri.path ?: "")
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        val description = "Liveness Check".toRequestBody("text/plain".toMediaTypeOrNull())

        viewModel.detectLiveness(file,
            onSuccess = { livenessResponse ->
                navigateToResult(livenessResponse)
            },
            onError = { errorMessage ->
                showToast(errorMessage)
            }
        )
    }

    private fun navigateToResult(livenessResponse: LivenessResponse) {
        Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_LIVENESS_RESPONSE, livenessResponse)
            startActivity(this)
            finish()
        }
    }

    private fun showToast(message: String) {
        runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
    }

    public override fun onResume() {
        super.onResume()
        setupCamera()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return mediaDir ?: filesDir
    }

    private fun savePhotoToDatabase(filePath: String) {
        val photo = Photo(filePath = filePath, timestamp = System.currentTimeMillis())
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.insertPhoto(photo)
        }
    }
}