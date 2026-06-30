package com.example.petbeats.ui.home.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import com.example.petbeats.data.remote.model.calendar.home.request.LocationRequest
import com.example.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.databinding.FragmentBookBinding
import com.example.petbeats.databinding.FragmentSearchBinding
import com.example.petbeats.ui.home.search.adapterhint.AdapterHint
import com.example.petbeats.ui.home.search.adapterhistory.AdapterHistory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.internal.ViewUtils.hideKeyboard
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapterHistory: AdapterHistory
    private lateinit var adapterHint: AdapterHint
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            HomeRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiHome::class.java)
            ),

            Room.databaseBuilder(
                requireContext(),
                AppDatabase::class.java,
                "app_db"
            ).build().historyDao()
        )
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    private fun getUserLocationAndSearch() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLat = location.latitude
                val userLng = location.longitude

                viewModel.onHintList(userLat, userLng)
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
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getUserLocationAndSearch()


        adapterHint = AdapterHint()
        adapterHistory = AdapterHistory()


        binding.recycleHint.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleHint.adapter = adapterHint
        binding.recycleHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleHistory.adapter = adapterHistory

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
            viewModel.bookClick()
        }

        binding.search.addTextChangedListener {
            viewModel.onSearchChange(it.toString())
            viewModel.onHintSearch()
        }

        binding.buttonAll.setOnClickListener {
            viewModel.checkButtonAll()
        }

        //Tự động check true khi click, flase thì thoát
        binding.search.setOnFocusChangeListener { _, search ->
            viewModel.onCheck(search)
        }

        //Xử lý khi bấm kính lúp
        binding.buttonSearch.setOnClickListener {
            viewModel.insertHistory()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    //list hint
                    adapterHint.submitList(state.listHint)

                    //list history
                    adapterHistory.submitList(state.listHistory)

                    if (state.isSearch) {
                        binding.search.setBackgroundResource(R.drawable.tittle_search_blue)
                    }
                    else {
                        binding.search.setBackgroundResource(R.drawable.tittle_search)
                    }


                    //check search
                    if (binding.search.text.toString() != state.search) {
                        binding.search.setText(state.search)
                    }

                    //check button all
                    if (state.isButtonAll) {
                        binding.lineHint.visibility = View.GONE
                        binding.hint.visibility = View.GONE
                        binding.lineHint1.visibility = View.GONE
                        binding.recycleHint.visibility = View.GONE
                    }
                    else {
                        binding.lineHint.visibility = View.VISIBLE
                        binding.hint.visibility = View.VISIBLE
                        binding.lineHint1.visibility = View.VISIBLE
                        binding.recycleHint.visibility = View.VISIBLE
                    }

                    //Check search room
                    if (state.search.isEmpty()) {
                        binding.text.visibility = View.VISIBLE
                        binding.buttonAll.visibility = View.VISIBLE
                        binding.line.visibility = View.VISIBLE
                        binding.recycleHistory.visibility = View.VISIBLE
                        binding.hint.visibility = View.VISIBLE
                        binding.lineHint.visibility = View.VISIBLE
                        binding.lineHint1.visibility = View.VISIBLE
                        binding.tvEmptyHistory.visibility = View.VISIBLE

                        adapterHint.submitList(state.listHint)
                    }
                    else {
                        binding.text.visibility = View.GONE
                        binding.buttonAll.visibility = View.GONE
                        binding.line.visibility = View.GONE
                        binding.recycleHistory.visibility = View.GONE
                        binding.hint.visibility = View.GONE
                        binding.lineHint.visibility = View.GONE
                        binding.lineHint1.visibility = View.GONE
                        binding.tvEmptyHistory.visibility = View.GONE

                        adapterHint.submitList(state.listSearch)
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
                        is SearchEvent.NavigationBook -> {
                            findNavController().navigate(R.id.search_book)
                        }
                        is SearchEvent.NavigationResultSearch -> {
                            findNavController().navigate(
                                R.id.search_resultSearch,
                                Bundle().apply {
                                    putString("search", event.search)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}