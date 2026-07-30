package com.vetpet.petbeats.ui.home_clinic.clinic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentClinicBinding
import com.example.VetPet.databinding.FragmentScheduleListBinding
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.remote.sharepreference.TokenManager
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import com.vetpet.petbeats.ui.home_clinic.schedulelist.ScheduleListViewModel
import com.vetpet.petbeats.ui.home_clinic.schedulelist.ScheduleListViewModelFactory
import com.vetpet.petbeats.ui.home_user.chatbot.ChatbotEvent
import kotlinx.coroutines.launch
import kotlin.getValue


class ClinicFragment : Fragment() {
    private var _binding: FragmentClinicBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: ClinicViewModel by viewModels {
        ClinicViewModelFactory(
            HomeClinicRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiClinicHome::class.java)
            ),
            TokenManager(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentClinicBinding.inflate(inflater, container, false)
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
        binding.btnEdit.setOnClickListener {
            viewModel.editInformationClick()
        }
        binding.btnPassword.setOnClickListener {
            viewModel.editPasswordClick()
        }
        binding.btnLogOut.setOnClickListener {
            viewModel.onLogoutClick()
        }

    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    //Check input
                    binding.tvInputName.text = state.name
                    binding.tvInputPhone.text = state.phone
                    binding.tvInputAddress.text = state.address
                    binding.tvInputLink.text = state.link
                    binding.tvInputTime.text = state.time
                    binding.tvInputState.text = state.state

                    if (state.image.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.image)
                            .into(binding.imgLibrary)
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
                        is ClinicEvent.NavigationEditInformation -> {
                            findNavController().navigate(R.id.editInformationClinicFragment)
                        }
                        is ClinicEvent.NavigationEditPassword -> {
                            findNavController().navigate(R.id.editPasswordClinicFragment)
                        }
                        is ClinicEvent.NavigationLogin -> {
                            findNavController().navigate(R.id.loginFragment)
                        }
                    }
                }
            }
        }
    }

}