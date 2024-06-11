package com.example.itcenter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itcenter.R
import com.example.itcenter.ShowProgress
import com.example.itcenter.adapter.ItemClickedListener
import com.example.itcenter.utils.PrefUtils
import com.example.itcenter.adapter.SaveAdapter
import com.example.itcenter.databinding.FragmentFavoriteBinding
import com.example.itcenter.model.DarslarModel
import com.example.itcenter.model.viewmodel.MainViewModel
import com.example.itcenter.utils.Constants

class FavoriteFragment : Fragment(), ItemClickedListener {
    lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: SaveAdapter
    var items= arrayListOf<DarslarModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateData()
    }
    fun updateData(){
        val pref = PrefUtils(requireContext())
        if (pref.getFavorite(Constants.favorite)?.isNotEmpty() == true) {
            items = pref.getFavorite(Constants.favorite)!!
        }else{
            pref.clearFavorite()
            items = pref.getFavorite(Constants.favorite)!!
        }
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        adapter = SaveAdapter(items,this,this)
        binding.favoriteRecyclerView.adapter = adapter
    }
    override fun onItemClicked(position: DarslarModel) {
        items.clear()
        val pref = PrefUtils(requireContext())
        if (pref.getFavorite(Constants.favorite)?.isNotEmpty() == true) {
            items = pref.getFavorite(Constants.favorite)!!
        }
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        adapter = SaveAdapter(items,this,this)
        binding.favoriteRecyclerView.adapter = adapter
    }
    companion object {
        @JvmStatic
        fun newInstance() = FavoriteFragment()
    }


}