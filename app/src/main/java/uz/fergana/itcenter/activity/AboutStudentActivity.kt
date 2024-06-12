package uz.fergana.itcenter.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import uz.fergana.itcenter.databinding.ActivityAboutStudentBinding
import uz.fergana.itcenter.model.viewmodel.MainViewModel

class AboutStudentActivity : AppCompatActivity() {
    lateinit var binding: ActivityAboutStudentBinding
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        var message = intent.getStringExtra("Student")
        binding.back.setOnClickListener {
            finish()
        }
        filter(message.toString())
        loadData()
    }


    fun loadData(){
        viewModel.getAllStudents()
    }
    fun filter(text: String){
        viewModel.studentData.observe(this, Observer {
            for (student in it){
                if (student.fullName==text){
                    binding.age.text = student.birthday
                    Glide.with(binding.userPhoto).load(student.userPhoto).into(binding.userPhoto)
                    binding.fullName.text = student.fullName
                    var txt = "${student.userPercentage}%"
                    binding.percentage.text = txt
                    binding.direction.text = student.group
                }
            }
        })
    }
}