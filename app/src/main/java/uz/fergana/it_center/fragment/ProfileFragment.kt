package uz.fergana.it_center.fragment

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import uz.fergana.it_center.R
import uz.fergana.it_center.ShowProgress
import uz.fergana.it_center.activity.SettingsActivity
import uz.fergana.it_center.databinding.FragmentProfileBinding
import uz.fergana.it_center.model.viewmodel.MainViewModel
import uz.fergana.it_center.utils.PrefUtils

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

        binding.btnLogin.setOnClickListener {
            loginUser()
            viewModel.getAllStudent()
        }
        viewModel.studentData.observe(requireActivity()) {
                    val pref = PrefUtils(requireContext())
                    var idRaqami = pref.getID()
                    val requestOptions = RequestOptions()
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                    var user = it.filter { it.id == idRaqami }
                    if (idRaqami!=0){
                        if (user.isEmpty()) {
                            binding.logInPage.visibility = View.VISIBLE
                            binding.profile.visibility = View.GONE
                        }else{
                            for (items in it) {
                                if (items.id==idRaqami) {
                                    binding.profile.visibility = View.VISIBLE
                                    binding.logInPage.visibility = View.GONE

                                    Glide.with(binding.img).load(items.userPhoto)
                                        .apply(requestOptions)
                                        .into(binding.img)
                                    binding.tvFullName.text = items.fullName
                                    binding.tvID.text = idRaqami.toString()
                                    break
                                }else{
                                    binding.logInPage.visibility = View.VISIBLE
                                    binding.profile.visibility = View.GONE
                                    continue
                                }
                            }
                        }
                    }else{
                        binding.logInPage.visibility = View.VISIBLE
                        binding.profile.visibility = View.GONE
                    }
        }
    }

    fun loadData() {
        viewModel.getAllStudents()
    }


    fun loginUser() {
        val email: String = binding.etEmail.text.toString()
        if (email=="") {
            binding.etEmail.error = "ID is invalid "
        }else {
            changeInProgress(true)
            Handler().postDelayed({
                login(email)
            }, 1500)
        }
    }
    fun login(email: String?){
        changeInProgress(true)
        (activity as? ShowProgress.View)?.again()
        viewModel.userData.observe(requireActivity(), Observer {
            for (student in it){
                if (student.id.toString() == email){
                    changeInProgress(false)
                    val pref = PrefUtils(requireActivity())
                    validateDate(email)
                    pref.setStudent(student)
                    validateDate("0")
                    loadData()
                    binding.profile.visibility = View.VISIBLE
                    binding.logInPage.visibility = View.GONE
                    binding.etEmail.text.clear()
                }else{
                    validateDate(null)
                    changeInProgress(false)
                }
            }
        })
    }
    fun validateDate(email: String?): Boolean {
        if (email==null) {
            binding.etEmail.error = "ID raqam xato"
            return false
        }
        return true
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
    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setMessage("Hisobingizdan chiqmoqchimisiz")
        alertDialogBuilder.setPositiveButton("Ha", DialogInterface.OnClickListener { dialog, which ->
            val pref = PrefUtils(requireContext())
            pref.clear()
            binding.profile.visibility = View.GONE
            binding.logInPage.visibility = View.VISIBLE
            (activity as? ShowProgress.View)?.again()
            loadData()
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