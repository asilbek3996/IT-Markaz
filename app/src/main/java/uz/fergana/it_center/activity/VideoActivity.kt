package uz.fergana.it_center.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import uz.fergana.it_center.R
import uz.fergana.it_center.databinding.ActivityVideoBinding
import uz.fergana.it_center.model.viewmodel.MainViewModel
import uz.fergana.it_center.utils.Constants
import uz.fergana.it_center.utils.PrefUtils

class VideoActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoBinding
    lateinit var viewModel: MainViewModel
    var homeWork= 0

    private var language: String? = null
    private var level: String? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val message = intent.getIntExtra("video", -1)
        intent.getStringExtra("Til")
        intent.getStringExtra("level")
        val pref = PrefUtils(this)
        viewModel.getLessons()
        binding.screenShare.setOnClickListener{
            homeWork = 1
            val link = "https://t.me/IT_center_supportbot" // Sizning linkingizni o'zgartiring
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }
        if (pref.checkFinishLesson(message)){
            binding.homeWork.visibility = View.GONE
            binding.screenShare.visibility = View.GONE
            binding.check.setImageResource(R.drawable.is_read)
            binding.endTXT.text = "Tugatildi"
        }else{
        binding.theEnd.setOnClickListener {
            if (homeWork==1){
                theEnd(message)
            }else{
                showAlertDialog(message)
            }
        }
        }
        viewModel.lessonsData.observe(this) {
            for (video in it) {
                if (video.id == message) {
                    binding.tvLanguage.text = video.lessonName
                    setupWebView()
                    loadVideo(video.videoLink)

                    val txt = video.homework
                    binding.tvSubtitr.text = txt

                    if (pref.checkFavorite(Constants.favorite, message)) {
                        binding.save.setImageResource(R.drawable.heart)
                    } else {
                        binding.save.setImageResource(R.drawable.favorite)
                    }

                    if (pref.checkFinishLesson(message)){
                        binding.homeWork.visibility = View.GONE
                        binding.screenShare.visibility = View.GONE
                        binding.check.setImageResource(R.drawable.is_read)
                        binding.endTXT.text = "Tugatildi"
                    }else {
                        var link = ""
                        when (video.languageName) {
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
                        val returnIntent = Intent()
                        returnIntent.putExtra("Til", language)  // Misol uchun
                        returnIntent.putExtra("level", level)  // Misol uchun
                        setResult(RESULT_OK, returnIntent)
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
        binding.webView.addJavascriptInterface(VideoEndListener(), "VideoEndListener")
    }

    private fun loadVideo(videoLink: String) {
        // Construct YouTube embed URL with parameters to hide controls and branding
        val videoUrl = videoLink.replace("watch?v=", "embed/") + "?controls=0&modestbranding=1&showinfo=0&rel=0&iv_load_policy=3&playsinline=1"
        binding.webView.loadUrl(videoUrl)
    }

    private inner class VideoEndListener {
        @JavascriptInterface
        fun onVideoEnd() {
            runOnUiThread {
                Toast.makeText(this@VideoActivity, "Video ended", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class MyChrome : WebChromeClient() {
        private var customView: View? = null
        private var customViewCallback: CustomViewCallback? = null
        private var originalOrientation: Int = 0
        private var originalSystemUiVisibility: Int = 0

        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
            if (customView != null) {
                callback?.onCustomViewHidden()
                return
            }

            customView = view
            customViewCallback = callback

            originalSystemUiVisibility = window.decorView.systemUiVisibility
            originalOrientation = requestedOrientation

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

            (window.decorView as FrameLayout).addView(customView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
            binding.webView.visibility = View.GONE
        }

        override fun onHideCustomView() {
            (window.decorView as FrameLayout).removeView(customView)
            customView = null
            customViewCallback?.onCustomViewHidden()

            window.decorView.systemUiVisibility = originalSystemUiVisibility
            requestedOrientation = originalOrientation

            binding.webView.visibility = View.VISIBLE
        }
    }
    private fun showAlertDialog(id: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this@VideoActivity)
        alertDialogBuilder.setMessage("Siz hali uyga vazifani topshirmadingiz darsni yakunlashdan oldin uyga vazifani topshiring. Aks holda sizga bal berilmasligi mumkin!")
        alertDialogBuilder.setPositiveButton(
            "Uyga vazifani topshirish",
            DialogInterface.OnClickListener { dialog, which ->
                homeWork = 1
                val link = "https://t.me/IT_center_supportbot" // Sizning linkingizni o'zgartiring
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            })
        alertDialogBuilder.setNegativeButton(
            "Darsni tugatish",
            DialogInterface.OnClickListener { dialog, which ->
                theEnd(id)
                dialog.dismiss()
            })
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        val messageTextView = alertDialog.findViewById<TextView>(android.R.id.message)
        messageTextView?.setTextColor(Color.BLACK)

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton?.setTextColor(ContextCompat.getColor(this@VideoActivity, R.color.greens))

        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton?.setTextColor(ContextCompat.getColor(this@VideoActivity, R.color.greens))
        alertDialog.window?.setBackgroundDrawableResource(R.color.white)
    }
fun theEnd(id: Int){
    var pref = PrefUtils(this@VideoActivity)
    binding.homeWork.visibility = View.GONE
    binding.screenShare.visibility = View.GONE
    binding.check.setImageResource(R.drawable.is_read)
    binding.endTXT.text = "Tugatildi"
    pref.setFinishLesson(id)
}

    override fun onBackPressed() {
        super.onBackPressed()
        val returnIntent = Intent()
        returnIntent.putExtra("Til", language)  // Misol uchun
        returnIntent.putExtra("level", level)  // Misol uchun
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}
