package com.anjuutspam.livenessfeature.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.anjuutspam.livenessfeature.databinding.ActivityStatementOfCustomerBinding

class StatementOfCustomerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStatementOfCustomerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
    }
}