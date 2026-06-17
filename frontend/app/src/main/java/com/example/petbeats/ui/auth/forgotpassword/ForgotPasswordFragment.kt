package com.example.petbeats.ui.auth.forgotpassword

import android.os.Bundle
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
import com.example.petbeats.databinding.FragmentForgotPasswordBinding
import kotlinx.coroutines.launch
import kotlin.toString


class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForgotPasswordBinding.bind(view)

        setOnClick()
        stateData()
        eventData()
    }

    private fun setOnClick() {
        binding.vector.setOnClickListener {
            viewModel.loginClick()
        }

        binding.inputEmail.addTextChangedListener {
            viewModel.onEmailChange(it.toString())
        }

        binding.sendCode.setOnClickListener {
            viewModel.onOtpClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    //checkEmail
                    if (binding.inputEmail.text.toString() != state.email) {
                        binding.inputEmail.setText(state.email)
                    }

                    if (binding.textError.toString() != state.error) {
                        binding.textError.text = state.error
                    }



                    //check error
                    if (state.isEmail) {
                        binding.inputEmail.setBackgroundResource(R.drawable.button_input_errol)
                    }
                    else {
                        binding.inputEmail.setBackgroundResource(R.drawable.button_input)
                    }
                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewModel.event.collect { event ->
                when (event) {
                    is ForgotPasswordEvent.NavigationOTPSendEmail -> {
                        findNavController().navigate(
                            R.id.otpFragment,
                            Bundle().apply {
                                putString("email", event.email)
                            }
                        )
                    }

                    is ForgotPasswordEvent.NavigationLogin -> {
                        findNavController().navigate(R.id.loginFragment)
                    }
                }
            }
        }
    }

}