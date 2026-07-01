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
import com.example.petbeats.R
import com.example.petbeats.data.remote.api.ApiHome
import com.example.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.databinding.FragmentResultSearchBinding
import com.example.petbeats.ui.home.resultsearch.adapter.ResultSearchAdapter
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

                viewModel.onLatiLong(userLat, userLng)

                //Check search của màn trước
                viewModel.onResultSearchList()

            } else {
                Toast.makeText(requireContext(), "Vui lòng bật GPS trên điện thoại", Toast.LENGTH_SHORT).show()

                viewModel.onResultSearchList()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show()

            viewModel.onResultSearchList()
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


        val search = arguments?.getString("search") ?: ""
        viewModel.onChangeSearch(search)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getUserLocationAndSearch()

        clickList()

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

    private fun clickList() {
        adapter = ResultSearchAdapter { click ->
            viewModel.itemClick(click)
        }
    }

    private fun setOnClick() {
        binding.back.setOnClickListener {
            viewModel.backClick()
        }

        binding.search.addTextChangedListener { text ->
            viewModel.onChangeSearch(text.toString())
        }

        //Tự động check true khi click, flase thì thoát
        binding.search.setOnFocusChangeListener { _, search ->
            viewModel.onCheck(search)
        }

        //Xử lý khi bấm kính lúp
        binding.buttonSearch.setOnClickListener {
            viewModel.onResultSearchList()
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


                    if (state.isSearch) {
                        binding.search.setBackgroundResource(R.drawable.tittle_search_blue)
                    }
                    else {
                        binding.search.setBackgroundResource(R.drawable.tittle_search)
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

                        is ResultSearchEvent.NavigationInformation -> {
                            findNavController().navigate(
                                R.id.informationRoomFragment,
                                Bundle().apply {
                                    putInt("id", event.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}