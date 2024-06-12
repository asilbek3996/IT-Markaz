package uz.fergana.itcenter.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import uz.fergana.itcenter.R
import uz.fergana.itcenter.adapter.QuestionAdapter
import uz.fergana.itcenter.adapter.score
import uz.fergana.itcenter.databinding.ActivityQuizBinding
import uz.fergana.itcenter.model.QuestionModel
import uz.fergana.itcenter.model.viewmodel.MainViewModel
import uz.fergana.itcenter.utils.Constants
import uz.fergana.itcenter.utils.PrefUtils

class QuizActivity : AppCompatActivity(), score {
    private lateinit var binding: ActivityQuizBinding
    private var position: Int = 0
    private var receivedList: MutableList<QuestionModel> = mutableListOf()
    private var correctAnswersCount = 0
    private var wrongAnswersCount = 0
    private var score = 0.0
    var level = "1"
    private var javob = "0"
    private var lan = ""
    private lateinit var timer: CountDownTimer
    lateinit var viewModel: MainViewModel

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        level = intent.getStringExtra("level").toString()
        var language = intent.getStringExtra("language")
        if (language != null) {
            lan(language,true)
        }
        binding.framelayout.visibility = android.view.View.VISIBLE
        binding.framelayout.startAnimation(fadeInAnimation)
        binding.questionList.visibility = android.view.View.VISIBLE
        binding.questionList.startAnimation(fadeInAnimation)

        val window: Window = this@QuizActivity.window
        window.statusBarColor = ContextCompat.getColor(this@QuizActivity, R.color.grey)
        viewModel.questionData.observe(this) {
            for (i in it) {
                var l = i.language?.trimEnd()
                if (i.level == level && l == language) {
                    receivedList.add(i)
                }
            }
            if (receivedList.isNotEmpty()) {
            val count = receivedList.size
                val pref = PrefUtils(this)
                val name = pref.getStudent(Constants.fullName)
                val txt = "Salom $name"
                binding.tvMain.text = txt

                binding.back.setOnClickListener {
                    val intent = Intent(this, QuizLevelActivity::class.java)
                    intent.putExtra("language",language)
                    startActivity(intent)
                    finish()
                }

                binding.apply {
                    questionNumberTxt.text = "Savollar 1/$count"
                    progressBar.progress = 1

                    questionTxt.text = receivedList[position].question

                    loadAnswers()
                    progressBar.max = count

                    rightArrow.setOnClickListener {
                        javob = "0"
                        if (progressBar.progress == count) {
                            if (language != null) {
                                navigateToScoreActivity(level, language)
                            }
                            return@setOnClickListener
                        }
                        if (language != null) {
                            moveToNextQuestion(language)
                        }
                    }
                }

                // Start the timer for the first question
                resetAndStartTimer()
            }else{
                if (language != null) {
                    showAlertDialog(language,level)
                }
            }
        }

        viewModel.getQuestions()
    }
    private fun showAlertDialog(language: String,level:String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("$language ${level}-bosqichda hali testlar mavjud emas keyinroq urinib ko'ring")
        alertDialogBuilder.setNegativeButton("Ortga qaytish", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
            startActivity(Intent(this,QuizLevelActivity::class.java))
            finish()
        })
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    private fun moveToNextQuestion(language: String) {
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        if (position < receivedList.size - 1) {
            position++
            binding.progressBar.progress += 1
            binding.questionNumberTxt.text = "Savollar ${binding.progressBar.progress}/${receivedList.size}"
            binding.questionTxt.text = receivedList[position].question
            binding.framelayout.startAnimation(fadeInAnimation) // Animatsiya framelayout uchun
            binding.questionList.startAnimation(fadeInAnimation) // Animatsiya questionList uchun
            loadAnswers()
            resetAndStartTimer()  // Reset and start the timer for the new question
        } else {
            navigateToScoreActivity(level,language)
        }
    }

    private fun loadAnswers() {
        val users: MutableList<String> = mutableListOf()
        users.add(receivedList[position].a.toString())
        users.add(receivedList[position].b.toString())
        users.add(receivedList[position].c.toString())
        users.add(receivedList[position].d.toString())

        if (receivedList[position].clickedAnswer != null) {
            receivedList[position].clickedAnswer = ""
        }
        var score = 100.0/receivedList.size
        val questionAdapter = QuestionAdapter(
            receivedList[position].right.toString(), users, score, this
        )

        questionAdapter.differ.submitList(users)
        binding.questionList.apply {
            layoutManager = LinearLayoutManager(this@QuizActivity)
            adapter = questionAdapter
        }
    }

    private fun navigateToScoreActivity(level: String,language: String) {
        timer.cancel()
        Toast.makeText(this, "$language", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@QuizActivity, ScoreActivity::class.java).apply {
            putExtra("right", correctAnswersCount)
            putExtra("wrong", wrongAnswersCount)
            putExtra("Score", score)
            putExtra("level", level)
            putExtra("language", language)
        }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        timer.cancel()
        val intent = Intent(this, QuizLevelActivity::class.java)
        var l = lan("0",false)
        intent.putExtra("language",l)
        startActivity(intent)
        finish()
    }

    private fun resetAndStartTimer() {
        if (::timer.isInitialized) {
            timer.cancel()  // Cancel the previous timer if it exists
        }
        timer = object : CountDownTimer(11000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timer.text = (millisUntilFinished / 1000).toString()
            }
            override fun onFinish() {
                if (javob == "0"){
                    timeOut()
                }else {
                    javob = "0"
                    var l = lan("0",false)
                    moveToNextQuestion(l)  // Move to the next question when the timer finishes
                }
            }
        }
        timer.start()  // Start the new timer
    }
    fun timeOut(){
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Sizning vaqtingiz tugadi testlarni qaytadan yeching yoki orqga qayting")
            alertDialogBuilder.setPositiveButton("Qayta boshlash", DialogInterface.OnClickListener { dialog, which ->
                clear()
                timer.start()
                dialog.dismiss()
            })
            alertDialogBuilder.setNegativeButton("Ortga qaytish", DialogInterface.OnClickListener { dialog, which ->
                timer.cancel()  // Cancel the timer when back button is pressed
                val intent = Intent(this, QuizLevelActivity::class.java)
                var l = lan("0",false)
                intent.putExtra("language",l)
                startActivity(intent)
                finish()
                dialog.dismiss()
            })
            alertDialogBuilder.setCancelable(false)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
    }
    fun clear(){
        binding.progressBar.progress = 0
        correctAnswersCount = 0
        wrongAnswersCount = 0
        position = -1
        var l = lan("0",false)
        moveToNextQuestion(l)
        loadAnswers()
    }
    override fun amount(number: Double, clickedAnswer: String, rightAnswer: Int, wrongAnswer: Int) {
            correctAnswersCount +=rightAnswer
            wrongAnswersCount += wrongAnswer
            score += number
            receivedList[position].clickedAnswer = clickedAnswer
        if (rightAnswer == 1){
            javob = "ok"
        }else{
            javob = "no"
        }
    }
    fun lan(language: String,c:Boolean):String{
        if (c){
            lan = language
        }
        return lan
    }
}