package com.example.itcenter.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.itcenter.R
import com.example.itcenter.activity.QuizSplashActivity
import com.example.itcenter.databinding.FragmentQuizBinding
import com.example.itcenter.model.viewmodel.MainViewModel

class QuizFragment : Fragment() {
    lateinit var binding: FragmentQuizBinding
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.play.setOnClickListener {
            startActivity(Intent(requireActivity(),QuizSplashActivity::class.java))
        }
        binding.lottieAnimationView.setAnimation(R.raw.quizz)
        binding.lottieAnimationView.playAnimation()
    }
    companion object {
        @JvmStatic
        fun newInstance() = QuizFragment()
    }
}