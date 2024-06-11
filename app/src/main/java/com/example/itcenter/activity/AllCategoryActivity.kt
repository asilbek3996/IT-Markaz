package com.example.itcenter.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.itcenter.adapter.AllCategoryAdapter
import com.example.itcenter.adapter.SearchCategoryAdapter
import com.example.itcenter.databinding.ActivityAllCategoryBinding
import com.example.itcenter.model.AllCategoryModel
import com.example.itcenter.model.CategoryModel
import com.example.itcenter.model.viewmodel.MainViewModel

class AllCategoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityAllCategoryBinding
    lateinit var viewModel: MainViewModel
    lateinit var adapter: SearchCategoryAdapter
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    loadData()
        binding.back.setOnClickListener {
            finish()
        }
        binding.search.setOnClickListener {
            toggleLayoutVisibility()
        }
        binding.ivExit.setOnClickListener {
            binding.linearlayout1.visibility = View.GONE
            binding.linearlayout2.visibility = View.VISIBLE
        }
    binding.swipe.setOnRefreshListener {
        loadData()
    }
    viewModel.progress.observe(this, Observer {
        binding.swipe.isRefreshing = it
    })
    viewModel.categoriesData.observe(this@AllCategoryActivity){
        adapter = SearchCategoryAdapter(it)
        binding.recyclerSearchCategory.layoutManager = GridLayoutManager(this@AllCategoryActivity, 3)
        binding.recyclerSearchCategory.adapter = adapter
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
                    binding.recyclerSearchCategory.visibility = View.GONE
                    binding.recyclerAllCategory.visibility = View.VISIBLE
                    viewModel.getAllCategories()
                } else {
                    binding.recyclerSearchCategory.visibility = View.VISIBLE
                    binding.recyclerAllCategory.visibility = View.GONE
                    filter(newText)
                }
                return true
            }
        })


        viewModel.categoriesData.observe(this) {
            binding.recyclerAllCategory.layoutManager = GridLayoutManager(this, 3)
            binding.recyclerAllCategory.adapter = AllCategoryAdapter(it)
        }
}
    fun loadData(){
        viewModel.getCategories()
    }

    fun filter(text: String){
        val filters: ArrayList<CategoryModel> = ArrayList()
        viewModel.categoriesData.observe(this, Observer {
            for (language in it){
                if (language.language.toLowerCase().contains(text.toLowerCase())){
                    filters.add(language)
                }
            }
            adapter.filter(filters)
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
}