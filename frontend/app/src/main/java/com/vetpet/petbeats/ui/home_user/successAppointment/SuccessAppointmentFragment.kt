package com.vetpet.petbeats.ui.home_user.successAppointment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.VetPet.R
import com.vetpet.petbeats.data.remote.api.ApiUserHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.example.VetPet.databinding.FragmentSuccessAppointmentBinding
import com.vetpet.petbeats.ui.home_user.book.adapter.BookChildState
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlin.getValue


class SuccessAppointmentFragment : Fragment() {
    private var _binding: FragmentSuccessAppointmentBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: SuccessAppointmentViewModel by viewModels {
        SuccessAppointmentViewModelFactory(
            HomeUserRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiUserHome::class.java)
            )
        )
    }

    //Khởi tạo launch xin quyền notify
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getAndSendFirebaseToken()
        }
        else {
            Toast.makeText(requireContext(), "Bạn đã từ chối quyền truy cập", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAndSendFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            val token = task.result

            Log.d("TOKEN_SEND", "token: $token")
            viewModel.sendDeviceToken(token)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSuccessAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Kiểm tra xem đã được cấp quyền chưa, có rồi thì tự động lấy token
        checkNotifyPermission()

        val clinicId = arguments?.getInt("clinicId") ?: 0
        val id = arguments?.getInt("id") ?: 0
        viewModel.onSuccessInformation(id, clinicId)

        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkNotifyPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { //tiramu: tên mã của android13
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getAndSendFirebaseToken()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }

                else -> {
                    requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
        else {
            getAndSendFirebaseToken()
        }
    }


    private fun setOnClick() {
        binding.btnNewBooking.setOnClickListener {
            viewModel.searchClick()
        }

        binding.btnHome.setOnClickListener {
            viewModel.bookingClick()
        }

        binding.btnDetail.setOnClickListener {
            val clinicId = arguments?.getInt("clinicId") ?: 0
            val id = arguments?.getInt("id") ?: 0

            viewModel.confirmClick(id, clinicId)
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {  state ->
                    if (binding.tvAddress.text.toString() != state.address) {
                        binding.tvAddress.text = state.address
                    }
                    if (binding.date.text.toString() != state.date) {
                        binding.date.text = state.date
                    }
                    if (binding.time.toString() != state.time) {
                        binding.time.text = state.time
                    }

                    //check state clinic
                    when (state.status) {
                        BookChildState.PENDING -> {
                            binding.stateClinic.text = "Chờ xử lý"
                            binding.stateClinic.setTextColor(Color.parseColor("#F7C120"))
                        }
                        BookChildState.CONFIRMED -> {
                            binding.stateClinic.text = "Đặt lịch thành công"
                            binding.stateClinic.setTextColor(Color.parseColor("#00FF0B"))
                        }
                        BookChildState.REJECTED -> {
                            binding.stateClinic.text = "Từ chối"
                            binding.stateClinic.setTextColor(Color.parseColor("#CC0900"))
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
                        is SuccessAppointmentEvent.NavigationSearch -> {
                            findNavController().navigate(R.id.successAppointment_search)
                        }
                        is SuccessAppointmentEvent.NavigationBooking -> {
                            findNavController().navigate(R.id.successAppointment_booking)
                        }
                        is SuccessAppointmentEvent.NavigationConfirm -> {
                            findNavController().navigate(
                                R.id.confirmAppointmentFragment,
                                Bundle().apply {
                                    putInt("id", event.id)
                                    putInt("clinicId", event.clinicId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

}