package com.example.petbeats.ui.home.confirmappointment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.VetPet.R
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.petbeats.data.remote.api.ApiHome
import com.example.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.example.petbeats.data.repository.HomeRepository
import com.example.VetPet.databinding.FragmentConfirmAppointmentBinding
import com.example.petbeats.ui.home.book.adapter.BookChildState
import kotlinx.coroutines.launch
import kotlin.getValue


class ConfirmAppointmentFragment : Fragment() {
    private var _binding: FragmentConfirmAppointmentBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: ConfirmAppointmentViewModel by viewModels {
        ConfirmAppointmentViewModelFactory(
            HomeRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiHome::class.java)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentConfirmAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("id") ?: 0
        val clinicId = arguments?.getInt("clinicId") ?: 0
        viewModel.onInformationBookingAPI(clinicId)
        viewModel.onInformationAppointment(id)

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
            viewModel.bookClick()
        }

        binding.btnEdit.setOnClickListener {
            val id = arguments?.getInt("id") ?: 0
            val clinicId = arguments?.getInt("clinicId") ?: 0

            viewModel.onEditAppointmentClick(id, clinicId)
        }

        binding.btnBooking.setOnClickListener {
            viewModel.searchClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.tvInputClinic.text = state.name
                    binding.tvClinicName.text = state.name
                    binding.tvInputAddressClinic.text = state.clinicAddress
                    binding.tvInputPhoneClinic.text = state.phoneClinic
                    binding.tvInputName.text = state.fullName
                    binding.tvInputPhoneHome.text = state.phoneUser
                    binding.tvInputAddressHome.text = state.homeAddress
                    binding.tvInputQuantityHome.text = state.petQuantity.toString()
                    binding.tvInputTypePetHome.text = state.petType
                    binding.tvInputStatusHome.text = state.petCondition
                    binding.tvInputTypeServiceHome.text = state.bookingType
                    binding.tvInputDayHome.text = state.appointmentDate
                    binding.tvInputTimeHome.text = state.appointmentTime.take(5)
                    binding.tvRating.text = "đánh giá ${state.rating}/5"


                    binding.tvInputIncludeHome.text = if (state.services.isEmpty()) {
                        "Không có dịch vụ"
                    }
                    else {
                        state.services.joinToString(separator = ", ") { serviceItem ->
                            serviceItem.name
                        }
                    }

                    //check state clinic
                    when (state.state) {
                        BookChildState.PENDING -> {
                            binding.stateClinic.text = "Chờ xử lý"
                            binding.stateClinic.setTextColor(Color.parseColor("#F7C120"))

                            //button edit
                            binding.btnEdit.isEnabled = true
                            binding.btnEdit.setBackgroundResource(R.drawable.button_auth_white)
                            binding.btnEdit.setTextColor(Color.parseColor("#486BF3"))
                        }
                        BookChildState.SUCCESS -> {
                            binding.stateClinic.text = "Đặt lịch thành công"
                            binding.stateClinic.setTextColor(Color.parseColor("#00FF0B"))

                            //button edit
                            binding.btnEdit.isEnabled = false
                            binding.btnEdit.setBackgroundResource(R.drawable.button_close)
                            binding.btnEdit.setTextColor(Color.parseColor("#FAFCFF"))
                        }
                        BookChildState.CANCELLED -> {
                            binding.stateClinic.text = "Từ chối"
                            binding.stateClinic.setTextColor(Color.parseColor("#CC0900"))

                            //button edit
                            binding.btnEdit.isEnabled = false
                            binding.btnEdit.setBackgroundResource(R.drawable.button_close)
                            binding.btnEdit.setTextColor(Color.parseColor("#FAFCFF"))
                        }
                    }

                    if (state.status) {
                        binding.tvStatus.text = "Đang hoạt động"
                    } else {
                        binding.tvStatus.text = "Không hoạt động"
                    }

                    if (state.thumbnailUrl.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.thumbnailUrl)
                            .into(binding.imgClinic)
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
                        is ConfirmAppointmentEvent.NavigationHomeAppointment -> {
                            findNavController().navigate(R.id.confirmAppointment_book)
                        }
                        is ConfirmAppointmentEvent.NavigationEditAppointment -> {
                            findNavController().navigate(
                                R.id.editCalendarFragment,
                                Bundle().apply {
                                    putInt("id", event.id)
                                    putInt("clinicId", event.clinicId)
                                }
                            )
                        }
                        is ConfirmAppointmentEvent.NavigationSearch -> {
                            findNavController().navigate(R.id.confirmAppointment_search)
                        }
                    }
                }
            }
        }
    }
}