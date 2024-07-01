package uz.fergana.it_center.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import uz.fergana.it_center.R
import uz.fergana.it_center.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.share.setOnClickListener {
            shareApp()
        }
        binding.back.setOnClickListener {
            finish()
        }
        binding.about.setOnClickListener {
            showCustomDialogBox()
        }
    }
    private fun shareApp() {
        val appMSG: String =
            "Salom, men IT Center Developers ilovasidan foydalanmoqdaman, uni ushbu havoladan " +
                    "yuklab olishingizni tavsiya qilaman: " +
                    "https://play.google.com/store/apps/details?id=uz.fergana.it_center"
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, appMSG)
        intent.type = "text/plain"
        startActivity(intent)
    }
    private fun showCustomDialogBox() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.about_custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val exit: ImageView = dialog.findViewById(R.id.exit)
        exit.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}