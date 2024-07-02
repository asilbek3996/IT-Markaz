package uz.fergana.it_center.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.orhanobut.hawk.Hawk
import org.greenrobot.eventbus.EventBus
import uz.fergana.it_center.R
import uz.fergana.it_center.ShowProgress
import uz.fergana.it_center.databinding.ActivityMainBinding
import uz.fergana.it_center.fragment.FavoriteFragment
import uz.fergana.it_center.fragment.HomeFragment
import uz.fergana.it_center.fragment.ProfileFragment
import uz.fergana.it_center.fragment.QuizFragment
import uz.fergana.it_center.model.Notification
import uz.fergana.it_center.model.viewmodel.MainViewModel
import uz.fergana.it_center.utils.Constants
import uz.fergana.it_center.utils.PrefUtils

class MainActivity : AppCompatActivity(), ShowProgress.View {
    private val homeFragment = HomeFragment.newInstance()
    private val favoriteFragment = FavoriteFragment.newInstance()
    private val quizFragment = QuizFragment.newInstance()
    private val profileFragment = ProfileFragment.newInstance()
    var activeFragment: Fragment = homeFragment

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var pref = PrefUtils(this)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        loadData()
        binding.notification.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        uz.fergana.it_center.NotificationManager.notificationsLiveData.observe(this, Observer { notifications ->
            notifications?.let {
                checkNotification()
            }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {
                // Ruxsat so'rash
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }else{
                checkNotification()
            }
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.flContainer, homeFragment, homeFragment.tag).hide(homeFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.flContainer, favoriteFragment, favoriteFragment.tag).hide(favoriteFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.flContainer, quizFragment, quizFragment.tag).hide(quizFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.flContainer, profileFragment, profileFragment.tag).hide(profileFragment)
            .commit()

        supportFragmentManager.beginTransaction().show(activeFragment).commit()


        binding.bottomNavigationView.setOnItemSelectedListener {
            if (it.itemId == R.id.actionHome) {
                supportFragmentManager.beginTransaction().hide(activeFragment)
                    .show(homeFragment)
                    .commit()
                activeFragment = homeFragment
                binding.tvMain.text = "Asosiy"
                binding.refreshMain.visibility = View.VISIBLE
                binding.refreshProgressMain.visibility = View.GONE
                binding.refreshDialog.visibility = View.VISIBLE
                binding.fmNotification.visibility = View.VISIBLE
            } else if (it.itemId == R.id.actionFavorite) {
                supportFragmentManager.beginTransaction().hide(activeFragment)
                    .show(favoriteFragment).commit()
                activeFragment = favoriteFragment
                binding.tvMain.text = "Tanlanganlar"
                binding.refreshDialog.visibility = View.GONE
                binding.fmNotification.visibility = View.GONE
                favoriteFragment.updateData()
            } else if (it.itemId == R.id.actionGame) {
                supportFragmentManager.beginTransaction().hide(activeFragment).show(quizFragment)
                    .commit()
                activeFragment = quizFragment
                binding.tvMain.text = "O'yinlar"
                binding.refreshDialog.visibility = View.GONE
                binding.fmNotification.visibility = View.GONE
            } else if (it.itemId == R.id.actionProfile) {
                supportFragmentManager.beginTransaction().hide(activeFragment).show(profileFragment)
                    .commit()
                activeFragment = profileFragment
                binding.tvMain.text = "Hisob"
                binding.refreshMain.visibility = View.GONE
                binding.refreshProgressMain.visibility = View.GONE
                binding.refreshDialog.visibility = View.GONE
                binding.fmNotification.visibility = View.GONE
            }

            true
        }
//        binding.favoriteRemove.setOnClickListener {
//            if (pref.getFavorite(Constants.favorite).isNullOrEmpty()) {
//                Toast.makeText(
//                    this,
//                    "Siz hali hech qaysi videoni tanlamadingiz",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                clearFavorite()
//            }
//        }
        viewModel.studentData.observe(this) {
            if (it != null) {
                viewModel.insertAllDBStudents(it)
                EventBus.getDefault().post(loadData())
            }
        }
        viewModel.categoriesData.observe(this) {
            viewModel.insertAllDBCategories(it)
        }
        viewModel.courceData.observe(this) {
            viewModel.insertAllDBCource(it)
        }
        viewModel.progress.observe(this) {
            if (it) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        checkNotification()
    }
    fun checkNotification(){
        var a = allNotification()
        var check = 0
        for (i in a){
            if (!i.isRead){
               check++
            }
        }
        if (check>0) {
            binding.ntRead.visibility = View.VISIBLE
            if (check < 100) {
                binding.tvNotification.text = check.toString()
            } else {
                binding.tvNotification.text = "+99"
            }
        }else{
            binding.ntRead.visibility = View.GONE
        }
    }
    private fun allNotification(): ArrayList<Notification> {
        return Hawk.get(Constants.notification, ArrayList())
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            checkNotification()
        }
    }
//    fun clearFavorite() {
//        var pref = PrefUtils(this@MainActivity)
//        val alertDialogBuilder = AlertDialog.Builder(this)
//        alertDialogBuilder.setMessage("Tanlanganlarni tozalab yuborasizmi")
//        alertDialogBuilder.setPositiveButton(
//            "Ha"
//        ) { dialog, which ->
//            pref.clearFavorite()
//            favoriteFragment.updateData()
//            dialog.dismiss()
//        }
//        alertDialogBuilder.setNegativeButton(
//            "Yo'q"
//        ) { dialog, which ->
//            dialog.dismiss()
//        }
//        alertDialogBuilder.setCancelable(false)
//        val alertDialog = alertDialogBuilder.create()
//        alertDialog.show()
//    }

    private fun loadData() {
        viewModel.getStudent()
        viewModel.getCategories()
        viewModel.getCource()
    }

    override fun showProgressBar() {
        binding.refreshProgressMain.visibility = View.VISIBLE
        binding.refreshMain.visibility = View.GONE
    }

    override fun hideProgressBar() {
        binding.refreshProgressMain.visibility = View.GONE
        binding.refreshMain.visibility = View.VISIBLE
    }

    override fun refresh() {
        loadData()
        profileFragment.loadData()
    }

    override fun again() {
        homeFragment.loadData()
    }


}
