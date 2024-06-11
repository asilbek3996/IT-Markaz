package com.example.itcenter.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.itcenter.R
import com.example.itcenter.databinding.ActivityCheckBinding
import com.example.itcenter.model.AllCategoryModel
import com.example.itcenter.model.viewmodel.MainViewModel
import com.example.itcenter.utils.PrefUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.hawk.Hawk

class CheckActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckBinding
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        showAlertDialog()
        binding.btnLogin.setOnClickListener {
                loginUser()
        }
        viewModel.getStudent()
    }
    fun loginUser() {
        val email: String = binding.etEmail.text.toString()
        if (email=="") {
            binding.etEmail.error = "Email is invalid "
        }else {
            changeInProgress(true)
            Handler().postDelayed({
                loginAccountInFirebase(email)
            }, 1500)
        }
    }
    fun loginAccountInFirebase(email: String?){
        changeInProgress(true)
        viewModel.studentData.observe(this, Observer {
            for (student in it){
                if (student.id.toString() == email){
                    changeInProgress(false)
                    val pref = PrefUtils(this)
                    validateDate(email)
                    pref.setStudent(student)
                    startActivity(Intent(this@CheckActivity,MainActivity::class.java))
                    finish()
                }else{
                    validateDate(null)
                    changeInProgress(false)
                }
            }
        })
    }
    fun validateDate(email: String?): Boolean {
        if (email==null) {
            binding.etEmail.error = "ID raqam xato"
            return false
        }
        return true
    }
    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Siz IT Center Dang'ara filliali o'quvchisimisiz")
        alertDialogBuilder.setPositiveButton("Ha", DialogInterface.OnClickListener { dialog, which ->
            binding.linearLayout.visibility = View.VISIBLE
            dialog.dismiss()
        })
        alertDialogBuilder.setNegativeButton("Yo'q", DialogInterface.OnClickListener { dialog, which ->
            startActivity(Intent(this@CheckActivity,RegisterActivity::class.java))
            finish()
            dialog.dismiss()
        })
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    fun changeInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnLogin.visibility = View.VISIBLE
        }
    }
}