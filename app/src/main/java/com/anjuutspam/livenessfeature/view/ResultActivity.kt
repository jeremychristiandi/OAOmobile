package com.anjuutspam.livenessfeature.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anjuutspam.livenessfeature.databinding.ActivityResultBinding
import com.anjuutspam.livenessfeature.model.LivenessResponse
import com.bumptech.glide.Glide

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var livenessResponse: LivenessResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        livenessResponse = intent.getParcelableExtra(EXTRA_LIVENESS_RESPONSE)!!

        binding.backButton.setOnClickListener { finish()}

        // Show result image
        livenessResponse.data?.resultPath?.let { resultPath ->
             Glide.with(this).load(resultPath).into(binding.ivResultImage)
        }

        // Show liveness status
        binding.tvVerifyResult.text = livenessResponse.data?.livenessStatus ?: "Unknown"

    }

    companion object {
        const val EXTRA_LIVENESS_RESPONSE = "extra_liveness_response"
    }
}

