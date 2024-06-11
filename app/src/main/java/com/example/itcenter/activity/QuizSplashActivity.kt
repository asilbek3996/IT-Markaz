package com.example.itcenter.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.itcenter.R
import com.example.itcenter.databinding.ActivityQuizSplashBinding

class QuizSplashActivity : AppCompatActivity() {
    lateinit var binding: ActivityQuizSplashBinding
    private var isBackPressed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.animationView.postDelayed({
            if (!isBackPressed) {
                startActivity(Intent(this, QuizStartActivity::class.java))
                finish()
            }
        },3500)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        isBackPressed = true
    }
}