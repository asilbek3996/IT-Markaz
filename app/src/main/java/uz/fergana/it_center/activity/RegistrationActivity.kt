package uz.fergana.it_center.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uz.fergana.it_center.R
import uz.fergana.it_center.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val message = intent.getStringExtra("Til")
        binding.btnLogin.setOnClickListener {
            var phone = binding.etPhone.text.toString()
            var text = "telefon raqami: \n$phone"
            sendMessageToTelegramBot(phone)
        }
    }
    fun sendMessageToTelegramBot(message: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://t.me/ITCenter_support_bot?start=$message")
        try {
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Telegram ilovasi o'rnatilmagan", Toast.LENGTH_SHORT).show()
        }
    }
}