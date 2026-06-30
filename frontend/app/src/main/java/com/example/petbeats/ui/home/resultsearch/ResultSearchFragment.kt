package com.example.petbeats.ui.home.resultsearch

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.petbeats.R
import com.example.petbeats.data.local.database.AppDatabase
import com.example.petbeats.data.remote.api.ApiHome
import com.example.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.databinding.FragmentBookBinding
import com.example.petbeats.databinding.FragmentResultSearchBinding
import com.example.petbeats.databinding.ResultSearchChildBinding
import com.example.petbeats.ui.home.book.BookEvent
import com.example.petbeats.ui.home.book.BookViewModel
import com.example.petbeats.ui.home.book.adapter.BookAdapter
import com.example.petbeats.ui.home.resultsearch.adapter.ResultSearchAdapter
import com.example.petbeats.ui.home.search.SearchViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlin.getValue

class ResultSearchFragment : Fragment() {
    private var _binding: FragmentResultSearchBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapter: ResultSearchAdapter
    private val viewModel: ResultSearchViewModel by viewModels {
        ResultSearchViewModelFactory(
            HomeRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiHome::class.java)
            )
        )
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    @SuppressLint("MissingPermission")
    private fun getUserLocationAndSearch() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLat = location.latitude
                val userLng = location.longitude

                viewModel.onResultSearchList(userLat, userLng)
            } else {
                Toast.makeText(requireContext(), "Vui lòng bật GPS trên điện thoại", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentResultSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val search = arguments?.getString("search") ?: ""
        viewModel.onChangeSearch(search)

        getUserLocationAndSearch()



        adapter = ResultSearchAdapter()

        binding.recycle.layoutManager = LinearLayoutManager(requireContext())
        binding.recycle.adapter = adapter

        setOnClick()
        stateData()
        eventData()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.back.setOnClickListener {
            viewModel.backClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.submitList(state.listResultSearch)

                    if (binding.search.text.toString() != state.search) {
                        binding.search.setText(state.search)
                    }
                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is ResultSearchEvent.NavigationSearch -> {
                            findNavController().navigate(R.id.resultSearch_search)
                        }
                    }
                }
            }
        }
    }
}