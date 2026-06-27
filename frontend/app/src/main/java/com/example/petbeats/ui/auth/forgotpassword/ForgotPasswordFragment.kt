package com.example.petbeats.ui.auth.forgotpassword

import android.os.Bundle
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
import com.example.petbeats.R
import com.example.petbeats.data.remote.api.ApiAuth
import com.example.petbeats.data.remote.retrofitInstance.RetrofitInstance.retrofit
import com.example.petbeats.data.repository.AuthRepository
import com.example.petbeats.databinding.FragmentForgotPasswordBinding
import kotlinx.coroutines.launch
import kotlin.toString


class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ForgotPasswordViewModel by viewModels {
        ForgotPasswordViewModelFactory(
            AuthRepository(
                retrofit.create(ApiAuth::class.java)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

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
        binding.vector.setOnClickListener {
            viewModel.loginClick()
        }

        binding.inputEmail.addTextChangedListener {
            viewModel.onEmailChange(it.toString())
        }

        binding.sendCode.setOnClickListener {
            viewModel.onOtpClick()
        }

        binding.login.setOnClickListener {
            viewModel.loginClick()
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

                    if (binding.emailError.text.toString() != state.emailError) {
                        binding.emailError.text = state.emailError
                    }

                    //check error
                    if (state.isEmail) {
                        binding.inputEmail.setBackgroundResource(R.drawable.button_input_errol)
                        binding.emailError.visibility = View.VISIBLE

                        val emailError = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.inputEmail.setTextColor(emailError)
                    }
                    else {
                        binding.inputEmail.setBackgroundResource(R.drawable.button_input)
                        binding.emailError.visibility = View.GONE

                        val emailSub = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.inputEmail.setTextColor(emailSub)
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
                        is ForgotPasswordEvent.NavigationOTPSendEmail -> {
                            findNavController().navigate(
                                R.id.otpFragment,
                                Bundle().apply {
                                    putString("email", event.email)
                                    putString("nextscreen", "resetpassword")
                                }
                            )
                        }

                        is ForgotPasswordEvent.NavigationLogin -> {
                            findNavController().navigate(R.id.forgotPassword_login)
                        }
                    }
                }
            }
        }
    }

}