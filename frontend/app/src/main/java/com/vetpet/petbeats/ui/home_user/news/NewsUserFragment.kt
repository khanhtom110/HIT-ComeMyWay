package com.vetpet.petbeats.ui.home_user.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.VetPet.R
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.VetPet.databinding.FragmentNewsUserBinding
import com.vetpet.petbeats.ui.home_user.news.adapter.AdapterNews
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class NewsUserFragment : Fragment() {
    private var _binding: FragmentNewsUserBinding?= null
    private val binding get() = _binding!!
    private lateinit var adapter: AdapterNews
    private val viewModel: NewsUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewsUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListNews()

        binding.recycle.layoutManager = LinearLayoutManager(requireContext())
        binding.recycle.adapter = adapter


        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickListNews() {
        adapter = AdapterNews { id ->
            viewModel.itemClickNews(id)
        }
    }

    private fun setOnClick() {

    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.submitList(state.listNews)
                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when(event) {
                        is NewsUserEvent.NavigaitonNewsUser -> {
                            findNavController().navigate(
                                R.id.newsDetailUserFragment,
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