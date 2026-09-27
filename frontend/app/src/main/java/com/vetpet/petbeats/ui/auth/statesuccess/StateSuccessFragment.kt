package com.vetpet.petbeats.ui.auth.statesuccess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentStateSuccessBinding
import com.vetpet.petbeats.core.utils.AnimationUtils.popIn
import com.vetpet.petbeats.core.utils.AnimationUtils.scaleBounce
import kotlinx.coroutines.launch


class StateSuccessFragment : Fragment() {
    private var _binding: FragmentStateSuccessBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StateSuccessViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStateSuccessBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Success animation
        binding.logo1.popIn()
        binding.tittle.let {
            it.alpha = 0f
            it.animate().alpha(1f).setStartDelay(400).setDuration(350).start()
        }
        binding.backToLogin.let {
            it.alpha = 0f
            it.animate().alpha(1f).setStartDelay(600).setDuration(350).start()
        }

        setOnClick()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.backToLogin.setOnClickListener {
            binding.backToLogin.scaleBounce()
            viewModel.loginClick()
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is StateSuccessEvent.NavigationLogin -> {
                            findNavController().navigate(R.id.loginFragment)
                        }
                    }
                }
            }
        }
    }
}