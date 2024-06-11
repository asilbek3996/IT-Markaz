package com.example.itcenter.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.itcenter.R
import com.example.itcenter.ShowProgress
import com.example.itcenter.adapter.AllCategoryAdapter
import com.example.itcenter.adapter.QuizLanguage
import com.example.itcenter.adapter.SearchCategoryAdapter
import com.example.itcenter.databinding.ActivityQuizLanguageBinding
import com.example.itcenter.model.viewmodel.MainViewModel

class QuizLanguageActivity : AppCompatActivity() {
    lateinit var binding: ActivityQuizLanguageBinding
    lateinit var viewModel: MainViewModel
    lateinit var adapter: SearchCategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        loadData()
        binding.back.setOnClickListener {
            startActivity(Intent(this,QuizStartActivity::class.java))
            finish()
        }
        viewModel.categoriesData.observe(this) {
            binding.recyclerQuiz.layoutManager = GridLayoutManager(this, 2)
            binding.recyclerQuiz.adapter = QuizLanguage(it,this)
        }
    }

    fun loadData(){
        viewModel.getAllDBCategory()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,QuizStartActivity::class.java))
        finish()
    }
}