package com.vetpet.petbeats.ui.home_user.editpassword_setting

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
import com.example.VetPet.databinding.FragmentEditPasswordSettingBinding
import com.example.VetPet.databinding.FragmentSettingUserBinding
import com.vetpet.petbeats.data.remote.api.ApiUserHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.remote.sharepreference.TokenManager
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.setting.SettingUserViewModel
import com.vetpet.petbeats.ui.home_user.setting.SettingUserViewModelFactory
import kotlinx.coroutines.launch
import kotlin.getValue


class EditPasswordSettingFragment : Fragment() {
    private var _binding: FragmentEditPasswordSettingBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: EditPasswordSettingViewModel by viewModels {
        EditPasswordSettingViewModelFactory(
            HomeUserRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiUserHome::class.java)
            ),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditPasswordSettingBinding.inflate(inflater, container, false)
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
            viewModel.setting()
        }
        binding.btnForgotPassword.setOnClickListener {
            viewModel.forgotPassword()
        }
        binding.btnUpdate.setOnClickListener {
            viewModel.onEditPasswordClick()
        }



        binding.eye.setOnClickListener {
            viewModel.changePassword()
        }
        binding.eye1.setOnClickListener {
            viewModel.changeNewPassword()
        }
        binding.eye2.setOnClickListener {
            viewModel.changeNewPassword1()
        }
        binding.inputPassword.addTextChangedListener {
            viewModel.onPasswordChange(it.toString())
        }
        binding.textNewInputPassword.addTextChangedListener {
            viewModel.onNewPasswordChange(it.toString())
        }
        binding.textNewInputPassword1.addTextChangedListener {
            viewModel.onNewPasswordChange1(it.toString())
        }

    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {  state ->
                    //isPassword
                    if (state.isPasswordVisible) {
                        binding.inputPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.eye.setImageResource(R.drawable.open_eye)
                    }
                    else {
                        binding.inputPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.eye.setImageResource(R.drawable.close_eye)
                    }

                    if (state.isNewPasswordVisible) {
                        binding.textNewInputPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.eye1.setImageResource(R.drawable.open_eye)
                    }
                    else {
                        binding.textNewInputPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.eye1.setImageResource(R.drawable.close_eye)
                    }

                    if (state.isNewPasswordVisible1) {
                        binding.textNewInputPassword1.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.eye2.setImageResource(R.drawable.open_eye)
                    }
                    else {
                        binding.textNewInputPassword1.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.eye2.setImageResource(R.drawable.close_eye)
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
                    if (state.isNewPassword) {
                        binding.textNewInputPassword.setBackgroundResource(R.drawable.button_input_errol)
                        binding.passwordNewError.visibility = View.VISIBLE

                        val passwordNewError = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.textNewInputPassword.setTextColor(passwordNewError)
                    }
                    else {
                        binding.textNewInputPassword.setBackgroundResource(R.drawable.button_input)
                        binding.passwordNewError.visibility = View.GONE

                        val passwordNewSub = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.textNewInputPassword.setTextColor(passwordNewSub)
                    }
                    if (state.isNewPassword1) {
                        binding.textNewInputPassword1.setBackgroundResource(R.drawable.button_input_errol)
                        binding.passwordNewError1.visibility = View.VISIBLE

                        val passwordNewError1 = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.textNewInputPassword1.setTextColor(passwordNewError1)
                    }
                    else {
                        binding.textNewInputPassword1.setBackgroundResource(R.drawable.button_input)
                        binding.passwordNewError1.visibility = View.GONE

                        val passwordNewSub1 = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.textNewInputPassword1.setTextColor(passwordNewSub1)
                    }



                    if (binding.passwordError.text.toString() != state.passwordError) {
                        binding.passwordError.text = state.passwordError
                    }
                    if (binding.passwordNewError.text.toString() != state.passwordError) {
                        binding.passwordNewError.text = state.passwordNewError
                    }
                    if (binding.passwordNewError1.text.toString() != state.passwordError) {
                        binding.passwordNewError1.text = state.passwordNewError
                    }



                    //check password
                    if (binding.inputPassword.text.toString() != state.password) {
                        binding.inputPassword.setText(state.password)
                    }
                    if (binding.textNewInputPassword.text.toString() != state.newPassword) {
                        binding.textNewInputPassword.setText(state.newPassword)
                    }
                    if (binding.textNewInputPassword1.text.toString() != state.newPassword1) {
                        binding.textNewInputPassword1.setText(state.newPassword1)
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
                        is EditPasswordSettingEvent.NavigationSetting -> {
                            findNavController().navigate(R.id.editPassword_setting)
                        }
                        is EditPasswordSettingEvent.NavigationForgotPassword -> {
                            findNavController().navigate(R.id.forgotPasswordFragment)
                        }
                    }
                }
            }
        }
    }

}