package com.vetpet.petbeats.ui.home_user.news_detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentNewsDetailUserBinding
import com.example.VetPet.databinding.FragmentNewsUserBinding
import com.vetpet.petbeats.ui.home_user.news.NewsUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class NewsDetailUserFragment : Fragment() {
    private var _binding: FragmentNewsDetailUserBinding?= null
    private val binding get() = _binding!!
    private val viewModel: NewsDetailUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewsDetailUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("id") ?: 0

        viewModel.onNewsDetailClick(id)


        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.vector.setOnClickListener {
            viewModel.newsClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.tvClinicName.text = state.clinicName
                    binding.tvTitle.text = state.titleClinic
                    binding.tvContent.text = state.content


                    if (state.clinicAvatarUrl.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.clinicAvatarUrl)
                            .circleCrop()
                            .into(binding.imgClinic)
                    }

                    if (state.imageUrl.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.imageUrl)
                            .into(binding.imgNews)
                    }
                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when(event) {
                        is NewsDetailUserEvent.NavigationNewsUserClick -> {
                            findNavController().navigate(R.id.newsUserFragment)
                        }
                    }
                }
            }
        }
    }

}