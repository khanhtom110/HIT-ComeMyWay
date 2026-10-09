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
            onSettingClick = { id, anchorView ->
                showPopupClinic(
                    anchorView = anchorView,
                    clinicId = id,
                    onDeleteClick = { clinicId ->
                        viewModel.itemDeleteAccountClick(clinicId)
                    },
                    onLockClick = { clinicId ->
                        viewModel.itemLockAccountClick(clinicId)
                    }
                )
            }
        )

        adapterNotCurrent = AppointmentAdminNotCurrentAdapter(
            onSettingClick = { id, anchorView ->
                showPopupClinic(
                    anchorView = anchorView,
                    clinicId = id,
                    onDeleteClick = { clinicId ->
                        viewModel.itemDeleteAccountClick(clinicId)
                    },
                    onLockClick = { clinicId ->
                        viewModel.itemLockAccountClick(clinicId)
                    }
                )
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    // Tab đang hoạt động
                    binding.btnReceive.setBackgroundResource(
                        if (state.isReceive) {
                            R.drawable.button_receive
                        } else {
                            R.color.colorBackground
                        }
                    )

                    binding.btnReceive.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            if (state.isReceive) {
                                R.color.colorTextReceive
                            } else {
                                R.color.colorPrimary
                            }
                        )
                    )

                    binding.lineReceive.visibility = if (state.isReceive) View.VISIBLE else View.GONE

                    // Tab chưa hoạt động
                    binding.btnWait.setBackgroundResource(
                        if (state.isWait) {
                            R.drawable.button_wait
                        } else {
                            R.color.colorBackground
                        }
                    )

                    binding.btnWait.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            if (state.isWait) {
                                R.color.colorTextWait
                            } else {
                                R.color.colorPrimary
                            }
                        )
                    )

                    binding.lineWait.visibility = if (state.isWait) View.VISIBLE else View.GONE

                    // Chọn adapter và cập nhật danh sách
                    if (state.isReceive) {
                        if (binding.recycle.adapter !== adapterCurrent) {
                            binding.recycle.adapter = adapterCurrent
                        }

                        adapterCurrent.submitList(state.listAppointmentAdmin)
                    } else {
                        if (binding.recycle.adapter !== adapterNotCurrent) {
                            binding.recycle.adapter = adapterNotCurrent
                        }

                        adapterNotCurrent.submitList(state.listAppointmentAdmin)
                    }

                    // Hiển thị loading, danh sách hoặc thông báo trống
                    if (state.isLoading) {
                        binding.shimmerFrameLayout.visibility = View.VISIBLE
                        binding.shimmerFrameLayout.startShimmer()

                        binding.recycle.visibility = View.GONE
                        binding.tvEmpty.visibility = View.GONE
                    } else {
                        binding.shimmerFrameLayout.stopShimmer()
                        binding.shimmerFrameLayout.visibility = View.GONE

                        val isEmpty = state.listAppointmentAdmin.isEmpty()

                        binding.recycle.visibility = if (isEmpty) View.GONE else View.VISIBLE
                        binding.tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
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