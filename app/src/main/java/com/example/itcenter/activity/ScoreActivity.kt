package com.example.itcenter.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.itcenter.databinding.ActivityScoreBinding
import com.example.itcenter.model.QuestionModel
import com.example.itcenter.utils.PrefUtils

class ScoreActivity : AppCompatActivity() {
    var score: Double = 0.0
    lateinit var binding: ActivityScoreBinding
    lateinit var lan:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        score=intent.getDoubleExtra("Score",0.0)
        var right=intent.getIntExtra("right",0)
        var wrong=intent.getIntExtra("wrong",0)
        var level1=intent.getStringExtra("level")
        var language=intent.getStringExtra("language")
        if (language != null) {
            lan= language
        }
        binding.totalRight.text = right.toString()
        binding.totalWrong.text = wrong.toString()
        var s: String
        if (score % 1 == 0.0) {
            // Agar son butun bo'lsa
            s= score.toInt().toString()
        } else {
            // Agar son kasr qismiga ega bo'lsa
            s = String.format("%.1f", score)
        }
        val txt = "$s%"
        binding.score.text=txt
        var pref = PrefUtils(this)
        var level = pref.getQuizLevel()
        if (score==100.0){
            level+=1
        }
        pref.setQuizLevel(level)
        binding.backMain.setOnClickListener {
            val intent = Intent(this, QuizLevelActivity::class.java)
            intent.putExtra("language",language)
            startActivity(intent)
            finish()
        }
        binding.refresh.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("level",level1)
            intent.putExtra("language",language)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, QuizLevelActivity::class.java)
        intent.putExtra("language",lan)
        startActivity(intent)
        finish()
    }
}