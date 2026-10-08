package com.vetpet.petbeats.ui.home_clinic.news_success

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
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentNewsClinicBinding
import com.example.VetPet.databinding.FragmentNewsSuccessClinicBinding
import com.vetpet.petbeats.ui.home_clinic.news.NewsClinicViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class NewsSuccessClinicFragment : Fragment() {
    private var _binding: FragmentNewsSuccessClinicBinding?= null
    private val binding get() = _binding!!
    private val viewModel: NewsSuccessClinicViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewsSuccessClinicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("id") ?: 0
        viewModel.onInformationList(id)


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
            viewModel.newsPostClick()
        }
        binding.btnNews.setOnClickListener {
            viewModel.newsClick()
        }
        binding.btnTry.setOnClickListener {
            val id = arguments?.getInt("id") ?: 0
            viewModel.onInformationList(id)
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.tvState.text = state.tvState
                    binding.tvTitle.text = state.tvTitle
                    binding.tvContent.text = state.tvContent


                    if (state.imgState.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.imgState)
                            .into(binding.imgState)
                    }

                    if (state.imgNews.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.imgNews)
                            .into(binding.imgNews)
                    }

                    if (state.imgClinic.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.imgClinic)
                            .circleCrop()
                            .into(binding.imgClinic)
                    }



                    if (state.checkState) {
                        binding.imgState.setImageResource(R.drawable.icon_camera_send_cancel)
                        binding.tvState.text = "Tin tức đã được đăng thành công"
                    }
                    else {
                        binding.imgState.setImageResource(R.drawable.icon_camera_send_success)
                        binding.tvState.text = "Có lỗi trong quá trình đăng tin"
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
                        is NewsSuccessClinicEvent.NavigationNewsPost -> {
                            findNavController().navigate(R.id.newsSuccessClinic_newsPostClinic)
                        }
                        is NewsSuccessClinicEvent.NavigationNews -> {
                            findNavController().navigate(R.id.newsSuccessClinic_newsClinic)
                        }
                    }
                }
            }
        }
    }
}