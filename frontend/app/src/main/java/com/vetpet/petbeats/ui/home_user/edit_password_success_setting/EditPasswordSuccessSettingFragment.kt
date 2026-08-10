package com.vetpet.petbeats.ui.home_user.edit_password_success_setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentEditPasswordSettingBinding
import com.example.VetPet.databinding.FragmentEditPasswordSuccessSettingBinding
import com.example.VetPet.databinding.FragmentInformationClinicSuccessBinding
import com.vetpet.petbeats.ui.home_clinic.activitymain.HomeClinicActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class EditPasswordSuccessSettingFragment : Fragment() {
    private var _binding: FragmentEditPasswordSuccessSettingBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditPasswordSuccessSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(2000)

            findNavController().navigate(R.id.editPasswordSuccess_setting)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}