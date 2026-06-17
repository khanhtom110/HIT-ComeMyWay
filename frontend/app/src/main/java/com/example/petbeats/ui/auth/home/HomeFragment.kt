package com.example.petbeats.ui.auth.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.petbeats.R
import com.example.petbeats.databinding.FragmentHomeBinding
import com.example.petbeats.ui.auth.login.LoginFragment
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        setOnClick()
        stateData()
    }

    private fun setOnClick() {
        binding.login.setOnClickListener {
            viewModel.loginClick()
        }
        binding.register.setOnClickListener {
            viewModel.registerClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewModel.event.collect { event ->
                when (event) {
                    is HomeEvent.NavigationLogin -> {
                        findNavController().navigate(R.id.loginFragment)
                    }
                    is HomeEvent.NavigationRegister -> {
                        findNavController().navigate(R.id.registerFragment)
                    }
                }
            }
        }
    }

}