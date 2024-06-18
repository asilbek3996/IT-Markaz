package uz.fergana.it_center.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.TouchDelegate
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import uz.fergana.it_center.R
import uz.fergana.it_center.adapter.DarsAdapter
import uz.fergana.it_center.databinding.ActivityDarslarBinding
import uz.fergana.it_center.model.DarslarModel
import uz.fergana.it_center.model.viewmodel.MainViewModel
import uz.fergana.it_center.utils.Constants
import uz.fergana.it_center.utils.PrefUtils

class DarslarActivity : AppCompatActivity() {
    lateinit var binding: ActivityDarslarBinding
    lateinit var viewModel: MainViewModel
    lateinit var adapter: DarsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDarslarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // SearchView ning EditText elementini topish
        val searchEditText = binding.searchview.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)

        // Matn rangini o'zgartirish
        searchEditText.setTextColor(Color.BLACK)
        searchEditText.setHintTextColor(Color.BLACK)
        var pref = PrefUtils(this)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val message = intent.getStringExtra("Til")
        val level = intent.getStringExtra("level")
        val group = pref.getStudent(Constants.g)
        binding.tvLanguage.text = message
        viewModel.getLessons()
        if (message == group || level == "free") {
            if (message != null && level != null) {
                loadData(message, level)
            }
            binding.back.setOnClickListener {
                finish()
            }
            binding.search.setOnClickListener {
                toggleLayoutVisibility() // toggleLayoutVisibility funksiyasini chaqirish
            }

            binding.fmExit.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.fmExit.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val parent = binding.fmExit.parent as View
                    parent.post {
                        val rect = Rect()
                        binding.fmExit.getHitRect(rect)
                        val extraPadding = 32  // 32dp ni sensorli maydonni kengaytirish uchun qo'shing
                        rect.top -= extraPadding
                        rect.left -= extraPadding
                        rect.bottom += extraPadding
                        rect.right += extraPadding
                        parent.touchDelegate = TouchDelegate(rect, binding.fmExit)
                    }
                }
            })

            binding.fmExit.setOnClickListener {
                animateButton(binding.ivExit)
                binding.linearlayout1.visibility = View.GONE
                binding.linearlayout2.visibility = View.VISIBLE
            }
            binding.ivExit.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.ivExit.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val parent = binding.ivExit.parent as View
                    parent.post {
                        val rect = Rect()
                        binding.ivExit.getHitRect(rect)
                        val extraPadding = 32
                        rect.top -= extraPadding
                        rect.left -= extraPadding
                        rect.bottom += extraPadding
                        rect.right += extraPadding
                        parent.touchDelegate = TouchDelegate(rect, binding.ivExit)
                    }
                }
            })
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
                        if (level != null && message != null) {
                            loadData(message, level)
                        }
                    } else {
                        if (level != null && message != null) {
                            filter(newText, message, level)
                        }
                    }
                    return true
                }
            })
        } else if (group==null) {
            var txt = "Siz hech qaysi guruhda o'qimaysiz"
            showAlertDialog(txt, message!!)
        } else {
            var text = "Siz $group guruhida o'qiysiz "
            showAlertDialog(text, message!!)
        }
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
    fun loadData(message: String, level: String) {
        var kotlin = arrayListOf<DarslarModel>()
        viewModel.lessonsData.observe(this) {
            for (darslar in it) {
                if (darslar.languageName == message && darslar.level == level) {
                    kotlin.add(darslar)
                }
            }
            adapter = DarsAdapter(kotlin)
            binding.recyclerDars.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.recyclerDars.adapter = adapter
        }
    }

    fun filter(message: String, languge: String, level: String) {
        var kotlin = arrayListOf<DarslarModel>()
        viewModel.lessonsData.observe(this, Observer {
            var item = arrayListOf<DarslarModel>()

            for (language in it) {
                if (language.lessonName.toLowerCase().contains(message.toLowerCase())) {
                    item.add(language)
                }
            }
            for (darslar in item) {
                if (darslar.languageName == languge && darslar.level == level) {
                    kotlin.add(darslar)
                }
            }
            adapter.filter(kotlin)
            binding.recyclerDars.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.recyclerDars.adapter = adapter
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

    private fun showAlertDialog(text: String, cource: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("$text $cource darslari sizga ochiq emas.\nAgar $cource darslari sizga qiziq bo'lsa telegram botimiz orqali uni sotib olishingiz mumkin.")
        alertDialogBuilder.setPositiveButton(
            "Sotib olish",
            DialogInterface.OnClickListener { dialog, which ->
                val link = "https://t.me/dangara_itcenterbot" // Sizning linkingizni o'zgartiring
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
                finish()
            })
        alertDialogBuilder.setNegativeButton(
            "Ortga qaytish",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                finish()
            })
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        val messageTextView = alertDialog.findViewById<TextView>(android.R.id.message)
        messageTextView?.setTextColor(Color.BLACK)

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton?.setTextColor(ContextCompat.getColor(this, R.color.greens))

        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton?.setTextColor(ContextCompat.getColor(this, R.color.greens))
        alertDialog.window?.setBackgroundDrawableResource(R.color.white)
    }

}