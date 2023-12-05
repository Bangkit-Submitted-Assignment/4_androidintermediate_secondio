package com.dicoding.myintermediateapplication.view.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.dicoding.myintermediateapplication.R
import com.dicoding.myintermediateapplication.data.pref.UserPreference
import com.dicoding.myintermediateapplication.data.pref.dataStore
import com.dicoding.myintermediateapplication.data.response.AddNewStoryResponse
import com.dicoding.myintermediateapplication.data.retrofit.ApiConfig
import com.dicoding.myintermediateapplication.databinding.ActivityUploadBinding
import com.dicoding.myintermediateapplication.view.ViewModelFactory
import com.dicoding.myintermediateapplication.view.main.MainActivity
import com.dicoding.myintermediateapplication.view.main.MainViewModel
import com.dicoding.myintermediateapplication.view.upload.CameraActivity.Companion.CAMERAX_RESULT
import com.google.gson.Gson
import getImageUri
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import reduceFileImage
import retrofit2.HttpException
import uriToFile
import kotlin.math.log

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    private var tokennn: String?=null

    private lateinit var userPreference:UserPreference

    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(this.dataStore) // Inisialisasi userPreference

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val descriptionEditText = binding.edDesc // Mengambil TextInputEditText
            val description = descriptionEditText.text.toString() // Mendapatkan teks dari TextInputEditText

            lifecycleScope.launch {
                try {
                    tokennn = userPreference.getToken() // Mengambil token dari userPreference
                    val requestBody = description.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "photo",
                        imageFile.name,
                        requestImageFile
                    )

                    val apiService = ApiConfig.getApiService()
                    apiService.uploadStory("Bearer $tokennn", multipartBody, requestBody)

                    // Menampilkan pesan upload berhasil
                    Toast.makeText(this@UploadActivity, "Upload successful", Toast.LENGTH_SHORT).show()

                    // Kembali ke MainActivity setelah upload selesai
                    val intent = Intent(this@UploadActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Menyelesaikan aktivitas saat kembali ke MainActivity

                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    Gson().fromJson(errorBody, AddNewStoryResponse::class.java)
                }
            }
        }
    }



    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
