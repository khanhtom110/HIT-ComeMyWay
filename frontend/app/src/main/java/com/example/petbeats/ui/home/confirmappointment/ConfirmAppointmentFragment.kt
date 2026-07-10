package com.example.petbeats.ui.home.confirmappointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.petbeats.R
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.petbeats.data.remote.api.ApiHome
import com.example.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.databinding.FragmentConfirmAppointmentBinding
import kotlinx.coroutines.launch
import kotlin.getValue


class ConfirmAppointmentFragment : Fragment() {
    private var _binding: FragmentConfirmAppointmentBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: ConfirmAppointmentViewModel by viewModels {
        ConfirmAppointmentViewModelFactory(
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
        _binding = FragmentConfirmAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("id") ?: 0
        viewModel.onInformationBookingAPI(id)
        viewModel.onInformationAppointment(id)

        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener {
            val id = arguments?.getInt("id") ?: 0

            viewModel.calendarClick(id)
        }

        binding.btnBooking.setOnClickListener {
            viewModel.onConfirmAppointment()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.tvInputClinic.text = state.name
                    binding.tvClinicName.text = state.name
                    binding.tvInputAddressClinic.text = state.clinicAddress
                    binding.tvInputPhoneClinic.text = state.phoneClinic
                    binding.tvInputName.text = state.fullName
                    binding.tvInputPhoneHome.text = state.phoneUser
                    binding.tvInputAddressHome.text = state.homeAddress
                    binding.tvInputQuantityHome.text = state.petQuantity.toString()
                    binding.tvInputTypePetHome.text = state.petType
                    binding.tvInputStatusHome.text = state.petCondition
                    binding.tvInputTypeServiceHome.text = state.bookingType
                    binding.tvInputDayHome.text = state.appointmentDate
                    binding.tvInputTimeHome.text = state.appointmentTime
                    binding.tvRating.text = "đánh giá ${state.rating}/5"

                    if (state.status) {
                        binding.tvStatus.text = "Đang hoạt động"
                    } else {
                        binding.tvStatus.text = "Không hoạt động"
                    }

                    if (state.thumbnailUrl.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.thumbnailUrl)
                            .into(binding.imgClinic)
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
                        is ConfirmAppointmentEvent.NavigationCalendar -> {
                            findNavController().navigate(
                                R.id.confirmAppointment_calendar,
                                Bundle().apply {
                                    putInt("id", event.id)
                                }
                            )
                        }
                        is ConfirmAppointmentEvent.NavigationSuccessAppointment -> {
                            findNavController().navigate(R.id.successAppointFragment)
                        }
                    }
                }
            }
        }
    }
}