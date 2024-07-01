package uz.fergana.it_center.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.fergana.it_center.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    private val apiRepository = ApiReository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Thread.sleep(3000)
//        installSplashScreen()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.animationView.postDelayed({
             startActivity(Intent(this,MainActivity::class.java))
            finish()
        },4000)
//        fetchDataFromApi()
    }
//    private fun fetchDataFromApi() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val data = apiRepository.fetchData()
//
//            withContext(Dispatchers.Main) {
//                // Once data is fetched, navigate to the main activity
//                val intent = Intent(this@SplashActivity, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }
//    }



}