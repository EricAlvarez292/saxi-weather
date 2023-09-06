package com.saxipapsi.weathermap.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saxipapsi.weathermap.databinding.ActivitySplashScreenBinding
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timer().schedule(1500) {
            MainActivity.start(this@SplashScreenActivity)
            finish()
        }
    }
}