package uz.fergana.it_center.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import uz.fergana.it_center.Utility
import uz.fergana.it_center.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUp.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }
        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    fun loginUser() {
        val email: String = binding.etEmail.getText().toString()
        val password: String = binding.etPassword.getText().toString()
        val isValidate = validateDate(email, password)
        if (!isValidate) {
            return
        }
        loginAccountInFirebase(email, password)
    }

    fun loginAccountInFirebase(email: String?, password: String?) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (firebaseAuth.currentUser!!.isEmailVerified) {
//                    var pref = PrefUtils(this)
//                    var student = AllStudentModel(0,email,"","","x","",0,"")
//                    pref.setStudent(student)
//                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                    finish()
                } else {
                    Utility.showToast(
                        this@LoginActivity,
                        "email not verified, Please verify your email."
                    )
                }
            } else {
                Utility.showToast(this@LoginActivity, task.exception!!.localizedMessage)
            }
        }
    }

    fun changeInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnLogin.visibility = View.VISIBLE
        }
    }

    fun validateDate(email: String?, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Email is invalid "
            return false
        }
        if (password.length < 6) {
            binding.etPassword.error = "Password length is invalid"
            return false
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        finish()
    }
}