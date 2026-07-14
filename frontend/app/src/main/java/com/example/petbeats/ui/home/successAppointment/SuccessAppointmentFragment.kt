package com.example.petbeats.ui.home.successAppointment

import android.graphics.Color
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
import com.example.petbeats.R
import com.example.petbeats.data.remote.api.ApiHome
import com.example.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.databinding.FragmentSuccessAppointmentBinding
import com.example.petbeats.ui.home.book.adapter.BookChildState
import kotlinx.coroutines.launch
import kotlin.getValue


class SuccessAppointmentFragment : Fragment() {
    private var _binding: FragmentSuccessAppointmentBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: SuccessAppointmentViewModel by viewModels {
        SuccessAppointmentViewModelFactory(
            HomeRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiHome::class.java)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSuccessAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clinicId = arguments?.getInt("clinicId") ?: 0
        val id = arguments?.getInt("id") ?: 0
        viewModel.onSuccessInformation(id, clinicId)

        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.btnNewBooking.setOnClickListener {
            viewModel.searchClick()
        }

        binding.btnHome.setOnClickListener {
            viewModel.bookingClick()
        }

        binding.btnDetail.setOnClickListener {
            val clinicId = arguments?.getInt("clinicId") ?: 0
            val id = arguments?.getInt("id") ?: 0

            viewModel.confirmClick(id, clinicId)
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {  state ->
                    if (binding.tvAddress.text.toString() != state.address) {
                        binding.tvAddress.text = state.address
                    }
                    if (binding.date.text.toString() != state.date) {
                        binding.date.text = state.date
                    }
                    if (binding.time.toString() != state.time) {
                        binding.time.text = state.time
                    }

                    //check state clinic
                    when (state.status) {
                        BookChildState.PENDING -> {
                            binding.stateClinic.text = "Chờ xử lý"
                            binding.stateClinic.setTextColor(Color.parseColor("#F7C120"))
                        }
                        BookChildState.SUCCESS -> {
                            binding.stateClinic.text = "Đặt lịch thành công"
                            binding.stateClinic.setTextColor(Color.parseColor("#00FF0B"))
                        }
                        BookChildState.CANCELLED -> {
                            binding.stateClinic.text = "Từ chối"
                            binding.stateClinic.setTextColor(Color.parseColor("#CC0900"))
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
                        is SuccessAppointmentEvent.NavigationSearch -> {
                            findNavController().navigate(R.id.successAppointment_search)
                        }
                        is SuccessAppointmentEvent.NavigationBooking -> {
                            findNavController().navigate(R.id.successAppointment_booking)
                        }
                        is SuccessAppointmentEvent.NavigationConfirm -> {
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