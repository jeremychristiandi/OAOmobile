package com.anjuutspam.livenessfeature.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anjuutspam.livenessfeature.api.ApiConfig
import com.anjuutspam.livenessfeature.db.Photo
import com.anjuutspam.livenessfeature.db.PhotoDao
import com.anjuutspam.livenessfeature.model.LivenessResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class LivenessViewModel(application: Application, private val photoDao: PhotoDao) : AndroidViewModel(application) {


    fun detectLiveness(imageFile: File, onSuccess: (LivenessResponse) -> Unit, onError: (String) -> Unit) {
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
        val description = "Liveness Check".toRequestBody("text/plain".toMediaTypeOrNull())

        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().detectLiveness(body, description)
                if (response.isSuccessful) {
                    val livenessResponse = response.body()
                    if (livenessResponse != null) {
                        savePhotoToDatabase(imageFile)
                        onSuccess(livenessResponse)
                    } else {
                        onError("Empty response body")
                    }
                } else {
                    val statusCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    onError("Failed to detect liveness: HTTP $statusCode - $errorMessage")
                }
            } catch (e: HttpException) {
                e.printStackTrace()
                onError("HTTP Error: ${e.message()}")
            } catch (e: IOException) {
                e.printStackTrace()
                onError("IO Error: ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                onError("Error: ${e.message}")
            }
        }
    }

    private suspend fun savePhotoToDatabase(imageFile: File) {
        val photo = Photo(filePath = imageFile.absolutePath, timestamp = System.currentTimeMillis())
        photoDao.insert(photo)
    }

    fun insertPhoto(photo: Photo) {
        viewModelScope.launch(Dispatchers.IO) {
            photoDao.insert(photo)
        }
    }
}
