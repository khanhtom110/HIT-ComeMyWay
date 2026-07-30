package com.vetpet.petbeats.ui.home_user.search

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
import com.example.VetPet.R
import com.vetpet.petbeats.data.local.database.AppDatabase
import com.vetpet.petbeats.data.remote.api.ApiUserHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.remote.sharepreference.TokenManager
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.example.VetPet.databinding.FragmentSearchBinding
import com.vetpet.petbeats.ui.home_user.search.adapterhint.AdapterHint
import com.vetpet.petbeats.ui.home_user.search.adapterhistory.AdapterHistory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapterHistory: AdapterHistory
    private lateinit var adapterHint: AdapterHint
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            HomeUserRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiUserHome::class.java)
            ),

            Room.databaseBuilder(
                requireContext(),
                AppDatabase::class.java,
                "app_db"
            ).build().historyDao(),

            TokenManager(requireContext())
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
                viewModel.onHintList()
            } else {
                Toast.makeText(requireContext(), "Vui lòng bật GPS trên điện thoại", Toast.LENGTH_SHORT).show()

                viewModel.onHintList()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show()

            viewModel.onHintList()
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


        clickListHistory()
        clickListHint()


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


    //Dùng để lấy search của history sang cho màn resultSearch
    private fun clickListHistory() {
        adapterHistory = AdapterHistory { click ->
            viewModel.itemClickHistory(click)
        }
    }

    private fun clickListHint() {
        adapterHint = AdapterHint { click ->
            viewModel.itemClickHint(click)
        }
    }

    private fun setOnClick() {
        binding.back.setOnClickListener {
            viewModel.bookClick()
        }

        binding.search.addTextChangedListener {
            viewModel.onSearchChange(it.toString())
        }

        binding.buttonAll.setOnClickListener {
            viewModel.buttonAllClick()
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
                        is SearchEvent.NavigationInformationId -> {
                            findNavController().navigate(
                                R.id.informationRoomFragment,
                                Bundle().apply {
                                    putInt("id", event.id)
                                }
                            )
                        }
                        is SearchEvent.NavigationHistoryListALl -> {
                            findNavController().navigate(R.id.historyListAllFragment)
                        }
                    }
                }
            }
        }
    }
}