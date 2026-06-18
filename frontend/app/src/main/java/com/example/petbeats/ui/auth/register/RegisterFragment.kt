package com.example.petbeats.ui.auth.register

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
import com.example.petbeats.databinding.FragmentHomeBinding
import com.example.petbeats.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch
import kotlin.toString

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

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
        binding.eye.setOnClickListener {
            viewModel.changeEye()
        }

        binding.eye1.setOnClickListener {
            viewModel.changeEye1()
        }

        binding.login1.setOnClickListener {
            viewModel.loginClick()
        }

        binding.inputName.addTextChangedListener {
            viewModel.onNameChange(it.toString())
        }

        binding.inputEmail.addTextChangedListener {
            viewModel.onEmailChange(it.toString())
        }

        binding.inputPassword.addTextChangedListener {
            viewModel.onPasswordChange(it.toString())
        }

        binding.inputPassword1.addTextChangedListener {
            viewModel.onPasswordChange1(it.toString())
        }

        binding.login.setOnClickListener {
            viewModel.onLoginClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                    if (state.isName) {
                        binding.inputName.setBackgroundResource(R.drawable.button_input_errol)
                    }
                    else {
                        binding.inputName.setBackgroundResource(R.drawable.button_input)
                    }
                    if (state.isEmail) {
                        binding.inputEmail.setBackgroundResource(R.drawable.button_input_errol)
                    }
                    else {
                        binding.inputEmail.setBackgroundResource(R.drawable.button_input)
                    }
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

                    if (binding.textError.text.toString() != state.error) {
                        binding.textError.text = state.error
                    }



                    //check Name
                    if (binding.inputName.text.toString() != state.name) {
                        binding.inputName.setText(state.name)
                    }

                    //check email
                    if (binding.inputEmail.text.toString() != state.email) {
                        binding.inputEmail.setText(state.email)
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
        //navigationLogin
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is RegisterEvent.NavigationLogin -> {
                            findNavController().navigate(R.id.loginFragment)
                        }
                    }
                }
            }
        }
    }

}