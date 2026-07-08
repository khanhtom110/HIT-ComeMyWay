package com.example.petbeats.ui.home.pet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.petbeats.R
import com.example.petbeats.data.remote.sharepreference.TokenManager
import com.example.petbeats.databinding.FragmentPetBinding



class PetFragment : Fragment() {
    private var _binding: FragmentPetBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.logout.setOnClickListener {
            performLogout()
        }
    }

    private fun performLogout() {
        // 1. Xóa sạch Token trong máy
        val tokenManager = TokenManager(requireContext())
        tokenManager.clearTokens()


        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_home, true)
            .build()

        findNavController().navigate(R.id.loginFragment, null, navOptions)
    }

}