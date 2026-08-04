package com.vetpet.petbeats.ui.home_user.image_me_locket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentImageMeLocketBinding
import com.vetpet.petbeats.data.remote.api.ApiUserHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.image_me_locket.adapter.ImageLocketChild
import com.vetpet.petbeats.ui.home_user.image_me_locket.adapter.ImageMeLocketAdapter
import kotlinx.coroutines.launch
import kotlin.getValue


class ImageMeLocketFragment : Fragment() {
    private var _binding: FragmentImageMeLocketBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapter: ImageMeLocketAdapter
    private var currentImageLocket: ImageLocketChild? = null
    private val viewModel: ImageMeLocketViewModel by viewModels {
        ImageMeLocketViewModelFactory(
            HomeUserRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiUserHome::class.java)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentImageMeLocketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onMeLocketList()

        setupViewPager()
        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {

    }



    private fun setupViewPager() {
        adapter = ImageMeLocketAdapter()
        binding.imgLocket.adapter = adapter


        binding.imgLocket.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)


                currentImageLocket = adapter.currentList[position]


                // Nếu lướt đến cách bức ảnh cuối cùng 2 vị trí, gọi API tải thêm
                val totalItemCount = adapter.itemCount
                if (position >= totalItemCount - 2) {
                    viewModel.onMeLocketList()
                }
            }
        })
    }



    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.submitList(state.listMeLocket)
                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->

                }
            }
        }
    }
}