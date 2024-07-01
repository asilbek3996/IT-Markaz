//package uz.fergana.it_center.activity
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import android.webkit.WebChromeClient
//import android.webkit.WebSettings
//import android.webkit.WebViewClient
//import android.widget.FrameLayout
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.ViewModelProvider
//import uz.fergana.it_center.R
//import uz.fergana.it_center.databinding.ActivityVideoBinding
//import uz.fergana.it_center.model.viewmodel.MainViewModel
//import uz.fergana.it_center.utils.Constants
//import uz.fergana.it_center.utils.PrefUtils
//
//class VideoActivity : AppCompatActivity() {
//    lateinit var binding: ActivityVideoBinding
//    lateinit var viewModel: MainViewModel
//
//    @SuppressLint("SetJavaScriptEnabled")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityVideoBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        val message = intent.getIntExtra("video", -1)
//        viewModel.getLessons()
//        viewModel.lessonsData.observe(this) {
//            for (video in it) {
//                if (video.id == message) {
//                    binding.tvLanguage.text = video.lessonName
//                    setupWebView()
//                    loadVideo(video.videoLink)
//
//                    val txt = video.homework
//                    binding.tvSubtitr.text = txt
//                    val pref = PrefUtils(this)
//
//                    if (pref.checkFavorite(Constants.favorite, message)) {
//                        binding.save.setImageResource(R.drawable.heart)
//                    } else {
//                        binding.save.setImageResource(R.drawable.favorite)
//                    }
//                    binding.homework.setOnClickListener {
//                        val link = "https://t.me/IT_center_supportbot" // Sizning linkingizni o'zgartiring
//                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
//                        startActivity(intent)
//                    }
//                    binding.save.setOnClickListener {
//                        pref.saveFavorite(Constants.favorite, video)
//                        Toast.makeText(
//                            this,
//                            "${pref.checkFavorite(Constants.favorite, message)}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        if (pref.checkFavorite(Constants.favorite, message)) {
//                            binding.save.setImageResource(R.drawable.heart)
//                        } else {
//                            binding.save.setImageResource(R.drawable.favorite)
//                        }
//                    }
//
//                    binding.back2.setOnClickListener {
//                        finish()
//                    }
//                }
//            }
//        }
//    }
//
//    @SuppressLint("SetJavaScriptEnabled")
//    private fun setupWebView() {
//        val webSettings = binding.webView.settings
//        webSettings.javaScriptEnabled = true
//        webSettings.domStorageEnabled = true
//        webSettings.loadsImagesAutomatically = true
//        webSettings.defaultTextEncodingName = "false"
//
//
//        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
//
//        binding.webView.webChromeClient = MyChrome()
//        binding.webView.webViewClient = WebViewClient()
//    }
//
//    private fun loadVideo(videoLink: String) {
//        // Construct YouTube embed URL with parameters to hide controls and branding
//        val videoUrl = videoLink.replace("watch?v=", "embed/") + "?controls=0&modestbranding=1"
//        binding.webView.loadUrl(videoUrl)
//    }
//
//    private inner class MyChrome : WebChromeClient() {
//        private var fullscreen: View? = null
//
//        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
//            super.onShowCustomView(view, callback)
//            binding.webView.visibility = View.GONE
//
//            if (fullscreen != null) {
//                (window.decorView as FrameLayout).removeView(fullscreen)
//            }
//
//            fullscreen = view
//            (window.decorView as FrameLayout).addView(fullscreen, FrameLayout.LayoutParams(-1, -1))
//            fullscreen?.visibility = View.VISIBLE
//        }
//
//        override fun onHideCustomView() {
//            super.onHideCustomView()
//            fullscreen?.visibility = View.GONE
//            binding.webView.visibility = View.VISIBLE
//        }
//    }
//}
package uz.fergana.it_center.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import uz.fergana.it_center.R
import uz.fergana.it_center.databinding.ActivityVideoBinding
import uz.fergana.it_center.model.viewmodel.MainViewModel
import uz.fergana.it_center.utils.Constants
import uz.fergana.it_center.utils.PrefUtils

class VideoActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoBinding
    lateinit var viewModel: MainViewModel

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val message = intent.getIntExtra("video", -1)
        viewModel.getLessons()
        viewModel.lessonsData.observe(this) {
            for (video in it) {
                if (video.id == message) {
                    binding.tvLanguage.text = video.lessonName
                    setupWebView()
                    loadVideo(video.videoLink)

                    val txt = video.homework
                    binding.tvSubtitr.text = txt
                    val pref = PrefUtils(this)

                    if (pref.checkFavorite(Constants.favorite, message)) {
                        binding.save.setImageResource(R.drawable.heart)
                    } else {
                        binding.save.setImageResource(R.drawable.favorite)
                    }

                    var link = ""
                    when(video.languageName) {
                        "Kotlin" -> {
                            binding.home.visibility = View.VISIBLE
                            binding.homeWork.visibility = View.VISIBLE
                            link =
                                "https://play.kotlinlang.org/#eyJ2ZXJzaW9uIjoiMi4wLjAiLCJwbGF0Zm9ybSI6ImphdmEiLCJhcmdzIjoiIiwibm9uZU1hcmtlcnMiOnRydWUsInRoZW1lIjoiaWRlYSIsImNvZGUiOiIvKipcbiAqIFlvdSBjYW4gZWRpdCwgcnVuLCBhbmQgc2hhcmUgdGhpcyBjb2RlLlxuICogcGxheS5rb3RsaW5sYW5nLm9yZ1xuICovXG5mdW4gbWFpbigpIHtcbiAgICBwcmludGxuKFwiSGVsbG8sIHdvcmxkISEhXCIpXG59In0="
                        }

                        "Java" -> {
                            binding.home.visibility = View.VISIBLE
                            binding.homeWork.visibility = View.VISIBLE
                            link = "https://codapi.org/java/"
                        }

                        "C++" -> {
                            binding.home.visibility = View.VISIBLE
                            binding.homeWork.visibility = View.VISIBLE
                            link = "https://codapi.org/cpp/"
                        }

                        "Python" -> {
                            binding.home.visibility = View.VISIBLE
                            binding.homeWork.visibility = View.VISIBLE
                            link =
                                "https://python-playground.netlify.app/#ewAiAG0AYQBpAG4ALgBwAHkAIgA6ACIAcAByAGkAbgB0ACgAJwBIAGUAbABsAG8AIABXAG8AcgBsAGQAIQAnACkAIgB9AA=="
                        }

                        "Frontend" -> {
                            binding.home.visibility = View.VISIBLE
                            binding.homeWork.visibility = View.VISIBLE
                            link = "https://codepen.io/pen/"
                        }
                        "Android" -> {
                            binding.home.visibility = View.GONE
                            binding.homeWork.visibility = View.GONE
                        }
                        "Backend" -> {
                            binding.home.visibility = View.GONE
                            binding.homeWork.visibility = View.GONE
                        }
                        "Literacy" -> {
                            binding.home.visibility = View.GONE
                            binding.homeWork.visibility = View.GONE
                        }
                    }
                    binding.homeWork.setOnClickListener {

                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        startActivity(intent)
                    }
                    binding.save.setOnClickListener {
                        pref.saveFavorite(Constants.favorite, video)
                        Toast.makeText(
                            this,
                            "${pref.checkFavorite(Constants.favorite, message)}",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (pref.checkFavorite(Constants.favorite, message)) {
                            binding.save.setImageResource(R.drawable.heart)
                        } else {
                            binding.save.setImageResource(R.drawable.favorite)
                        }
                    }

                    binding.back2.setOnClickListener {
                        finish()
                    }
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadsImagesAutomatically = true
        webSettings.defaultTextEncodingName = "false"


        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW

        binding.webView.webChromeClient = MyChrome()
        binding.webView.webViewClient = WebViewClient()
    }

    private fun loadVideo(videoLink: String) {
        // Construct YouTube embed URL with parameters to hide controls and branding
        val videoUrl = videoLink.replace("watch?v=", "embed/") + "?controls=0&modestbranding=1"
        binding.webView.loadUrl(videoUrl)
    }

    private inner class MyChrome : WebChromeClient() {
        private var fullscreen: View? = null

        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
            super.onShowCustomView(view, callback)
            binding.webView.visibility = View.GONE

            if (fullscreen != null) {
                (window.decorView as FrameLayout).removeView(fullscreen)
            }

            fullscreen = view
            (window.decorView as FrameLayout).addView(fullscreen, FrameLayout.LayoutParams(-1, -1))
            fullscreen?.visibility = View.VISIBLE
        }

        override fun onHideCustomView() {
            super.onHideCustomView()
            fullscreen?.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE
        }
    }
}

