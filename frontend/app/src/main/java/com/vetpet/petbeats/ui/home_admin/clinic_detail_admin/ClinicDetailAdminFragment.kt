package com.vetpet.petbeats.ui.home_admin.clinic_detail_admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentClinicDetailAdminBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class ClinicDetailAdminFragment : Fragment() {
    private var _binding: FragmentClinicDetailAdminBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: ClinicDetailAdminViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentClinicDetailAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clinicId = arguments?.getInt("clinicId") ?: 0
        viewModel.onInformationList(clinicId)

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
            viewModel.listAppointmentClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.nameClinic.text = state.nameClinic
                    binding.textPhone.text = state.phone
                    binding.textAddress.text = state.address
                    binding.textMap.text = state.map
                    binding.tvDescription.text = state.description
                    binding.openTime.text = state.openTime.take(5)
                    binding.closeTime.text = state.closeTime.take(5)


                    if (state.imgClinic.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.imgClinic)
                            .into(binding.imgClinic)
                    }

                    //Service
                    binding.service.removeAllViews()
                    state.services.forEach { serviceName ->
                        val chip = com.google.android.material.chip.Chip(requireContext()).apply {
                            text = serviceName
                            isClickable = false
                            isCheckable = false

                            chipStrokeWidth = 0f
                            setChipBackgroundColorResource(R.color.colorModes)
                            setTextColor("#486BF3".toColorInt())
                        }
                        binding.service.addView(chip)
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
                        is ClinicDetailAdminEvent.NavigationListAppointment -> {
                            findNavController().navigate(R.id.clinicDetail_listAppointment)
                        }
                    }
                }
            }
        }
    }


}