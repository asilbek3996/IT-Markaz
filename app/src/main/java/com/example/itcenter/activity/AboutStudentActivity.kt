package com.example.itcenter.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.itcenter.R
import com.example.itcenter.ShowProgress
import com.example.itcenter.databinding.ActivityAboutStudentBinding
import com.example.itcenter.model.viewmodel.MainViewModel
import java.util.Calendar

class AboutStudentActivity : AppCompatActivity() {
    lateinit var binding: ActivityAboutStudentBinding
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        var message = intent.getStringExtra("Student")
        binding.back.setOnClickListener {
            finish()
        }
        filter(message.toString())
        loadData()
    }


    fun loadData(){
        viewModel.getAllStudents()
    }
    fun filter(text: String){
        viewModel.studentData.observe(this, Observer {
            for (student in it){
                if (student.fullName==text){
                    binding.age.text = student.birthday
                    Glide.with(binding.userPhoto).load(student.userPhoto).into(binding.userPhoto)
                    binding.fullName.text = student.fullName
                    var txt = "${student.userPercentage}%"
                    binding.percentage.text = txt
                    binding.direction.text = student.group
                }
            }
        })
    }
}