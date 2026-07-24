package com.vetpet.petbeats.ui.home_clinic.schedulelist

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentScheduleListBinding
import com.example.VetPet.databinding.LayoutPopupDialogBinding
import com.example.VetPet.databinding.LayoutPopupReasonBinding
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.adapter.AppointmentRefuseAdapter
import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.adapter.AppointmentWaitAdapter
import kotlinx.coroutines.launch
import kotlin.getValue


class ScheduleListFragment : Fragment() {
    private var _binding: FragmentScheduleListBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapterWait: AppointmentWaitAdapter
    private lateinit var adapterRefuseReceive: AppointmentRefuseAdapter
    private val viewModel: ScheduleListViewModel by viewModels {
        ScheduleListViewModelFactory(
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
        _binding = FragmentScheduleListBinding.inflate(inflater, container, false)
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
        adapterWait = AppointmentWaitAdapter(
            onDetailClick = { id -> viewModel.itemDetailClick(id) },

            onRefuseClick = { id ->
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
                ) },


            onReceiveClick = { id ->
                showPopupDialog(
                    message = "Tiếp nhận lịch khám?",
                    leftButton = "Quay lại",
                    rightButton = "Tiếp nhận",
                    onRightButtonClick = {
                        viewModel.itemReceiveClick(id)
                    }
                ) },
        )

        adapterRefuseReceive = AppointmentRefuseAdapter { id ->
            viewModel.itemReceiveClick(id)
        }
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener {
            viewModel.appointmentScheduleClick()
        }


        binding.btnWait.setOnClickListener {
            viewModel.onWaitClick()
        }
        binding.btnRefuse.setOnClickListener {
            viewModel.onRefuseClick()
        }
        binding.btnReceive.setOnClickListener {
            viewModel.onReceiveClick()
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

                    if (state.isWait) {
                        binding.btnWait.setBackgroundResource(R.drawable.button_wait)
                        binding.linePending.visibility = View.VISIBLE

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorTextWait)
                        binding.btnWait.setTextColor(clinic)


                        binding.recycle.adapter = adapterWait
                        adapterWait.submitList(state.listAppointmentChild)


                        viewModel.onAppointmentWaitList()
                    }
                    else {
                        binding.btnWait.setBackgroundResource(R.color.colorBackground)
                        binding.linePending.visibility = View.GONE

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                        binding.btnWait.setTextColor(clinic)
                    }

                    if (state.isRefuse) {
                        binding.btnRefuse.setBackgroundResource(R.drawable.button_refuse)
                        binding.lineRefuse.visibility = View.VISIBLE

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorTextRefuse)
                        binding.btnRefuse.setTextColor(clinic)


                        binding.recycle.adapter = adapterRefuseReceive
                        adapterRefuseReceive.submitList(state.listAppointmentChild)


                        viewModel.onAppointmentRefuseList()
                    }
                    else {
                        binding.btnRefuse.setBackgroundResource(R.color.colorBackground)
                        binding.lineRefuse.visibility = View.GONE

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                        binding.btnRefuse.setTextColor(clinic)
                    }

                    if (state.isReceive) {
                        binding.btnReceive.setBackgroundResource(R.drawable.button_receive)
                        binding.lineReceive.visibility = View.VISIBLE

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorTextReceive)
                        binding.btnReceive.setTextColor(clinic)


                        binding.recycle.adapter = adapterRefuseReceive
                        adapterRefuseReceive.submitList(state.listAppointmentChild)


                        viewModel.onAppointmentReceiveList()
                    }
                    else {
                        binding.btnReceive.setBackgroundResource(R.color.colorBackground)
                        binding.lineReceive.visibility = View.GONE

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                        binding.btnReceive.setTextColor(clinic)
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
                        is ScheduleListEvent.NavigationAppointmentSchedule -> {
                            findNavController().navigate(R.id.scheduleList_appointmentSchedule)
                        }
                        is ScheduleListEvent.NavigationDetail -> {
                            findNavController().navigate(R.id.appointmentDetailWait)
                        }
                        is ScheduleListEvent.NavigationDetailReceive -> {
                            findNavController().navigate(R.id.appointmentDetailReceive)
                        }
                        is ScheduleListEvent.NavigationDetailRefuse -> {
                            findNavController().navigate(R.id.appointmentDetailRefuse)
                        }
                    }
                }
            }
        }
    }


}