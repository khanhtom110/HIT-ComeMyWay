package com.example.petbeats.ui.home.resultsearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petbeats.R
import com.example.petbeats.databinding.FragmentBookBinding
import com.example.petbeats.databinding.FragmentResultSearchBinding
import com.example.petbeats.databinding.ResultSearchChildBinding
import com.example.petbeats.ui.home.book.BookEvent
import com.example.petbeats.ui.home.book.BookViewModel
import com.example.petbeats.ui.home.book.adapter.BookAdapter
import com.example.petbeats.ui.home.resultsearch.adapter.ResultSearchAdapter
import kotlinx.coroutines.launch
import kotlin.getValue

class ResultSearchFragment : Fragment() {
    private var _binding: FragmentResultSearchBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapter: ResultSearchAdapter
    private val viewModel: ResultSearchViewModel by viewModels()

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

        setOnClick()

        adapter = ResultSearchAdapter()

        binding.recycle.layoutManager = LinearLayoutManager(requireContext())
        binding.recycle.adapter = adapter

        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.search.setOnClickListener {
            viewModel.searchClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.submitList(state.list)
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