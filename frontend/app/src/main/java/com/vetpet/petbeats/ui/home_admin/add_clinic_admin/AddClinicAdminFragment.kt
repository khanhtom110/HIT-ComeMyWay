package com.vetpet.petbeats.ui.home_admin.add_clinic_admin

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentAddClinicAdminBinding
import com.example.VetPet.databinding.FragmentAddClinicSuccessAdminBinding
import com.vetpet.petbeats.core.utils.AnimationUtils.fadeIn
import com.vetpet.petbeats.core.utils.AnimationUtils.fadeOut
import com.vetpet.petbeats.core.utils.AnimationUtils.shake
import com.vetpet.petbeats.ui.home_admin.add_clinic_success_admin.AddClinicSuccessAdminViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
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

        binding.editNameAccount.addTextChangedListener {
            viewModel.onNameChange(it.toString())

        }
        binding.editEmail.addTextChangedListener {
            viewModel.onPasswordChange(it.toString())
        }

        binding.btnAddAccount.setOnClickListener {
            viewModel.onAddAccountClick()
        }
    }


    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    //check error
                    if (state.isName) {
                        binding.editNameAccount.setBackgroundResource(R.drawable.button_input_errol)
                        binding.nameAccountError.fadeIn()
                        binding.editNameAccount.shake()

                        val nameError = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.editNameAccount.setTextColor(nameError)
                    }
                    else {
                        binding.editNameAccount.setBackgroundResource(R.drawable.button_input)
                        binding.nameAccountError.fadeOut()

                        val nameSub = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.editNameAccount.setTextColor(nameSub)
                    }
                    if (state.isEmail) {
                        binding.editEmail.setBackgroundResource(R.drawable.button_input_errol)
                        binding.emailError.fadeIn()
                        binding.editEmail.shake()

                        val passwordError = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.editEmail.setTextColor(passwordError)
                    }
                    else {
                        binding.editEmail.setBackgroundResource(R.drawable.button_input)
                        binding.emailError.fadeOut()

                        val passwordSub = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.editEmail.setTextColor(passwordSub)
                    }

                    if (binding.nameAccountError.text.toString() != state.nameError) {
                        binding.nameAccountError.text = state.nameError
                    }
                    if (binding.emailError.text.toString() != state.emailError) {
                        binding.emailError.text = state.emailError
                    }



                    //check name
                    if (binding.editNameAccount.text.toString() != state.name) {
                        binding.editNameAccount.setText(state.name)
                    }

                    //check password
                    if (binding.editEmail.text.toString() != state.email) {
                        binding.editEmail.setText(state.email)
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
                        is AddClinicAdminEvent.NavigationListAppointmentAdmin -> {
                            findNavController().navigate(R.id.listAppointmentAdminFragment)
                        }

                        is AddClinicAdminEvent.NavigationAddClinicSuccess -> {
                            findNavController().navigate(R.id.addClinicSuccessAdminFragment)
                        }
                    }
                }
            }
        }
    }
}