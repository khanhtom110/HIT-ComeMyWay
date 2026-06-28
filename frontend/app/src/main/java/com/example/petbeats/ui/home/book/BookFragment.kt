package com.example.petbeats.ui.home.book

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petbeats.R
import com.example.petbeats.core.base.PermissionHelper
import com.example.petbeats.databinding.FragmentBookBinding
import com.example.petbeats.ui.home.book.adapter.BookAdapter
import kotlinx.coroutines.launch
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class BookFragment : Fragment() {
    private var _binding: FragmentBookBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapter: BookAdapter
    private val viewModel: BookViewModel by viewModels()


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        when {
            permission.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                Toast.makeText(requireContext(), "Đã cấp quyền GPS", Toast.LENGTH_SHORT).show()
                getUserLocationAndSearch()
            }
            permission.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                Toast.makeText(requireContext(), "Đã cấp quyền vị trí tương đối", Toast.LENGTH_SHORT).show()
                getUserLocationAndSearch()
            }
            else -> {
                Toast.makeText(requireContext(), "Bạn đã từ chối cấp quyền!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookAdapter()


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermissionAndStartSearch()

        setOnClick()


        binding.recycle.layoutManager = LinearLayoutManager(requireContext())
        binding.recycle.adapter = adapter

        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkLocationPermissionAndStartSearch() {

        if (PermissionHelper.hasLocationPermission(requireContext())) {
            getUserLocationAndSearch()
        }
        else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocationAndSearch() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLat = location.latitude
                val userLng = location.longitude

                Toast.makeText(requireContext(), "Tọa độ: $userLat, $userLng", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(requireContext(), "Vui lòng bật GPS trên điện thoại", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setOnClick() {
        binding.search.setOnClickListener {
            viewModel.searchClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.submitList(state.list)
                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is BookEvent.NavigationSearch -> {
                            findNavController().navigate(R.id.searchFragment)
                        }
                    }
                }
            }
        }
    }
}