package com.vetpet.petbeats.ui.home_user.historyBook

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
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentHistoryBookBinding
import com.vetpet.petbeats.ui.home_user.book.adapter.BookAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class HistoryBookFragment : Fragment() {
    private var _binding: FragmentHistoryBookBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapter: BookAdapter
    private val viewModel: HistoryBookViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListHistoryBook()

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

    private fun clickListHistoryBook() {
        adapter = BookAdapter { id, clinicId ->
            viewModel.itemClickBookAppointment(id, clinicId)
        }
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener {
            viewModel.bookClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.submitList(state.listBook)

                    
                    if (state.isLoading) {
                        binding.shimmerFrameLayout.startShimmer()
                        binding.shimmerFrameLayout.visibility = View.VISIBLE
                        binding.recycle.visibility = View.GONE
                        binding.tvEmpty.visibility = View.GONE
                    }
                    else {
                        binding.shimmerFrameLayout.stopShimmer()
                        binding.shimmerFrameLayout.visibility = View.GONE

                        //Kiểm tra xem list có data không
                        if (state.listBook.isEmpty()) {
                            binding.recycle.visibility = View.GONE
                            binding.tvEmpty.visibility = View.VISIBLE
                        } else {
                            binding.recycle.visibility = View.VISIBLE
                            binding.tvEmpty.visibility = View.GONE
                        }
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
                        is HistoryBookEvent.NavigationBook -> {
                            findNavController().navigate(R.id.historyBook_book)
                        }
                        is HistoryBookEvent.NavigationBookingAppointment -> {
                            findNavController().navigate(
                                R.id.confirmAppointmentFragment,
                                Bundle().apply {
                                    putInt("id", event.id)
                                    putInt("clinicId", event.clinicId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

}