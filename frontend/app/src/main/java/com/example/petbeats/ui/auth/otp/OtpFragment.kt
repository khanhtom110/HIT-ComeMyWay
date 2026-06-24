package com.example.petbeats.ui.auth.otp

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.petbeats.databinding.FragmentHomeBinding
import com.example.petbeats.databinding.FragmentOtpBinding
import com.example.petbeats.ui.auth.login.LoginViewModelFactory
import kotlinx.coroutines.launch
import kotlin.toString


class OtpFragment : Fragment() {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OtpViewModel by viewModels {
        OtpViewModelFactory(
            AuthRepository(
                retrofit.create(ApiAuth::class.java)
            )
        )
    }
    private var currenScreen: String = ""
    private var countDownTimer: CountDownTimer ?= null

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

        currenScreen = arguments?.getString("nextscreen") ?: ""

        startResendTimer()

        setOnCLick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        _binding = null
    }

    private fun setOnCLick() {
        binding.verify.setOnClickListener {
            val email = arguments?.getString("email") ?: ""

            viewModel.onResetClick(email, currenScreen)
        }

        setupOtp()

        binding.vector.setOnClickListener {
            viewModel.vectorClick()
        }

        binding.otp.setOnClickListener {
            startResendTimer()
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
        binding.input5.addTextChangedListener {
            viewModel.input5(it.toString())
        }
        binding.input6.addTextChangedListener {
            viewModel.input6(it.toString())
        }
    }

    private fun startResendTimer() {
        binding.otp.isEnabled = false
        val otpReset = ContextCompat.getColor(requireContext(), R.color.colorOtp)
        binding.otp.setTextColor(otpReset)


        countDownTimer = object : CountDownTimer(120000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val totalSeconds = millisUntilFinished / 1000
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60

                val timeString = String.format("%02d:%02d", minutes, seconds)

                binding.otp.text = "Gửi lại ($timeString)"

                val email = arguments?.getString("email") ?: ""
                binding.content.text = "Chúng tôi đã gửi 1 mã xác minh đến $email.\nMã xác minh có giá trị trong $timeString"
            }

            override fun onFinish() {
                binding.otp.isEnabled = true
                val otpReset = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                binding.otp.setTextColor(otpReset)
                binding.otp.text = "Gửi lại"
            }
        }.start()
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


                    //check error
                    if (state.isOtp) {
                        binding.input1.setBackgroundResource(R.drawable.button_input_errol)
                        binding.input2.setBackgroundResource(R.drawable.button_input_errol)
                        binding.input3.setBackgroundResource(R.drawable.button_input_errol)
                        binding.input4.setBackgroundResource(R.drawable.button_input_errol)
                        binding.input5.setBackgroundResource(R.drawable.button_input_errol)
                        binding.input6.setBackgroundResource(R.drawable.button_input_errol)

                        binding.otpError.visibility = View.VISIBLE
                    }
                    else {
                        binding.input1.setBackgroundResource(R.drawable.button_input)
                        binding.input2.setBackgroundResource(R.drawable.button_input)
                        binding.input3.setBackgroundResource(R.drawable.button_input)
                        binding.input4.setBackgroundResource(R.drawable.button_input)
                        binding.input5.setBackgroundResource(R.drawable.button_input)
                        binding.input6.setBackgroundResource(R.drawable.button_input)

                        binding.otpError.visibility = View.GONE
                    }
                    if (binding.otpError.text.toString() != state.otpError) {
                        binding.otpError.text = state.otpError
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
                        //Nút vector quay lại màn trước
                        is OtpEvent.NavigationVector -> {
                            findNavController().popBackStack()
                        }

                        //Nút verify chuyển sang màn tiếp theo
                        is OtpEvent.NavigationSendToken -> {
                            if (currenScreen == "registersuccess") {
                                findNavController().navigate(R.id.registerSuccessFragment)
                            }
                            if (currenScreen == "resetpassword") {
                                findNavController().navigate(
                                    R.id.resetPasswordFragment,
                                    Bundle().apply {
                                        putString("token", event.token)
                                    }
                                )
                            }
                        }


                    }
                }
            }
        }
    }
}