package com.example.itcenter.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.itcenter.R
import com.example.itcenter.databinding.ActivityLevelBinding
import com.example.itcenter.model.viewmodel.MainViewModel

class LevelActivity : AppCompatActivity() {
    lateinit var binding: ActivityLevelBinding
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val message = intent.getStringExtra("Til")
        viewModel.categoriesData.observe(this){
            for (item in it){
                if (item.language == message){
                    Glide.with(binding.img).load(item.image).into(binding.img)
                    Glide.with(binding.topImage).load(item.image).into(binding.topImage)
                    Glide.with(binding.ivBegin).load(item.levelImage).into(binding.ivBegin)
                    Glide.with(binding.ivFree).load(item.levelImage).into(binding.ivFree)
                    Glide.with(binding.ivMedium).load(item.levelImage).into(binding.ivMedium)
                    Glide.with(binding.ivAdvanced).load(item.levelImage).into(binding.ivAdvanced)
                }
            }
        }
        var txt = ""
        if (message == "Kompyuter Savodxonligi" || message == "Microsoft Office"){
            txt = "$message darslari"
        }else{
            txt = "$message dasturlash tili"
        }
        binding.tvName.text = txt
        binding.back.setOnClickListener {
            finish()
        }
        val intent = Intent(this, DarslarActivity::class.java)
        binding.free.setOnClickListener {
            intent.apply {
                putExtra("level", "free")
                putExtra("Til", message)
            }
            startActivity(intent)
        }
        binding.begin.setOnClickListener {
            intent.apply {
                putExtra("level", "begin")
                putExtra("Til", message)
            }
            startActivity(intent)
        }
        binding.medium.setOnClickListener {
            intent.apply {
                putExtra("level", "medium")
                putExtra("Til", message)
            }
            startActivity(intent)
        }
        binding.advanced.setOnClickListener {
            intent.apply {
                putExtra("level", "advanced")
                putExtra("Til", message)
            }
            startActivity(intent)
        }
        viewModel.getCategories()
    }
}