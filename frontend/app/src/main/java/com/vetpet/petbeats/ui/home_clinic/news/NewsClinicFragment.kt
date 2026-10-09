package com.vetpet.petbeats.ui.home_clinic.news

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentNewsClinicBinding
import com.vetpet.petbeats.core.utils.AnimationUtils.crossFadeShimmerToContent
import com.vetpet.petbeats.ui.home_clinic.news.adapter.AdapterNewsClinic
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class NewsClinicFragment : Fragment() {
    private var _binding: FragmentNewsClinicBinding?= null
    private val binding get() = _binding!!
    private val viewModel: NewsClinicViewModel by viewModels()
    private lateinit var adapterNews: AdapterNewsClinic


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewsClinicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterNews = AdapterNewsClinic()

        binding.recycle.layoutManager = LinearLayoutManager(requireContext())
        binding.recycle.adapter = adapterNews

        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.tvClinicName.setOnClickListener {
            viewModel.newsPostClick()
        }
    }


    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapterNews.submitList(state.listNews)

                    if (state.isLoading) {
                        binding.shimmerFrameLayout.startShimmer()
                        binding.shimmerFrameLayout.visibility = View.VISIBLE
                        binding.recycle.visibility = View.GONE


                        binding.boxNews.visibility = View.GONE
                    }
                    else {
                        binding.shimmerFrameLayout.stopShimmer()

                        //Kiểm tra xem list có data không
                        if (state.listNews.isEmpty()) {
                            binding.shimmerFrameLayout.visibility = View.GONE
                            binding.recycle.visibility = View.VISIBLE

                            binding.boxNews.visibility = View.VISIBLE
                        } else {
                            crossFadeShimmerToContent(binding.shimmerFrameLayout, binding.recycle)
                            binding.boxNews.visibility = View.GONE
                        }
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
                        is NewsClinicEvent.NavigationClinicPost -> {
                            findNavController().navigate(
                                R.id.newsPostClinicFragment,
                                Bundle().apply {
                                    putInt("id", event.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }


}