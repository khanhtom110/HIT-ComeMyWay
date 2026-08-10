package com.vetpet.petbeats.ui.splash_onboarding.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.VetPet.databinding.ActivitySplashBinding
import com.vetpet.petbeats.ui.auth.activitymain.AuthActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycleScope.launch {
            delay(2000)

            val intent = Intent(this@SplashActivity, AuthActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}