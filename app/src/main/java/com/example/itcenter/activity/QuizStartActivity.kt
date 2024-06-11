package com.example.itcenter.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.itcenter.R
import com.example.itcenter.databinding.ActivityQuizStartBinding

class QuizStartActivity : AppCompatActivity() {
    lateinit var binding: ActivityQuizStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.exitQuiz.setOnClickListener {
            finish()
        }
        binding.startQuiz.setOnClickListener {
            startActivity(Intent(this,QuizLanguageActivity::class.java))
            finish()
        }
    }
}