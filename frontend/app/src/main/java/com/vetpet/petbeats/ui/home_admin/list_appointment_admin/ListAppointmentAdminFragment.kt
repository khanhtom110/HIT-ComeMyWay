package com.vetpet.petbeats.ui.home_admin.list_appointment_admin

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentListAppointmentAdminBinding
import com.example.VetPet.databinding.LayoutPopupClinicActionBinding
import com.vetpet.petbeats.core.utils.AnimationUtils.crossFadeShimmerToContent
import com.vetpet.petbeats.core.utils.AnimationUtils.fadeIn
import com.vetpet.petbeats.ui.home_admin.list_appointment_admin.adapter.AppointmentAdminCurrentAdapter
import com.vetpet.petbeats.ui.home_admin.list_appointment_admin.adapter.AppointmentAdminNotCurrentAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class ListAppointmentAdminFragment : Fragment() {
    private var _binding: FragmentListAppointmentAdminBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapterCurrent: AppointmentAdminCurrentAdapter
    private lateinit var adapterNotCurrent: AppointmentAdminNotCurrentAdapter
    private val viewModel: ListAppointmentAdminViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListAppointmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickList()

        binding.recycle.layoutManager = LinearLayoutManager(requireContext())


        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun clickList() {
        adapterCurrent = AppointmentAdminCurrentAdapter(
            onSettingClick = { id, view ->
                when {
                    viewModel.state.value.isReceive -> {
                        showPopupClinic(
                            anchorView = view,
                            clinicId = id,
                            onDeleteClick = { id ->
                                viewModel.itemDeleteAccountClick(id)
                            },
                            onLockClick = { id ->
                                viewModel.itemLockAccountClick(id)
                            }
                        )
                    }
                }
            }
        )

        adapterCurrent = AppointmentAdminCurrentAdapter(
            onSettingClick = { id, view ->
                when {
                    viewModel.state.value.isWait -> {
                        showPopupClinic(
                            anchorView = view,
                            clinicId = id,
                            onDeleteClick = { id ->
                                viewModel.itemDeleteAccountClick(id)
                            },
                            onLockClick = { id ->
                                viewModel.itemLockAccountClick(id)
                            }
                        )
                    }
                }
            }
        )
    }


    private fun setOnClick() {
        binding.btnAddAccount.setOnClickListener {
            viewModel.addClinicClick()
        }



        binding.btnReceive.setOnClickListener {
            viewModel.onReceiveClick()
        }
        binding.btnWait.setOnClickListener {
            viewModel.onWaitClick()
        }
    }

    private fun showPopupClinic(
        anchorView: View,
        clinicId: Int,
        onDeleteClick: (Int) -> Unit,
        onLockClick: (Int) -> Unit
    ) {

        val popupBinding = LayoutPopupClinicActionBinding.inflate(layoutInflater)

        val popupWindow = PopupWindow(
            popupBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Background trong suốt
        popupWindow.setBackgroundDrawable(
            Color.TRANSPARENT.toDrawable()
        )

        // Cho phép bấm ra ngoài để đóng popup
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        // Xóa tài khoản
        popupBinding.tvDelete.setOnClickListener {
            popupWindow.dismiss()

            onDeleteClick(clinicId)
        }

        // Khóa tài khoản
        popupBinding.tvLock.setOnClickListener {
            popupWindow.dismiss()

            onLockClick(clinicId)
        }

        // Hiện popup ngay dưới dấu 3 chấm
        popupWindow.showAsDropDown(
            anchorView,
            -140,
            -10
        )
    }


    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state.isReceive) {
                        binding.btnReceive.setBackgroundResource(R.drawable.button_receive)
                        binding.lineReceive.visibility = View.VISIBLE

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorTextReceive)
                        binding.btnReceive.setTextColor(clinic)

                        binding.recycle.adapter = adapterCurrent
                    }
                    else {
                        binding.btnReceive.setBackgroundResource(R.color.colorBackground)
                        binding.lineReceive.visibility = View.GONE

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                        binding.btnReceive.setTextColor(clinic)
                    }

                    if (state.isWait) {
                        binding.btnWait.setBackgroundResource(R.drawable.button_wait)
                        binding.lineWait.visibility = View.VISIBLE

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorTextWait)
                        binding.btnWait.setTextColor(clinic)


                        binding.recycle.adapter = adapterNotCurrent
                    }
                    else {
                        binding.btnWait.setBackgroundResource(R.color.colorBackground)
                        binding.lineWait.visibility = View.GONE

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                        binding.btnWait.setTextColor(clinic)
                    }



                    if (state.isLoading) {
                        //Xóa animation cũ đang chạy dở
                        binding.shimmerFrameLayout.clearAnimation()
                        binding.recycle.clearAnimation()
                        binding.tvEmpty.clearAnimation()

                        //Trả lại tọa độ hiển thị
                        binding.shimmerFrameLayout.alpha = 1f
                        binding.recycle.alpha = 1f

                        //ẩn hiện
                        binding.shimmerFrameLayout.startShimmer()
                        binding.shimmerFrameLayout.visibility = View.VISIBLE
                        binding.recycle.visibility = View.GONE
                        binding.tvEmpty.visibility = View.GONE
                    }
                    else {
                        binding.shimmerFrameLayout.stopShimmer()

                        //Kiểm tra xem list có data không
                        if (state.listAppointmentAdmin.isEmpty()) {
                            binding.shimmerFrameLayout.visibility = View.GONE
                            binding.recycle.visibility = View.GONE
                            binding.tvEmpty.fadeIn()
                        } else {
                            binding.tvEmpty.visibility = View.GONE
                            crossFadeShimmerToContent(binding.shimmerFrameLayout, binding.recycle)
                        }
                    }

                    if (!state.isLoading) {
                        if (state.isReceive) {
                            adapterCurrent.submitList(state.listAppointmentAdmin)
                        }
                        else {
                            adapterNotCurrent.submitList(state.listAppointmentAdmin)
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
                        is ListAppointmentAdminEvent.NavigationAddClinic -> {
                            findNavController().navigate(R.id.addClinicAdminFragment)
                        }
                    }
                }
            }
        }
    }

}