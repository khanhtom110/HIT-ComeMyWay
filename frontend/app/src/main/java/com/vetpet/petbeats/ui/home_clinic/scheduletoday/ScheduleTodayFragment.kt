package com.vetpet.petbeats.ui.home_clinic.scheduletoday

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentAppointmentScheduleBinding
import com.example.VetPet.databinding.FragmentScheduleListBinding
import com.example.VetPet.databinding.FragmentScheduleTodayBinding
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.AppointmentScheduleViewModel
import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.AppointmentScheduleViewModelFactory
import com.vetpet.petbeats.ui.home_clinic.schedulelist.ScheduleListViewModel
import com.vetpet.petbeats.ui.home_clinic.schedulelist.ScheduleListViewModelFactory
import kotlinx.coroutines.launch
import kotlin.getValue


class ScheduleTodayFragment : Fragment() {
    private var _binding: FragmentScheduleTodayBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: ScheduleTodayViewModel by viewModels {
        ScheduleTodayViewModelFactory(
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
        _binding = FragmentScheduleTodayBinding.inflate(inflater, container, false)
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

                }
            }
        }
    }

}