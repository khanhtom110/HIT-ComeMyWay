package com.vetpet.petbeats.ui.home_admin.add_clinic_admin

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
import com.example.VetPet.databinding.FragmentAddClinicAdminBinding
import com.example.VetPet.databinding.FragmentAddClinicSuccessAdminBinding
import com.vetpet.petbeats.ui.home_admin.add_clinic_success_admin.AddClinicSuccessAdminViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class AddClinicAdminFragment : Fragment() {
    private var _binding: FragmentAddClinicAdminBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: AddClinicAdminViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddClinicAdminBinding.inflate(inflater, container, false)
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
        binding.btnBack.setOnClickListener {
            viewModel.listAppointmentAdminClick()
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
                        is AddClinicAdminEvent -> {
                            findNavController().navigate(R.id.listAppointmentAdminFragment)
                        }
                    }
                }
            }
        }
    }
}