package com.example.petbeats.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
import com.example.petbeats.R
import com.example.petbeats.data.remote.api.ApiAuth
import com.example.petbeats.data.remote.retrofitInstance.RetrofitInstance.retrofit
import com.example.petbeats.data.repository.AuthRepository
import com.example.petbeats.data.local.sharepreference.TokenManager
import com.example.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.example.petbeats.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat
import com.example.petbeats.ui.home.activitymain.HomeActivity


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            AuthRepository(
                RetrofitInstance.retrofit.create(ApiAuth::class.java)
            )
        )
    }

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

        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                        binding.nameError.visibility = View.VISIBLE

                        val nameError = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.inputName.setTextColor(nameError)
                    }
                    else {
                        binding.inputName.setBackgroundResource(R.drawable.button_input)
                        binding.nameError.visibility = View.GONE

                        val nameSub = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.inputName.setTextColor(nameSub)
                    }
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
                        is LoginEvent.NavigationHome -> {
                            val tokenManager = TokenManager(requireContext())
                            tokenManager.saveTokens(event.accessToken, event.refreshToken)

                            val intent = Intent(requireContext(), HomeActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

}