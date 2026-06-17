package com.example.petbeats.ui.auth.otp

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
import com.example.petbeats.databinding.FragmentHomeBinding
import com.example.petbeats.databinding.FragmentOtpBinding
import kotlinx.coroutines.launch
import kotlin.toString


class OtpFragment : Fragment() {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OtpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOtpBinding.inflate(inflater, container, false)

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
        binding.verify.setOnClickListener {
            val email = arguments?.getString("email") ?: ""

            viewModel.onResetClick(email)
        }

        setupOtp()

        binding.vector.setOnClickListener {
            viewModel.forgotClick()
        }

    }

    private fun setupOtp() {
        //Tự động nhảy sang ô tiếp theo
        binding.input1.addTextChangedListener {
            if (it?.length == 1) {
                binding.input2.requestFocus()
            }
        }

        binding.input2.addTextChangedListener {
            if (it?.length == 1) {
                binding.input3.requestFocus()
            }
        }

        binding.input3.addTextChangedListener {
            if (it?.length == 1) {
                binding.input4.requestFocus()
            }
        }

        binding.input4.addTextChangedListener {
            if (it?.length == 1) {
                binding.input5.requestFocus()
            }
        }

        binding.input5.addTextChangedListener {
            if (it?.length == 1) {
                binding.input6.requestFocus()
            }
        }

        //chuyển dữ liệu sang viewmodel để lưu lên OtpUiState
        binding.input1.addTextChangedListener {
            viewModel.input1(it.toString())
        }
        binding.input2.addTextChangedListener {
            viewModel.input2(it.toString())
        }
        binding.input3.addTextChangedListener {
            viewModel.input3(it.toString())
        }
        binding.input4.addTextChangedListener {
            viewModel.input4(it.toString())
        }
        binding.input4.addTextChangedListener {
            viewModel.input5(it.toString())
        }
        binding.input4.addTextChangedListener {
            viewModel.input6(it.toString())
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (binding.input1.text.toString() != state.otp1) {
                        binding.input1.setText(state.otp1)
                    }

                    if (binding.input2.text.toString() != state.otp2) {
                        binding.input2.setText(state.otp2)
                    }

                    if (binding.input3.text.toString() != state.otp3) {
                        binding.input3.setText(state.otp3)
                    }

                    if (binding.input4.text.toString() != state.otp4) {
                        binding.input4.setText(state.otp4)
                    }

                    if (binding.input5.text.toString() != state.otp5) {
                        binding.input5.setText(state.otp5)
                    }

                    if (binding.input6.text.toString() != state.otp6) {
                        binding.input6.setText(state.otp6)
                    }
                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewModel.event.collect { event ->
                when (event) {
                    is OtpEvent.NavigationForgot -> {
                        findNavController().navigate(R.id.forgotPasswordFragment)
                    }

                    is OtpEvent.NavigationResetSendEmail -> {
                        findNavController().navigate(
                            R.id.resetPasswordFragment,
                            Bundle().apply {
                                putString("email", event.email)
                                putString("otp", binding.input1.text.toString() + binding.input2.text.toString() + binding.input3.text.toString() + binding.input4.text.toString() + binding.input5.text.toString() + binding.input6.text.toString())
                            }
                        )
                    }

                }
            }
        }
    }
}