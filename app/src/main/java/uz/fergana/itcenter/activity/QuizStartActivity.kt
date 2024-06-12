package uz.fergana.itcenter.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.fergana.itcenter.databinding.ActivityQuizStartBinding

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