package com.example.itcenter.activity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.itcenter.databinding.ActivitySplashBinding
import com.example.itcenter.model.viewmodel.MainViewModel
import com.example.itcenter.utils.PrefUtils
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.hawk.Hawk

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        var pref = PrefUtils(this)
        val idRaqami = pref.getID()
        viewModel.getStudent()
        binding.animationViews.postDelayed({
                        if (idRaqami!=-1){
                            viewModel.studentData.observe(this){
                                var id = it.filter { it.id==idRaqami }
                                if (id.isNullOrEmpty()) {
                                    val pref = PrefUtils(this)
                                    pref.clear()
                                    startActivity(Intent(this, CheckActivity::class.java))
                                    this.finish()
                                }else{
                                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                                    finish()
                                }
                            }
                        }else{
                                startActivity(Intent(this@SplashActivity, CheckActivity::class.java))
                                finish()
                        }
        },2950)
    }

}