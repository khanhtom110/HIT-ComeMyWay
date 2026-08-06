package com.vetpet.petbeats.ui.home_clinic.clinic

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentClinicBinding
import com.google.android.material.chip.Chip
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import com.vetpet.petbeats.ui.auth.activitymain.AuthActivity
import com.vetpet.petbeats.ui.home_clinic.schedulelist.ScheduleListViewModel
import com.vetpet.petbeats.ui.home_clinic.schedulelist.ScheduleListViewModelFactory
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

        viewModel.onInformation()

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

    private fun addChipToGroup(state: ClinicState, serviceName: String, isChecked: Boolean = false) {
        //nền thay đổi theo click
        val groundColor = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked), // đang chọn
                intArrayOf(-android.R.attr.state_checked) //bình thường
            ),
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary), //đã click
                ContextCompat.getColor(requireContext(), R.color.colorModes) //chưa click
            )
        )

        //màu text thay đổi theo click
        val textColors = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.colorBackground), //đã click
                ContextCompat.getColor(requireContext(), R.color.colorPrimary) //chưa click
            )
        )

        //màu viền thay đổi theo click
        val strokeColorState = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary), //đã click
                ContextCompat.getColor(requireContext(), R.color.colorModes)  //chưa click
            )
        )


        if (binding.service.isEmpty() && state.services.isNotEmpty()) {
            state.services.forEach { serviceName ->
                val chip = Chip(requireContext()).apply {
                    text = serviceName

                    //cho phép bấm chọn
                    isClickable = false
                    isCheckable = false
                    chipStrokeWidth = 3f

                    // Chỉ việc gọi lại biến đã tạo ở trên, không khởi tạo lại
                    chipBackgroundColor = groundColor
                    setTextColor(textColors)
                    chipStrokeColor = strokeColorState

                }
                binding.service.addView(chip)
            }
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
                    binding.tvInputTime.text = "${state.openTime} - ${state.closeTime}"
                    binding.tvInputState.text = state.state

                    if (state.image.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.image)
                            .into(binding.imgLibrary)
                    }

                    binding.service.removeAllViews()
                    state.services.forEach { serviceName ->
                        addChipToGroup(state, serviceName, isChecked = true)
                    }
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