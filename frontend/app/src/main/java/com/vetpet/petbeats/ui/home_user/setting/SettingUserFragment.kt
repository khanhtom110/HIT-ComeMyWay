package com.vetpet.petbeats.ui.home_user.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.vetpet.petbeats.data.remote.sharepreference.TokenManager
import com.example.VetPet.databinding.FragmentSettingUserBinding
import com.vetpet.petbeats.data.remote.api.ApiUserHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.auth.activitymain.AuthActivity
import com.vetpet.petbeats.ui.home_user.successAppointment.SuccessAppointmentViewModel
import com.vetpet.petbeats.ui.home_user.successAppointment.SuccessAppointmentViewModelFactory
import kotlinx.coroutines.launch
import kotlin.getValue


class SettingUserFragment : Fragment() {
    private var _binding: FragmentSettingUserBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: SettingUserViewModel by viewModels {
        SettingUserViewModelFactory(
            HomeUserRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiUserHome::class.java)
            ),
            TokenManager(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onProfile()

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
                viewModel.state.collect {  state ->
                    binding.tvInputName.text = state.name
                    binding.tvInputPhone.text = state.phone
                    binding.tvInputAddress.text = state.address
                    binding.tvInputEmail.text = state.email


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
                    when(event) {
                        is SettingUserEvent.NavigationEditInformationSetting -> {
                            findNavController().navigate(
                                R.id.editInformationFragment,
                                Bundle().apply {
                                    putInt("id", event.id)
                                }
                            )
                        }
                        is SettingUserEvent.NavigationEditPasswordSetting -> {
                            findNavController().navigate(R.id.editPasswordFragment)
                        }
                        is SettingUserEvent.NavigationLogin -> {
                            val intent = Intent(requireContext(), AuthActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}