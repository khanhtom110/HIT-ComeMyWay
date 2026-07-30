package com.vetpet.petbeats.ui.home_user.edit_resetpassword_setting

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
import com.example.VetPet.databinding.FragmentEditResetpasswordSettingBinding
import com.example.VetPet.databinding.FragmentResetPasswordBinding
import com.vetpet.petbeats.data.remote.api.ApiAuth
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance.retrofit
import com.vetpet.petbeats.data.repository.AuthRepository
import com.vetpet.petbeats.ui.auth.resetpassword.ResetPasswordEvent
import com.vetpet.petbeats.ui.auth.resetpassword.ResetPasswordViewModel
import com.vetpet.petbeats.ui.auth.resetpassword.ResetPasswordViewModelFactory
import kotlinx.coroutines.launch
import kotlin.getValue


class EditResetpasswordSettingFragment : Fragment() {
    private var _binding: FragmentEditResetpasswordSettingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditResetpasswordSettingViewModel by viewModels {
        EditResetpasswordSettingViewModelFactory(
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
        _binding = FragmentEditResetpasswordSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnCLick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnCLick() {
        binding.vector.setOnClickListener {
            val email = arguments?.getString("email") ?: ""

            viewModel.otpClick(email)
        }

        binding.eye.setOnClickListener {
            viewModel.changePassword()
        }

        binding.eye1.setOnClickListener {
            viewModel.changePassword1()
        }

        binding.setting.setOnClickListener {
            viewModel.onSettingClick()
        }

        binding.inputPassword.addTextChangedListener {
            viewModel.onPasswordChange(it.toString())
        }

        binding.inputPassword1.addTextChangedListener {
            viewModel.onPasswordChange1(it.toString())
        }

        binding.resetPassword.setOnClickListener {
            val token = arguments?.getString("token") ?: ""

            viewModel.onSuccessClick(token)
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    //isPassword
                    if (state.isPasswordVisible) {
                        binding.inputPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.eye.setImageResource(R.drawable.open_eye)
                    }
                    else {
                        binding.inputPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.eye.setImageResource(R.drawable.close_eye)
                    }



                    //isPassword1
                    if (state.isPasswordVisible1) {
                        binding.inputPassword1.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.eye1.setImageResource(R.drawable.open_eye)
                    }
                    else {
                        binding.inputPassword1.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.eye1.setImageResource(R.drawable.close_eye)
                    }



                    //check error
                    if (state.isPassword) {
                        binding.inputPassword.setBackgroundResource(R.drawable.button_input_errol)
                        binding.passwordError.visibility = View.VISIBLE

                        val passwordError = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.inputPassword.setTextColor(passwordError)
                    }
                    else {
                        binding.inputPassword.setBackgroundResource(R.drawable.button_input)
                        binding.passwordError.visibility = View.GONE

                        val passwordSub = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.inputPassword.setTextColor(passwordSub)
                    }
                    if (state.isPassword1) {
                        binding.inputPassword1.setBackgroundResource(R.drawable.button_input_errol)
                        binding.passwordError1.visibility = View.VISIBLE

                        val passwordError1 = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.inputPassword1.setTextColor(passwordError1)
                    }
                    else {
                        binding.inputPassword1.setBackgroundResource(R.drawable.button_input)
                        binding.passwordError1.visibility = View.GONE

                        val passwordSub1 = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.inputPassword1.setTextColor(passwordSub1)
                    }


                    if (binding.passwordError.text.toString() != state.passwordError) {
                        binding.passwordError.text = state.passwordError
                    }
                    if (binding.passwordError1.text.toString() != state.passwordError1) {
                        binding.passwordError1.text = state.passwordError1
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is EditResetpasswordSettingEvent.NavigaitonOtpSendEmail -> {
                            findNavController().navigate(
                                R.id.editResetpassword_editOtp,
                                Bundle().apply {
                                    putString("nextscreen", "resetpassword")
                                    putString("email", event.email)
                                }
                            )
                        }

                        is EditResetpasswordSettingEvent.NavigationEditpasswordSuccess -> {
                            findNavController().navigate(R.id.stateSuccessFragment)
                        }

                        is EditResetpasswordSettingEvent.NavigationSetting -> {
                            findNavController().navigate(R.id.editResetpassword_setting)
                        }
                    }
                }

            }
        }
    }
}