package com.example.petbeats.ui.home.informationroom

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.petbeats.R
import com.example.petbeats.data.remote.api.ApiHome
import com.example.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.databinding.FragmentBookBinding
import com.example.petbeats.databinding.FragmentInformationRoomBinding
import com.example.petbeats.databinding.FragmentResultSearchBinding
import com.example.petbeats.ui.home.book.BookViewModel
import com.example.petbeats.ui.home.book.BookViewModelFactory
import com.example.petbeats.ui.home.book.adapter.BookAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlin.getValue
import androidx.core.net.toUri
import androidx.core.graphics.toColorInt


class InformationRoomFragment : Fragment() {
    private var _binding: FragmentInformationRoomBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: InformationRoomViewModel by viewModels {
        InformationRoomViewModelFactory(
            HomeRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiHome::class.java)
            )
        )
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    private fun getUserLocationAndSearch() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLat = location.latitude
                val userLng = location.longitude

                viewModel.onLatiLong(userLat, userLng)
                val id = arguments?.getInt("id") ?: 0
                viewModel.onInformationList(id)
            } else {
                Toast.makeText(requireContext(), "Vui lòng bật GPS trên điện thoại", Toast.LENGTH_SHORT).show()

                val id = arguments?.getInt("id") ?: 0
                viewModel.onInformationList(id)
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show()

            val id = arguments?.getInt("id") ?: 0
            viewModel.onInformationList(id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInformationRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getUserLocationAndSearch()

        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener {
            viewModel.resultClick()
        }

        binding.btnBooking.setOnClickListener {
            val id = arguments?.getInt("id") ?: 0

            viewModel.calendarClick(id)
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    Log.d("UI_DEBUG", "Data arrived: ${state.name}")
                    binding.tvClinicName.text = state.name
                    binding.tvAddress.text = state.address
                    binding.tvOpenTime.text = state.openTime.take(5)
                    binding.tvCloseTime.text = state.closeTime.take(5)
                    binding.tvPhone.text = state.phone
                    binding.tvDescription.text = state.description
                    binding.tvRating.text = "Đánh giá: ${state.rating}/5"
                    binding.tvDistance.text = "${state.distance} km"

                    if (state.distance == 0.0) {
                        binding.tvDistance.visibility = View.GONE
                    }
                    else {
                        binding.tvDistance.visibility = View.VISIBLE
                    }


                    if (state.isOperating) {
                        binding.tvStatus.text = "Đang hoạt động"
                    } else {
                        binding.tvStatus.text = "Không hoạt động"
                    }

                    if (state.thumbnailUrl.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.thumbnailUrl)
                            .into(binding.imgClinic)
                    }

                    //Service
                    binding.service.removeAllViews()
                    state.services.forEach { serviceName ->
                        val chip = com.google.android.material.chip.Chip(requireContext()).apply {
                            text = serviceName
                            isClickable = false
                            isCheckable = false

                            chipStrokeWidth = 0f
                            setChipBackgroundColorResource(R.color.colorModes)
                            setTextColor("#486BF3".toColorInt())
                        }
                        binding.service.addView(chip)
                    }

                    //Đến đường dẫn link locate
                    binding.btnLocate.setOnClickListener {
                        val link = state.mapLink

                        if (!link.isEmpty()) {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, link.toUri())
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(requireContext(), "Không thể mở link này", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else {
                            Toast.makeText(requireContext(), "Đường dẫn không tồn tại", Toast.LENGTH_SHORT).show()
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
                    when (event) {
                        is InformationRoomEvent.NavigationResultSearch -> {
                            findNavController().popBackStack()
                        }
                        is InformationRoomEvent.NavigationCalendar -> {
                            findNavController().navigate(
                                R.id.calendarFragment,
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