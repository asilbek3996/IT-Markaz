package com.example.itcenter.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itcenter.R
import com.example.itcenter.adapter.QuizLanguage
import com.example.itcenter.adapter.QuizLevelAdapter
import com.example.itcenter.adapter.level
import com.example.itcenter.databinding.ActivityQuizLevelBinding
import com.example.itcenter.model.QuestionModel
import com.example.itcenter.model.QuizLevelModel
import com.example.itcenter.utils.PrefUtils

class QuizLevelActivity : AppCompatActivity(),level {
    lateinit var binding: ActivityQuizLevelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val message = intent.getStringExtra("language")
        val list = listOf(
            QuizLevelModel(1,"1-bosqich"),
            QuizLevelModel(2,"2-bosqich"),
            QuizLevelModel(3,"3-bosqich"),
            QuizLevelModel(4,"4-bosqich"),
            QuizLevelModel(5,"5-bosqich"),
            QuizLevelModel(6,"6-bosqich"),
            QuizLevelModel(7,"7-bosqich"),
            QuizLevelModel(8,"8-bosqich"),
            QuizLevelModel(9,"9-bosqich"),
            QuizLevelModel(10,"10-bosqich"),
        )
        binding.back.setOnClickListener {
            startActivity(Intent(this,QuizLanguageActivity::class.java))
            finish()
        }
        binding.recyclerQuiz.layoutManager = LinearLayoutManager(this)
        binding.recyclerQuiz.adapter = QuizLevelAdapter(list,this, message.toString(),this@QuizLevelActivity)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,QuizLanguageActivity::class.java))
        finish()
    }

    override fun onItemClicked(position: QuizLevelModel) {
        var pref = PrefUtils(this)
        var level = pref.getQuizLevel()
        showAlertDialog(position.text,level)
    }

    private fun showAlertDialog(level: String,l: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Siz hali ${level}ga yetib kelmagansiz. Siz ${l}-bosqichni yechib bo'lmagansiz. ${l}-bosqichni yeching")
        alertDialogBuilder.setPositiveButton("Tushunarli", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}