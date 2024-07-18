package com.anjuutspam.livenessfeature.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.anjuutspam.livenessfeature.R
import com.anjuutspam.livenessfeature.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragment_container, EmailFragment::class.java, null)
            }
        }

    }


    fun navigateToLiveFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, LiveFragment::class.java, null)
            addToBackStack(null)
        }
    }

    fun navigateToLivenessActivity() {
        val intent = Intent(this, LivenessActivity::class.java)
        startActivity(intent)
    }

}
