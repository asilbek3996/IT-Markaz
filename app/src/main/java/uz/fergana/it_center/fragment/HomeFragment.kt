package uz.fergana.it_center.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import uz.fergana.it_center.activity.AllCategoryActivity
import uz.fergana.it_center.ShowProgress
import uz.fergana.it_center.activity.LevelActivity
import uz.fergana.it_center.adapter.ImageAdapter
import uz.fergana.it_center.adapter.TopStudentAdapter
import uz.fergana.it_center.databinding.FragmentHomeBinding
import uz.fergana.it_center.model.AllStudentModel
import uz.fergana.it_center.model.CategoryModel
import uz.fergana.it_center.model.GroupModel
import uz.fergana.it_center.model.viewmodel.MainViewModel
import uz.fergana.it_center.utils.Constants
import uz.fergana.it_center.utils.PrefUtils
import kotlin.math.abs


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    var handler = Handler()
    lateinit var viewModel: MainViewModel
    var item = arrayListOf<CategoryModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        viewModel.progress.observe(requireActivity(), Observer {
            binding.swipe.isRefreshing = it
            if (it) {
                (activity as? ShowProgress.View)?.showProgressBar()
            } else {
                (activity as? ShowProgress.View)?.hideProgressBar()
            }
        })

        binding.swipe.setOnRefreshListener {
            loadData()
            (activity as? ShowProgress.View)?.refresh()
        }

        binding.tvAll.setOnClickListener {
            startActivity(Intent(requireContext(), AllCategoryActivity::class.java))
        }
        val intent = Intent(requireContext(), LevelActivity::class.java)
        binding.category1.setOnClickListener {
            intent.apply {
                putExtra("Til", item[0].language)
            }
            startActivity(intent)
        }
        binding.category2.setOnClickListener {
            intent.apply {
                putExtra("Til", item[1].language)
            }
            startActivity(intent)
        }
        binding.category3.setOnClickListener {
            intent.apply {
                putExtra("Til", item[2].language)
            }
            startActivity(intent)
        }
        viewModel.studentData.observe(requireActivity(), Observer {
                viewModel.shimmer.observe(requireActivity()) { status ->
                    when (status) {
                        0 -> {
                            binding.swipe.visibility = View.GONE
                            binding.shimmerLayout.visibility = View.VISIBLE
                        }

                        1 -> {
                            Handler().postDelayed({
                                binding.swipe.visibility = View.VISIBLE
                                binding.shimmerLayout.visibility = View.GONE
                                topTest(it)
                            }, 1000)
                        }

                        2 -> {
                            binding.swipe.visibility = View.GONE
                            binding.shimmerLayout.visibility = View.VISIBLE
                            showErrorDialog()
                        }
                    }
                }
        })


        setUpTransformer()
        viewModel.adsData.observe(requireActivity(), Observer {

            binding.viewPager.adapter = ImageAdapter(it, binding.viewPager)
            binding.viewPager.offscreenPageLimit = 3
            binding.viewPager.clipToPadding = false
            binding.viewPager.clipChildren = false
            binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        })
        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 2000)
            }
        })
        viewModel.categoriesData.observe(requireActivity(), Observer {
            categories(it)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
    private fun showErrorDialog() {
        AlertDialog.Builder(requireActivity())
            .setMessage("Serverga ulanishda xatolik yuz berdi. Iltimos, yana bir bor urinib ko'ring")
            .setPositiveButton("Qayta urinib ko'ring") { dialog, _ ->
                loadData()
                dialog.dismiss()
            }
            .show()
    }
    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable, 3000)
    }

    private val runnable = Runnable {
        binding.viewPager.currentItem += 1
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(0))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        binding.viewPager.setPageTransformer(transformer)
    }

    fun loadData() {
        viewModel.getOffers()
        viewModel.getCategories()
        viewModel.getAllStudents()
    }

    private fun topTest(students: List<AllStudentModel>) {
        val pref = PrefUtils(requireContext())
        var android = arrayListOf<AllStudentModel>()
        var python = arrayListOf<AllStudentModel>()
        var java = arrayListOf<AllStudentModel>()
        var kotlin = arrayListOf<AllStudentModel>()
        var cpp = arrayListOf<AllStudentModel>()
        var computerLiteracy = arrayListOf<AllStudentModel>()
        var scratch = arrayListOf<AllStudentModel>()
        var frontend = arrayListOf<AllStudentModel>()
        var backend = arrayListOf<AllStudentModel>()
        for (student in students) {
            if (student.group == "Android") {
                android.add(student)
            } else if (student.group == "Python") {
                python.add(student)
            } else if (student.group == "Kotlin") {
                kotlin.add(student)
            } else if (student.group == "Java") {
                java.add(student)
            } else if (student.group == "C++") {
                cpp.add(student)
            } else if (student.group == "Scratch") {
                scratch.add(student)
            } else if (student.group == "Literacy") {
                computerLiteracy.add(student)
            } else if (student.group == "Frontend") {
                frontend.add(student)
            } else if (student.group == "Backend") {
                backend.add(student)
            }
        }
        var group = arrayListOf<GroupModel>()
        var item = arrayListOf<GroupModel>()
        var items = listOf(
            GroupModel("Java", java, "Java"),
            GroupModel("Kotlin", kotlin, "Kotlin"),
            GroupModel("Android", android, "Android"),
            GroupModel("Python", python, "Python"),
            GroupModel("C++", cpp, "C++"),
            GroupModel("Kompyuter Savodxonligi", computerLiteracy, "Literacy"),
            GroupModel("Scratch", scratch, "Scratch"),
            GroupModel("Frontend", frontend, "Frontend"),
            GroupModel("Backend", backend, "Backend"),
        )
        for (it in items) {
            if (it.group == pref.getStudent(Constants.group)) {
                group.add(it)
            } else {
                item.add(it)
            }
        }
        group.addAll(item)
        binding.recyclerGroup.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerGroup.adapter = TopStudentAdapter(group)
    }

    private fun categories(category: List<CategoryModel>) {
        var pref = PrefUtils(requireContext())
        var item2 = arrayListOf<CategoryModel>()
        for (it in category) {
            if (it.language == pref.getStudent(Constants.g)) {
                item.add(it)
            } else {
                item2.add(it)
            }
        }
        item.addAll(item2)
        Glide.with(binding.imgCategory1).load(item[0].image).into(binding.imgCategory1)
        Glide.with(binding.imgCategory2).load(item[1].image).into(binding.imgCategory2)
        Glide.with(binding.imgCategory3).load(item[2].image).into(binding.imgCategory3)
        binding.nameCategory1.text = item[0].language
        binding.nameCategory2.text = item[1].language
        binding.nameCategory3.text = item[2].language
    }

}