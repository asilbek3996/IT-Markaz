package com.example.itcenter.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itcenter.R
import com.example.itcenter.adapter.DarsAdapter
import com.example.itcenter.adapter.SearchStudentAdapter
import com.example.itcenter.databinding.ActivityDarslarBinding
import com.example.itcenter.model.AllStudentModel
import com.example.itcenter.model.DarslarModel
import com.example.itcenter.model.viewmodel.MainViewModel
import com.example.itcenter.utils.Constants
import com.example.itcenter.utils.PrefUtils

class DarslarActivity : AppCompatActivity() {
    lateinit var binding: ActivityDarslarBinding
    lateinit var viewModel: MainViewModel
    lateinit var adapter: DarsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDarslarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var pref = PrefUtils(this)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val message = intent.getStringExtra("Til")
        val level = intent.getStringExtra("level")
        val group = pref.getStudent(Constants.g)
        binding.tvLanguage.text = message
        viewModel.getLessons()
        if (message == group || level == "free") {
            if (message != null && level != null) {
                loadData(message, level)
            }
            binding.back.setOnClickListener {
                finish()
            }
            binding.search.setOnClickListener {
                toggleLayoutVisibility() // toggleLayoutVisibility funksiyasini chaqirish
            }
            binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    binding.ivExit.setOnClickListener {
                        if (newText.isNullOrEmpty()) {
                            binding.linearlayout1.visibility = View.GONE
                            binding.linearlayout2.visibility = View.VISIBLE
                        } else {
                            binding.searchview.setQuery("", false)
                        }
                    }
                    if (newText.isNullOrEmpty()) {
                        if (level != null && message != null) {
                            loadData(message, level)
                        }
                    } else {
                        if (level != null && message != null) {
                            filter(newText, message, level)
                        }
                    }
                    return true
                }
            })
        } else if (group == "x") {
            var txt = "Siz hech qaysi guruhda o'qimaysiz"
            showAlertDialog(txt, message!!)
        } else {
            var text = "Siz $group guruhida o'qiysiz "
            showAlertDialog(text, message!!)
        }
    }

    fun loadData(message: String, level: String) {
        var kotlin = arrayListOf<DarslarModel>()
        viewModel.lessonsData.observe(this) {
            for (darslar in it) {
                if (darslar.languageName == message && darslar.level == level) {
                    kotlin.add(darslar)
                }
            }
            adapter = DarsAdapter(kotlin)
            binding.recyclerDars.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.recyclerDars.adapter = adapter
        }
    }

    fun filter(message: String, languge: String, level: String) {
        var kotlin = arrayListOf<DarslarModel>()
        viewModel.lessonsData.observe(this, Observer {
            var item = arrayListOf<DarslarModel>()

            for (language in it) {
                if (language.lessonName.toLowerCase().contains(message.toLowerCase())) {
                    item.add(language)
                }
            }
            for (darslar in item) {
                if (darslar.languageName == languge && darslar.level == level) {
                    kotlin.add(darslar)
                }
            }
            adapter.filter(kotlin)
            binding.recyclerDars.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.recyclerDars.adapter = adapter
        })
    }

    private fun toggleLayoutVisibility() {
        // SearchViewning joriy visibility holatini aniqlash
        val currentVisibility = binding.search.visibility
        // Agar SearchView ko'rsatilmoqda bo'lsa
        if (currentVisibility == View.VISIBLE) {
            // LinearLayout1ni ko'rsatish va LinearLayout2ni yashirish
            binding.linearlayout1.visibility = View.VISIBLE
            binding.linearlayout2.visibility = View.GONE
        } else {
            // Aks holda, LinearLayout1ni yashirish va LinearLayout2ni ko'rsatish
            binding.linearlayout1.visibility = View.GONE
            binding.linearlayout2.visibility = View.VISIBLE
        }
    }

    private fun showAlertDialog(text: String, cource: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("$text $cource darslari sizga ochiq emas.\nAgar $cource darslari sizga qiziq bo'lsa telegram botimiz orqali uni sotib olishingiz mumkin.")
        alertDialogBuilder.setPositiveButton(
            "Sotib olish",
            DialogInterface.OnClickListener { dialog, which ->
                val link = "https://t.me/dangara_itcenterbot" // Sizning linkingizni o'zgartiring
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
                finish()
            })
        alertDialogBuilder.setNegativeButton(
            "Ortga qaytish",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                finish()
            })
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}