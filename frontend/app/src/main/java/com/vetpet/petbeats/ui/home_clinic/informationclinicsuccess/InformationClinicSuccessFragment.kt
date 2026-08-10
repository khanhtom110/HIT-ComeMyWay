package com.vetpet.petbeats.ui.home_clinic.informationclinicsuccess

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentInformationClinicBinding
import com.example.VetPet.databinding.FragmentInformationClinicSuccessBinding
import com.example.VetPet.databinding.FragmentSplashClinicBinding
import com.vetpet.petbeats.ui.home_clinic.activitymain.HomeClinicActivity
import com.vetpet.petbeats.ui.home_user.activitymain.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class InformationClinicSuccessFragment : Fragment() {
    private var _binding: FragmentInformationClinicSuccessBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInformationClinicSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(2000)

            val intent = Intent(requireContext(), HomeClinicActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}