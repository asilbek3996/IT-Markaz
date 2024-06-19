package uz.fergana.it_center.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.TouchDelegate
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import uz.fergana.it_center.adapter.SearchStudentAdapter
import uz.fergana.it_center.adapter.StudentAdapter
import uz.fergana.it_center.databinding.ActivityAllStudentBinding
import uz.fergana.it_center.model.AllStudentModel
import uz.fergana.it_center.model.viewmodel.MainViewModel

class AllStudentActivity : AppCompatActivity() {
    lateinit var binding: ActivityAllStudentBinding
    lateinit var viewModel: MainViewModel
    lateinit var adapter: SearchStudentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        val message = intent.getStringExtra("Group")
        val name = intent.getStringExtra("GroupName")
        binding.tvGroup.text = name
        viewModel.studentData.observe(this){
            adapter = SearchStudentAdapter(it)
            binding.recyclerSearchStudent.layoutManager = GridLayoutManager(this,3)
            binding.recyclerSearchStudent.adapter = adapter
            getStudent(it,message!!)
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
                    binding.recyclerSearchStudent.visibility = View.GONE
                    binding.recyclerStudent.visibility = View.VISIBLE
                    loadData()
                } else {
                    binding.recyclerSearchStudent.visibility = View.VISIBLE
                    binding.recyclerStudent.visibility = View.GONE
                    filter(newText, message!!)
                }

                return true
            }
        })

    }
    fun loadData(){
        viewModel.getAllStudents()
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
    fun getStudent(students: List<AllStudentModel>, message: String){
        var item = arrayListOf<AllStudentModel>()

        for (student in students){
            if (student.group==message){
                item.add(student)
            }
        }
        binding.recyclerStudent.layoutManager = GridLayoutManager(this,3)
        binding.recyclerStudent.adapter = StudentAdapter(item)
    }
    fun filter(text: String, message: String){
        val filters: ArrayList<AllStudentModel> = ArrayList()
        viewModel.studentData.observe(this, Observer {
            var item = arrayListOf<AllStudentModel>()

            for (student in it){
                if (student.group==message){
                    item.add(student)
                }
            }
            for (language in item){
                if (language.fullName.toLowerCase().contains(text.toLowerCase())){
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