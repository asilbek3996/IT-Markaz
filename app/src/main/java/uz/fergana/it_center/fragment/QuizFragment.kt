package uz.fergana.it_center.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import uz.fergana.it_center.R
import uz.fergana.it_center.ShowProgress
import uz.fergana.it_center.activity.QuizSplashActivity
import uz.fergana.it_center.databinding.FragmentQuizBinding
import uz.fergana.it_center.model.viewmodel.MainViewModel
import uz.fergana.it_center.utils.PrefUtils

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
        var pref = PrefUtils(requireContext())
        var idRaqami =pref.getID()
        binding.play.setOnClickListener {
            if (idRaqami==0){
                showAlertDialog()
            }else {
                startActivity(Intent(requireActivity(), QuizSplashActivity::class.java))
            }
        }
        binding.lottieAnimationView.setAnimation(R.raw.quizz)
        binding.lottieAnimationView.playAnimation()
    }
    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Siz ro'yhatdan o'tmagansiz avval ro'yhatdan o'ting keyin savollarni ko'rishingiz mumkin")
        alertDialogBuilder.setPositiveButton(
            "Ro'yhatdan o'tish",
            DialogInterface.OnClickListener { dialog, which ->
                (activity as? ShowProgress.View)?.login()
                dialog.dismiss()
            })
        alertDialogBuilder.setNegativeButton(
            "Bekor qilishi",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        val messageTextView = alertDialog.findViewById<TextView>(android.R.id.message)
        messageTextView?.setTextColor(Color.BLACK)

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton?.setTextColor(ContextCompat.getColor(requireContext(), R.color.greens))

        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton?.setTextColor(ContextCompat.getColor(requireContext(), R.color.greens))
        alertDialog.window?.setBackgroundDrawableResource(R.color.white)
    }

    companion object {
        @JvmStatic
        fun newInstance() = QuizFragment()
    }
}