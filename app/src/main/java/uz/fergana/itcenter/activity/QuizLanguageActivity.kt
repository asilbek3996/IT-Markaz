package uz.fergana.itcenter.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import uz.fergana.itcenter.adapter.QuizLanguage
import uz.fergana.itcenter.adapter.SearchCategoryAdapter
import uz.fergana.itcenter.databinding.ActivityQuizLanguageBinding
import uz.fergana.itcenter.model.viewmodel.MainViewModel

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