package com.vetpet.petbeats.ui.auth.register

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
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch
import kotlin.toString
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import com.vetpet.petbeats.core.utils.AnimationUtils.shake
import com.vetpet.petbeats.core.utils.AnimationUtils.fadeIn
import com.vetpet.petbeats.core.utils.AnimationUtils.fadeOut
import com.vetpet.petbeats.core.utils.AnimationUtils.scaleBounce
import com.vetpet.petbeats.core.utils.AnimationUtils.staggeredEntrance

@AndroidEntryPoint
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

        setupAnimations()
        setOnCLick()
        stateData()
        eventData()
    }

    private fun setupAnimations() {
        staggeredEntrance(
            binding.logo1,
            binding.tittle,
            binding.inputName,
            binding.inputEmail,
            binding.inputPassword,
            binding.inputPassword1,
            binding.otp,
            binding.login1
        )
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

        binding.otp.setOnClickListener {
            viewModel.onOtpClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                    if (state.isEmail) {
                        binding.inputEmail.setBackgroundResource(R.drawable.button_input_errol)
                        binding.emailError.fadeIn()
                        binding.inputEmail.shake()

                        val emailError = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.inputEmail.setTextColor(emailError)
                    }
                    else {
                        binding.inputEmail.setBackgroundResource(R.drawable.button_input)
                        binding.emailError.fadeOut()

                        val emailSub = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.inputEmail.setTextColor(emailSub)
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
                    if (state.isPassword1) {
                        binding.inputPassword1.setBackgroundResource(R.drawable.button_input_errol)
                        binding.passwordError1.fadeIn()
                        binding.inputPassword1.shake()

                        val passwordError1 = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.inputPassword1.setTextColor(passwordError1)
                    }
                    else {
                        binding.inputPassword1.setBackgroundResource(R.drawable.button_input)
                        binding.passwordError1.fadeOut()

                        val passwordSub1 = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.inputPassword1.setTextColor(passwordSub1)
                    }


                    if (binding.nameError.text.toString() != state.nameError) {
                        binding.nameError.text = state.nameError
                    }
                    if (binding.emailError.text.toString() != state.emailError) {
                        binding.emailError.text = state.emailError
                    }
                    if (binding.passwordError.text.toString() != state.passwordError) {
                        binding.passwordError.text = state.passwordError
                    }
                    if (binding.passwordError1.text.toString() != state.passwordError1) {
                        binding.passwordError1.text = state.passwordError1
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

                    if (binding.inputPassword1.text.toString() != state.password1) {
                        binding.inputPassword1.setText(state.password1)
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
                        is RegisterEvent.NavigationRegisterSendEmail -> {
                            findNavController().navigate(
                                R.id.otpFragment,
                                Bundle().apply {
                                    putString("email", event.email)
                                    putString("nextscreen", "registersuccess")
                                }
                            )
                        }

                        is RegisterEvent.NavigationLogin -> {
                            findNavController().navigate(R.id.loginFragment)
                        }
                    }
                }
            }
        }
    }

}