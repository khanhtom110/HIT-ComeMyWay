package com.vetpet.petbeats.ui.home_clinic.schedulelist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentScheduleListBinding
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.adapter.AppointmentRefuseReceiveAdapter
import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.adapter.AppointmentWaitAdapter
import kotlinx.coroutines.launch
import kotlin.getValue


class ScheduleListFragment : Fragment() {
    private var _binding: FragmentScheduleListBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapterWait: AppointmentWaitAdapter
    private lateinit var adapterRefuseReceive: AppointmentRefuseReceiveAdapter
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
            onRefuseClick = { id -> viewModel.itemRefuseClick(id) },
            onReceiveClick = { id -> viewModel.itemReceiveClick(id) }
        )

        adapterRefuseReceive = AppointmentRefuseReceiveAdapter { id ->
            viewModel.itemDetailClick(id)
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
                    }
                }
            }
        }
    }


}