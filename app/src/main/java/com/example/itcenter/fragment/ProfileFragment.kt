package com.example.itcenter.fragment

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.itcenter.R
import com.example.itcenter.ShowProgress
import com.example.itcenter.activity.CheckActivity
import com.example.itcenter.activity.RegisterActivity
import com.example.itcenter.activity.SettingsActivity
import com.example.itcenter.databinding.FragmentProfileBinding
import com.example.itcenter.model.AllStudentModel
import com.example.itcenter.model.viewmodel.MainViewModel
import com.example.itcenter.utils.Constants
import com.example.itcenter.utils.PrefUtils
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        val pref = PrefUtils(requireContext())
        binding.settings.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingsActivity::class.java))
        }

        binding.rating.setOnClickListener {
            val uri: Uri = Uri.parse("market://details?id=com.google.android.youtube")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)

            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.youtube")
                    )
                )
            }
        }

        binding.support.setOnClickListener {
            val link = "https://t.me/ITCenter_support_bot" // Sizning linkingizni o'zgartiring
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
            requireActivity().finish()
        }

        binding.logOut.setOnClickListener {
            showAlertDialog()
        }
        viewModel.studentData.observe(requireActivity()) {
            if (it != null && it.isNotEmpty()) {
                if (it.isNotEmpty()) {
                    val pref = PrefUtils(requireContext())
                    var idRaqami = pref.getID()
                    val requestOptions = RequestOptions()
                        .placeholder(R.drawable.user) // Standart rasm
                        .error(R.drawable.user)
                    for (items in it) {
                        Glide.with(binding.img).load(items.userPhoto).apply(requestOptions)
                            .into(binding.img)
                        binding.tvFullName.text = items.fullName
                        binding.tvID.text = idRaqami.toString()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Sizning ID raqamingiz serverda topilmadi.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    val pref = PrefUtils(requireContext())
                    pref.clear()
                    startActivity(Intent(requireActivity(), CheckActivity::class.java))
                    requireActivity().finish()
                }
            } else {
                loadData()
            }
        }
    }

    fun loadData() {
        viewModel.getAllStudents()
    }
    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setMessage("Hisobingizdan chiqmoqchimisiz")
        alertDialogBuilder.setPositiveButton("Ha", DialogInterface.OnClickListener { dialog, which ->
            val pref = PrefUtils(requireContext())
            pref.clear()
            startActivity(Intent(requireActivity(), CheckActivity::class.java))
            requireActivity().finish()
            dialog.dismiss()
        })
        alertDialogBuilder.setNegativeButton("Yo'q", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}