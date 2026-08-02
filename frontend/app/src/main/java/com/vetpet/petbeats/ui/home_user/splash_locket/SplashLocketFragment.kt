package com.vetpet.petbeats.ui.home_user.splash_locket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentEditPasswordSuccessSettingBinding
import com.example.VetPet.databinding.FragmentSplashLocketBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashLocketFragment : Fragment() {
    private var _binding: FragmentSplashLocketBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSplashLocketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(2000)

            findNavController().navigate(R.id.splashLocket_locket)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}