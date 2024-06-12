package uz.fergana.itcenter.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import uz.fergana.itcenter.R
import uz.fergana.itcenter.databinding.ActivityVideoBinding
import uz.fergana.itcenter.model.viewmodel.MainViewModel
import uz.fergana.itcenter.utils.Constants
import uz.fergana.itcenter.utils.PrefUtils

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
