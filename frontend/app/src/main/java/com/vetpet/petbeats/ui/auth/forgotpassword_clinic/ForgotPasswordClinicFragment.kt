package com.vetpet.petbeats.ui.auth.forgotpassword_clinic

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
import com.example.VetPet.databinding.FragmentForgotPasswordClinicBinding
import kotlinx.coroutines.launch
import kotlin.getValue


class ForgotPasswordClinicFragment : Fragment() {
    private var _binding: FragmentForgotPasswordClinicBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ForgotPasswordClinicViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForgotPasswordClinicBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClick()
        eventData()
    }

    private fun setOnClick() {
        binding.sendCode.setOnClickListener {
            viewModel.loginClick()
        }

        binding.vector.setOnClickListener {
            viewModel.forgotClick()
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is ForgotPasswordClinicEvent.NavigationForgotPassword -> {
                            findNavController().navigate(R.id.forgotClinic_forgot)
                        }
                        is ForgotPasswordClinicEvent.NavigationLogin -> {
                            findNavController().navigate(R.id.forgotPassword_login)
                        }
                    }
                }
            }
        }
    }


}