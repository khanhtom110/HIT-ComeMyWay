package com.vetpet.petbeats.ui.home_clinic.appointmentschedule

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
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentAppointmentScheduleBinding
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import kotlinx.coroutines.launch
import kotlin.getValue


class AppointmentScheduleFragment : Fragment() {
    private var _binding: FragmentAppointmentScheduleBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: AppointmentScheduleViewModel by viewModels {
        AppointmentScheduleViewModelFactory(
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
        _binding = FragmentAppointmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.buttonAll.setOnClickListener {
            viewModel.scheduleToday()
        }
        binding.buttonListAll.setOnClickListener {
            viewModel.scheduleList()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->

                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is AppointmentScheduleEvent.NavigationScheduleToday -> {
                            findNavController().navigate(R.id.scheduleTodayFragment)
                        }
                        is AppointmentScheduleEvent.NavigationScheduleList -> {
                            findNavController().navigate(R.id.scheduleListFragment)
                        }
                    }
                }
            }
        }
    }

}