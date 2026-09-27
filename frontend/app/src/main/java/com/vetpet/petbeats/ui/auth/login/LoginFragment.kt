package com.vetpet.petbeats.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.VetPet.R
import com.vetpet.petbeats.data.remote.api.ApiAuth
import com.vetpet.petbeats.data.repository.AuthRepository
import com.vetpet.petbeats.data.remote.sharepreference.TokenManager
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.example.VetPet.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat
import com.vetpet.petbeats.ui.home_clinic.activitymain.HomeClinicActivity
import com.vetpet.petbeats.ui.home_user.activitymain.HomeActivity
import com.vetpet.petbeats.core.utils.AnimationUtils.shake
import com.vetpet.petbeats.core.utils.AnimationUtils.fadeIn
import com.vetpet.petbeats.core.utils.AnimationUtils.fadeOut
import com.vetpet.petbeats.core.utils.AnimationUtils.scaleBounce
import com.vetpet.petbeats.core.utils.AnimationUtils.staggeredEntrance
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAnimations()
        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAnimations() {
        staggeredEntrance(
            binding.logo1, binding.tittle,
            binding.textName, binding.inputName,
            binding.textPassword, binding.inputPassword,
            binding.login, binding.donthave,
            delayStep = 60
        )
    }

    private fun setOnClick() {
        binding.eye.setOnClickListener {
            viewModel.changeEye()
        }

        binding.forgotPassword.setOnClickListener {
            viewModel.forgotCLick()
        }
        binding.signUp.setOnClickListener {
            viewModel.registerClick()
        }

        binding.inputName.addTextChangedListener {
            viewModel.onNameChange(it.toString())

        }
        binding.inputPassword.addTextChangedListener {
            viewModel.onPasswordChange(it.toString())
        }

        binding.login.setOnClickListener {
            viewModel.onLoginClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    //displayPassword
                    if (state.isPasswordVisible) {
                        binding.inputPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.eye.setImageResource(R.drawable.open_eye)
                    }
                    else {
                        binding.inputPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.eye.setImageResource(R.drawable.close_eye)
                    }





                    //check error
                    if (state.isName) {
                        binding.inputName.setBackgroundResource(R.drawable.button_input_errol)
                        binding.nameError.fadeIn()
                        binding.inputName.shake()

                        val nameError = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.inputName.setTextColor(nameError)
                    }
                    else {
                        binding.inputName.setBackgroundResource(R.drawable.button_input)
                        binding.nameError.fadeOut()

                        val nameSub = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.inputName.setTextColor(nameSub)
                    }
                    if (state.isPassword) {
                        binding.inputPassword.setBackgroundResource(R.drawable.button_input_errol)
                        binding.passwordError.fadeIn()
                        binding.inputPassword.shake()

                        val passwordError = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.inputPassword.setTextColor(passwordError)
                    }
                    else {
                        binding.inputPassword.setBackgroundResource(R.drawable.button_input)
                        binding.passwordError.fadeOut()

                        val passwordSub = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.inputPassword.setTextColor(passwordSub)
                    }

                    if (binding.nameError.text.toString() != state.nameError) {
                        binding.nameError.text = state.nameError
                    }
                    if (binding.passwordError.text.toString() != state.passwordError) {
                        binding.passwordError.text = state.passwordError
                    }



                    //check name
                    if (binding.inputName.text.toString() != state.name) {
                        binding.inputName.setText(state.name)
                    }

                    //check password
                    if (binding.inputPassword.text.toString() != state.password) {
                        binding.inputPassword.setText(state.password)
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
                        is LoginEvent.NavigationForgot -> {
                            findNavController().navigate(R.id.forgotPasswordFragment)
                        }
                        is LoginEvent.NavigationRegister -> {
                            findNavController().navigate(R.id.registerFragment)
                        }
                        is LoginEvent.NavigationUserHome -> {
                            val tokenManager = TokenManager(requireContext())
                            tokenManager.saveTokens(event.accessToken, event.refreshToken)
                            tokenManager.saveUserId(event.userId)

                            val intent = Intent(requireContext(), HomeActivity::class.java)
                            startActivity(intent)
                        }
                        is LoginEvent.NavigationChangePassword -> {
                            val tokenManager = TokenManager(requireContext())
                            tokenManager.saveTokens(event.accessToken, event.refreshToken)
                            tokenManager.saveUserId(event.userId)

                            findNavController().navigate(R.id.splashClinicFragment)
                        }
                        is LoginEvent.NavigationLoginSuccess -> {
                            val tokenManager = TokenManager(requireContext())
                            tokenManager.saveTokens(event.accessToken, event.refreshToken)
                            tokenManager.saveUserId(event.userId)

                            findNavController().navigate(R.id.loginSuccessFragment)
                        }
                        is LoginEvent.NavigationClinicHome -> {
                            val tokenManager = TokenManager(requireContext())
                            tokenManager.saveTokens(event.accessToken, event.refreshToken)
                            tokenManager.saveUserId(event.userId)

                            val intent = Intent(requireContext(), HomeClinicActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

}