package com.anjuutspam.livenessfeature.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anjuutspam.livenessfeature.db.PhotoDao


class LivenessViewModelFactory(private val application: Application, private val photoDao: PhotoDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LivenessViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LivenessViewModel(application, photoDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}

