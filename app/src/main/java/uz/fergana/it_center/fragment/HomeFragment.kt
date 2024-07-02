package uz.fergana.it_center.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.play.integrity.internal.i
import uz.fergana.it_center.activity.AllCategoryActivity
import uz.fergana.it_center.ShowProgress
import uz.fergana.it_center.activity.LevelActivity
import uz.fergana.it_center.activity.RegisterActivity
import uz.fergana.it_center.activity.RegistrationActivity
import uz.fergana.it_center.adapter.ImageAdapter
import uz.fergana.it_center.adapter.TopStudentAdapter
import uz.fergana.it_center.databinding.FragmentHomeBinding
import uz.fergana.it_center.model.AllStudentModel
import uz.fergana.it_center.model.CategoryModel
import uz.fergana.it_center.model.CourceModel
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
    var cource = arrayListOf<CourceModel>()
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

        binding.tvAllCource.setOnClickListener {
            startActivity(Intent(requireContext(), AllCategoryActivity::class.java))
        }
        binding.tvAll.setOnClickListener {
            startActivity(Intent(requireContext(), AllCategoryActivity::class.java))
        }
        val intent = Intent(requireContext(), LevelActivity::class.java)
        binding.cource1.setOnClickListener {
            intent.apply {
                putExtra("Til", cource[0].language)
            }
            startActivity(intent)
        }
        binding.course2.setOnClickListener {
            intent.apply {
                putExtra("Til", cource[1].language)
            }
            startActivity(intent)
        }
        binding.cource3.setOnClickListener {
            intent.apply {
                putExtra("Til", cource[2].language)
            }
            startActivity(intent)
        }
        binding.category1.setOnClickListener {
            startActivity(Intent(requireContext(),RegistrationActivity::class.java))
        }
        viewModel.studentData.observe(requireActivity(), Observer {
            topTest(it)
                viewModel.shimmer.observe(requireActivity()) { status ->
                    when (status) {
                        0 -> {
                            binding.swipe.visibility = View.GONE
                            binding.shimmerLayout.visibility = View.VISIBLE
                        }

                        1 -> {
                            Handler().postDelayed({
                                Toast.makeText(requireContext(), "${it.size}", Toast.LENGTH_SHORT).show()
                                binding.swipe.visibility = View.VISIBLE
                                binding.shimmerLayout.visibility = View.GONE
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
            if (it.isNotEmpty()) {
                categories(it)
            }else{
                showErrorDialog()
            }
        })
        viewModel.courceData.observe(requireActivity(), Observer {
            Toast.makeText(requireContext(), "${it.size}", Toast.LENGTH_SHORT).show()
            var pref = PrefUtils(requireContext())
            if (it.isNotEmpty()) {
                if (pref.getID()!=0) {
                    var userC = it.filter { it.language == pref.getStudent(Constants.g) }
                    userCource(userC[0])
                    binding.notUser.visibility = View.GONE
                    binding.userCource.visibility = View.VISIBLE
                }else {
                    cource(it)
                    binding.notUser.visibility = View.VISIBLE
                    binding.userCource.visibility = View.GONE
                }
            }else{
                showErrorDialog()
            }
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
        viewModel.getAllDBCategory()
        viewModel.getAllDBCource()
        viewModel.getAllStudents()
    }

    private fun topTest(students: List<AllStudentModel>) {
        val pref = PrefUtils(requireContext())
        var group = arrayListOf<GroupModel>()
        var groupName = arrayListOf<String>()
        viewModel.courceData.observe(requireActivity()){
            for (i in it){
                if (pref.getID()==0) {
                    groupName.add(i.language.toString())
                }else{
                    if (pref.getStudent(Constants.g)==i.language){
                        groupName.add(i.language.toString())
                    }
                }
            }
        }
for (name in groupName){
    var item = arrayListOf<AllStudentModel>()
    for (student in students) {
        if (student.group == name) {
            item.add(student)
        }
    }
    group.add(GroupModel(name,item,name))
}
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
    private fun cource(c: List<CourceModel>) {
        cource.addAll(c)
        Glide.with(binding.imgCource1).load(cource[0].image).into(binding.imgCource1)
        Glide.with(binding.imgCource2).load(cource[1].image).into(binding.imgCource2)
        Glide.with(binding.imgCource3).load(cource[2].image).into(binding.imgCource3)
        binding.nameCource1.text = cource[0].language
        binding.nameCource2.text = cource[1].language
        binding.nameCourse3.text = cource[2].language
    }
    private fun userCource(userCource: CourceModel) {
        Glide.with(binding.userCourceImage).load(userCource.levelImage).into(binding.userCourceImage)
        binding.userCourceName.text = userCource.language
    }

}