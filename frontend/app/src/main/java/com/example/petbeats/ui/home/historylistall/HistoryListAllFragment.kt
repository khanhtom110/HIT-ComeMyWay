package com.example.petbeats.ui.home.historylistall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.petbeats.data.remote.sharepreference.TokenManager
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.databinding.FragmentHistoryListAllBinding
import com.example.petbeats.ui.home.search.SearchEvent
import com.example.petbeats.ui.home.search.adapterhistory.AdapterHistory
import kotlinx.coroutines.launch


class HistoryListAllFragment : Fragment() {
    private var _binding: FragmentHistoryListAllBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapterHistory: AdapterHistory
    private val viewModel: HistoryListAllViewModel by viewModels {
        HistoryListAllVIewModelFactory(
            HomeRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiHome::class.java)
            ),

            Room.databaseBuilder(
                requireContext(),
                AppDatabase::class.java,
                "app_db"
            ).build().historyDao(),

            TokenManager(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryListAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListHistory()

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

    private fun clickListHistory() {
        adapterHistory = AdapterHistory { click ->
            viewModel.itemClickHistory(click)
        }
    }

    private fun setOnClick() {
        binding.back.setOnClickListener {
            viewModel.searchClick()
        }

        binding.search.addTextChangedListener {
            viewModel.onSearchChange(it.toString())
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
                        is HistoryListAllEvent.NavigationSearch -> {
                            findNavController().navigate(R.id.historyListAll_Search)
                        }

                        is HistoryListAllEvent.NavigationResultSearch -> {
                            findNavController().navigate(
                                R.id.resultSearchFragment,
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