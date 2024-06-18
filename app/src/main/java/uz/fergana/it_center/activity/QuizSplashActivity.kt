package uz.fergana.it_center.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.fergana.it_center.databinding.ActivityQuizSplashBinding

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