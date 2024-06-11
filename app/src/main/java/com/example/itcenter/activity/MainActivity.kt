package com.example.itcenter.activity

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.itcenter.fragment.*
import com.example.itcenter.R
import com.example.itcenter.ShowProgress
import com.example.itcenter.databinding.ActivityMainBinding
import com.example.itcenter.model.AllStudentModel
import com.example.itcenter.model.Notification
import com.example.itcenter.model.viewmodel.MainViewModel
import com.example.itcenter.utils.Constants
import com.example.itcenter.utils.PrefUtils
import com.orhanobut.hawk.Hawk
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

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
        var notification = findViewById<ImageView>(R.id.notification)
        binding.notification.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        com.example.itcenter.NotificationManager.notificationsLiveData.observe(this, Observer { notifications ->
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
        binding.refreshMain.setOnClickListener {
            // Tugma bosilganda holatini ViewModelga uzatish
            viewModel.setButtonClicked(true)
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            if (it.itemId == R.id.actionHome) {
                supportFragmentManager.beginTransaction().hide(activeFragment)
                    .show(homeFragment)
                    .commit()
                activeFragment = homeFragment
                binding.tvMain.text = "Asosiy"
                binding.crown1.visibility = View.GONE
                binding.crown2.visibility = View.GONE
                notification.visibility = View.VISIBLE
                binding.refreshMain.visibility = View.VISIBLE
                binding.refreshProgressMain.visibility = View.GONE
                binding.favoriteRemove.visibility = View.GONE
                binding.refreshDialog.visibility = View.VISIBLE
            } else if (it.itemId == R.id.actionFavorite) {
                supportFragmentManager.beginTransaction().hide(activeFragment)
                    .show(favoriteFragment).commit()
                activeFragment = favoriteFragment
                binding.tvMain.text = "Tanlanganlar"
                binding.crown1.visibility = View.GONE
                binding.crown2.visibility = View.GONE
                notification.visibility = View.VISIBLE
                binding.refreshDialog.visibility = View.GONE
                binding.favoriteRemove.visibility = View.VISIBLE
                favoriteFragment.updateData()
            } else if (it.itemId == R.id.actionGame) {
                supportFragmentManager.beginTransaction().hide(activeFragment).show(quizFragment)
                    .commit()
                activeFragment = quizFragment
                binding.tvMain.text = "Quiz"
                binding.crown1.visibility = View.VISIBLE
                binding.crown2.visibility = View.VISIBLE
                notification.visibility = View.GONE
                binding.refreshDialog.visibility = View.GONE
                binding.favoriteRemove.visibility = View.GONE
            } else if (it.itemId == R.id.actionProfile) {
                supportFragmentManager.beginTransaction().hide(activeFragment).show(profileFragment)
                    .commit()
                activeFragment = profileFragment
                binding.tvMain.text = "Hisob"
                binding.crown1.visibility = View.GONE
                binding.crown2.visibility = View.GONE
                notification.visibility = View.VISIBLE
                binding.refreshMain.visibility = View.VISIBLE
                binding.refreshProgressMain.visibility = View.GONE
                binding.refreshDialog.visibility = View.VISIBLE
                binding.favoriteRemove.visibility = View.GONE
            }

            true
        }
        binding.favoriteRemove.setOnClickListener {
            if (pref.getFavorite(Constants.favorite).isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "Siz hali hech qaysi videoni tanlamadingiz",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                clearFavorite()
            }
        }
        viewModel.studentData.observe(this) {
            if (it != null) {
                viewModel.insertAllDBStudents(it)
                EventBus.getDefault().post(loadData())
            }
        }
        viewModel.categoriesData.observe(this) {
            viewModel.insertAllDBCategories(it)
        }
        viewModel.progress.observe(this) {
            if (it) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        }
    }

    private fun sendNotification(message: Notification) {
        val intent = Intent(this, NotificationDetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            getIntExtra("notification",message.id)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val channelId = "new_message_channel_id"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.it_center_logo)
            .setContentTitle(message.title)
            .setContentText(message.comment)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "New Message Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {
                // Ruxsat so'rash
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }else{
                Toast.makeText(this, "berilgan", Toast.LENGTH_SHORT).show()
                notificationManager.notify(0, notificationBuilder.build())
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
    fun clearFavorite() {
        var pref = PrefUtils(this@MainActivity)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Tanlanganlarni tozalab yuborasizmi")
        alertDialogBuilder.setPositiveButton(
            "Ha"
        ) { dialog, which ->
            pref.clearFavorite()
            favoriteFragment.updateData()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton(
            "Yo'q"
        ) { dialog, which ->
            dialog.dismiss()
        }
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun loadData() {
        viewModel.getStudent()
        viewModel.getCategories()
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
    }

}
