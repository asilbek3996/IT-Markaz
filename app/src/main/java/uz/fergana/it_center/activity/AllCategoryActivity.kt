package uz.fergana.it_center.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import uz.fergana.it_center.R
import uz.fergana.it_center.adapter.AllCategoryAdapter
import uz.fergana.it_center.adapter.SearchCategoryAdapter
import uz.fergana.it_center.databinding.ActivityAllCategoryBinding
import uz.fergana.it_center.model.CategoryModel
import uz.fergana.it_center.model.viewmodel.MainViewModel


class AllCategoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityAllCategoryBinding
    lateinit var viewModel: MainViewModel
    lateinit var adapter: SearchCategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val closeButton = binding.searchview.findViewById<ImageView>(androidx.appcompat.R.id
            .search_close_btn)
        closeButton.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
            width = 48.dpToPx()
            height = 48.dpToPx()
        }





    viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        loadData()


        // SearchView ning EditText elementini topish
        val searchEditText = binding.searchview.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)

        // Matn rangini o'zgartirish
        searchEditText.setTextColor(Color.BLACK)
        searchEditText.setHintTextColor(Color.BLACK)

        binding.back.setOnClickListener {
            finish()
        }
        binding.search.setOnClickListener {
            toggleLayoutVisibility()
        }
        binding.fmExit.setOnClickListener {
            animateButton(binding.ivExit)
            binding.linearlayout1.visibility = View.GONE
            binding.linearlayout2.visibility = View.VISIBLE
        }

        binding.fmExit.post {
            val rect = Rect()
            binding.ivExit.getHitRect(rect)
            val extraSpace = ((binding.fmExit.layoutParams.width - binding.ivExit.layoutParams.width) / 2)
            rect.top -= extraSpace
            rect.bottom += extraSpace
            rect.left -= extraSpace
            rect.right += extraSpace
            binding.fmExit.touchDelegate = TouchDelegate(rect, binding.ivExit)
        }
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
                binding.fmExit.setOnClickListener {
                    animateButton(binding.ivExit)
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
        viewModel.getAllDBCategory()
    }
    fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
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

    private fun animateButton(button: ImageView) {
        val scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1.0f, 1.2f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1.0f, 1.2f, 1.0f)

        // AnimatorSet - bir nechta animatsiyalarni birlashtirish uchun
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.duration = 300 // Animatsiya davomiyligi
        animatorSet.start()
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

