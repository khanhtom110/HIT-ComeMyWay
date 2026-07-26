package com.vetpet.petbeats.ui.home_clinic.appointmentdetail_wait

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentAppointmentDetailBinding
import com.example.VetPet.databinding.LayoutPopupDialogBinding
import com.example.VetPet.databinding.LayoutPopupReasonBinding
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import com.vetpet.petbeats.ui.home_clinic.appointmentdetail_receive.AppointmentDetailReceiveEvent
import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.AppointmentScheduleEvent
import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.AppointmentScheduleViewModelFactory
import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.adapter.AppointmentReceiveAdapter
import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.adapter.AppointmentRefuseAdapter
import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.adapter.AppointmentWaitAdapter
import kotlinx.coroutines.launch
import kotlin.getValue


class AppointmentDetailWaitFragment : Fragment() {
    private var _binding: FragmentAppointmentDetailBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: AppointmentDetailWaitViewModel by viewModels {
        AppointmentDetailWaitViewModelFactory(
            HomeClinicRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiClinicHome::class.java)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAppointmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("id") ?: 0
        viewModel.onAppointmentDetail(id)

        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setOnClick() {
        val id = arguments?.getInt("id") ?: 0

        binding.btnBack.setOnClickListener {
            viewModel.appointmentScheduleClick()
        }
        binding.btnRefuse.setOnClickListener {
            showPopupDialog(
                message = "Từ chối lịch khám?",
                leftButton = "Quay lại",
                rightButton = "Từ chối",
                onRightButtonClick = {
                    showPopupReason(
                        leftButton = "Quay lại",
                        rightButton = "Từ chối",
                        onRightButtonClick = { reason ->
                            viewModel.itemRefuseClick(id, reason)
                        }
                    )
                }
            )
        }
        binding.btnReceive.setOnClickListener {
            showPopupDialog(
                message = "Tiếp nhận lịch khám?",
                leftButton = "Quay lại",
                rightButton = "Tiếp nhận",
                onRightButtonClick = {
                    viewModel.itemReceiveClick(id)
                }
            )
        }
    }

    private fun showPopupDialog(
        message: String,
        leftButton: String,
        rightButton: String,

        onLeftButtonClick: (() -> Unit)? = null,
        onRightButtonClick: (() -> Unit)? = null
    ) {
        //Khởi tạo binding
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogBinding = LayoutPopupDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        //Xử lý giao diện
        dialogBinding.tvDialogTitle.text = message
        dialogBinding.btnLeft.text = leftButton
        dialogBinding.btnRight.text = rightButton

        dialogBinding.btnLeft.setOnClickListener {
            dialog.dismiss()
            onLeftButtonClick?.invoke()
        }

        dialogBinding.btnRight.setOnClickListener {
            dialog.dismiss()
            onRightButtonClick?.invoke()
        }

        dialog.show()
    }

    private fun showPopupReason(
        leftButton: String,
        rightButton: String,

        onLeftButtonClick: (() -> Unit)? = null,
        onRightButtonClick: ((String) -> Unit)? = null
    ) {
        //Khởi tạo binding
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogBinding = LayoutPopupReasonBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        //Xử lý giao diện
        dialogBinding.btnLeft.text = leftButton
        dialogBinding.btnRight.text = rightButton

        dialogBinding.btnLeft.setOnClickListener {
            dialog.dismiss()
            onLeftButtonClick?.invoke()
        }

        dialogBinding.btnRight.setOnClickListener {
            val reason = dialogBinding.tvDialogTitle.text.toString()

            dialog.dismiss()
            onRightButtonClick?.invoke(reason)
        }

        dialog.show()
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.numbDay.text = state.day
                    binding.numbTime.text = state.time
                    binding.numbQuantity.text = state.quantity.toString()
                    binding.numbService.text = state.bookingType
                    binding.numbOwner.text = state.fullName
                    binding.numbPhone.text = state.phone
                    binding.numbAddress.text = state.address
                    binding.numbState.text = state.state


                    binding.numbInclude.text = if (state.services.isEmpty()) {
                        "Không có dịch vụ"
                    }
                    else {
                        state.services.joinToString(separator = ", ") { serviceItem ->
                            serviceItem.name
                        }
                    }


                    if (state.imgPet.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.imgPet)
                            .into(binding.imgPet)
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
                        is AppointmentDetailWaitEvent.NavigationAppointmentSchedule -> {
                            findNavController().navigate(R.id.wait_appointmentSchedule)
                        }
                        is AppointmentDetailWaitEvent.NavigationDetailReceive -> {
                            findNavController().navigate(
                                R.id.appointmentDetailReceive,
                                Bundle().apply {
                                    putInt("id", event.id)
                                }
                            )
                        }
                        is AppointmentDetailWaitEvent.NavigationDetailRefuse -> {
                            findNavController().navigate(R.id.appointmentDetailRefuse,
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