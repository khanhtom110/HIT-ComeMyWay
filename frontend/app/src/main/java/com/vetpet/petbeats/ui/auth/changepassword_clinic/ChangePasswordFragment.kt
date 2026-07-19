package com.vetpet.petbeats.ui.auth.changepassword_clinic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentChangePasswordBinding
import com.example.VetPet.databinding.FragmentResetPasswordBinding
import com.vetpet.petbeats.data.remote.api.ApiAuth
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance.retrofit
import com.vetpet.petbeats.data.repository.AuthRepository
import com.vetpet.petbeats.ui.auth.resetpassword.ResetPasswordViewModel
import com.vetpet.petbeats.ui.auth.resetpassword.ResetPasswordViewModelFactory
import kotlin.getValue


class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChangePasswordViewModel by viewModels {
        ChangePasswordViewModelFactory(
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
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

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
            viewModel.changePassword()
        }

        binding.eye1.setOnClickListener {
            viewModel.changePassword1()
        }

        binding.inputPassword.addTextChangedListener {
            viewModel.onPasswordChange(it.toString())
        }

        binding.inputPassword1.addTextChangedListener {
            viewModel.onPasswordChange1(it.toString())
        }

        binding.changePassword.setOnClickListener {
            val token = arguments?.getString("token") ?: ""

            viewModel.onChangeClick(token)
        }
    }

    private fun stateData() {

    }

    private fun eventData() {

    }


}