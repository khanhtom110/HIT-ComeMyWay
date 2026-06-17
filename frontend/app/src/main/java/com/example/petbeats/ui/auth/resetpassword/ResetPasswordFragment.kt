package com.example.petbeats.ui.auth.resetpassword

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.petbeats.R
import com.example.petbeats.databinding.FragmentResetPasswordBinding
import kotlinx.coroutines.launch
import kotlin.toString


class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResetPasswordBinding.bind(view)

        setOnCLick()
        stateData()
        eventData()
    }

    private fun setOnCLick() {
        binding.vector.setOnClickListener {
            viewModel.otpClick()
        }

        binding.eye.setOnClickListener {
            viewModel.changePassword()
        }

        binding.eye1.setOnClickListener {
            viewModel.changePassword1()
        }

        binding.inputPassword.addTextChangedListener {
            viewModel.onPasswordChange(it.toString())
        }

        binding.inputPassword1.addTextChangedListener {
            viewModel.onPasswordChange1(it.toString())
        }

        binding.resetPassword.setOnClickListener {
            val email = arguments?.getString("email") ?: ""
            val otp = arguments?.getString("otp") ?: ""

            viewModel.onSuccessClick(email, otp)
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    //isPassword
                    if (state.isPasswordVisible) {
                        binding.inputPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.eye.setImageResource(R.drawable.eyeopen)
                    }
                    else {
                        binding.inputPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.eye.setImageResource(R.drawable.eyeclose)
                    }

                    binding.inputPassword.setSelection(binding.inputPassword.length())


                    //isPassword1
                    if (state.isPasswordVisible1) {
                        binding.inputPassword1.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.eye1.setImageResource(R.drawable.eyeopen)
                    }
                    else {
                        binding.inputPassword1.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.eye1.setImageResource(R.drawable.eyeclose)
                    }

                    binding.inputPassword1.setSelection(binding.inputPassword1.length())


                    //check error
                    if (state.isPassword) {
                        binding.inputPassword.setBackgroundResource(R.drawable.button_input_errol)
                    }
                    else {
                        binding.inputPassword.setBackgroundResource(R.drawable.button_input)
                    }
                    if (state.isPassword1) {
                        binding.inputPassword1.setBackgroundResource(R.drawable.button_input_errol)
                    }
                    else {
                        binding.inputPassword1.setBackgroundResource(R.drawable.button_input)
                    }

                    if (binding.textError.toString() != state.error) {
                        binding.textError.text = state.error
                    }


                    //check password
                    if (binding.inputPassword.text.toString() != state.password) {
                        binding.inputPassword.setText(state.password)
                    }

                    //check password1
                    if (binding.inputPassword1.text.toString() != state.password1) {
                        binding.inputPassword1.setText(state.password1)
                    }
                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewModel.event.collect { event ->
                when (event) {
                    is ResetPasswordEvent.NavigationOTP -> {
                        findNavController().navigate(R.id.otpFragment)
                    }

                    is ResetPasswordEvent.NavigationSuccess -> {
                        findNavController().navigate(R.id.stateSuccessFragment)
                    }
                }
            }
        }
    }
}