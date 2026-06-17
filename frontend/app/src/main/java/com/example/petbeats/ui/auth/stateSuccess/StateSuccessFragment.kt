package com.example.petbeats.ui.auth.stateSuccess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.petbeats.R
import com.example.petbeats.databinding.FragmentStateSuccessBinding
import kotlinx.coroutines.launch


class StateSuccessFragment : Fragment() {
    private lateinit var binding: FragmentStateSuccessBinding
    private val viewModel: StateSuccessViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_state_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStateSuccessBinding.bind(view)

        setOnCLick()
        eventData()
    }

    private fun setOnCLick() {
        binding.backToLogin.setOnClickListener {
            viewModel.loginClick()
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewModel.event.collect { event ->
                when (event) {
                    is StateSuccessEvent.NavigationLogin -> {
                        findNavController().navigate(R.id.success_login)
                    }

                }
            }
        }
    }

}