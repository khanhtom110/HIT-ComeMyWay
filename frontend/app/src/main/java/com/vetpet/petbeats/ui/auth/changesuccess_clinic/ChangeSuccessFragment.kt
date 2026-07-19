package com.vetpet.petbeats.ui.auth.changesuccess_clinic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentChangeSuccessBinding
import com.example.VetPet.databinding.FragmentSplashClinicBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ChangeSuccessFragment : Fragment() {
    private var _binding: FragmentChangeSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(2000)
            findNavController().navigate(R.id.informationClinic)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}