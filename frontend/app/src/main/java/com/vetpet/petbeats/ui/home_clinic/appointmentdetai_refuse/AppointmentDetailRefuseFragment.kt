package com.vetpet.petbeats.ui.home_clinic.appointmentdetai_refuse

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
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentAppointmentDetailRefuseBinding
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import kotlinx.coroutines.launch
import kotlin.getValue


class AppointmentDetailRefuseFragment : Fragment() {
    private var _binding: FragmentAppointmentDetailRefuseBinding?= null
    private val binding get() = _binding!!
    private val viewModel: AppointmentDetailRefuseViewModel by viewModels {
        AppointmentDetailRefuseViewModelFactory(
            HomeClinicRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiClinicHome::class.java)
            )
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAppointmentDetailRefuseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("id") ?: 0
        viewModel.onAppointmentDetailRefuse(id)

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
            viewModel.appointmentScheduleClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.numbDay.text = state.day
                    binding.numbTime.text = state.time
                    binding.numbQuantity.text = state.quantity.toString()
                    binding.numbService.text = state.bookingType
                    binding.numbOwner.text = state.fullName
                    binding.numbPhone.text = state.phone
                    binding.numbAddress.text = state.address
                    binding.numbState.text = state.state
                    binding.tvInputReason.text = state.reason


                    binding.numbInclude.text = if (state.services.isEmpty()) {
                        "Không có dịch vụ"
                    }
                    else {
                        state.services.joinToString(separator = ", ") { serviceItem ->
                            serviceItem.name
                        }
                    }


                    if (state.imgPet.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.imgPet)
                            .into(binding.imgPet)
                    }
                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when(event) {
                        is AppointmentDetailRefuseEvent.NavigationAppointmentSchedule -> {
                            findNavController().navigate(R.id.refuse_appointmentSchedule)
                        }
                    }
                }
            }
        }
    }

}