package com.example.petbeats.ui.home.book

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
import com.example.petbeats.ui.home.book.adapter.BookAdapter
import kotlinx.coroutines.launch


class BookFragment : Fragment() {
    private var _binding: FragmentBookBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapter: BookAdapter
    private val viewModel: BookViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClick()

        adapter = BookAdapter()

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
                        is BookEvent.NavigationSearch -> {
                            findNavController().navigate(R.id.searchFragment)
                        }
                    }
                }
            }
        }
    }
}