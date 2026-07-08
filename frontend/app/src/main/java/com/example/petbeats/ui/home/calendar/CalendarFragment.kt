package com.example.petbeats.ui.home.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.petbeats.R
import com.example.petbeats.data.remote.api.ApiHome
import com.example.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.databinding.FragmentCalendarBinding
import kotlinx.coroutines.launch


class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: CalendarViewModel by viewModels {
        CalendarViewModelFactory(
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
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            viewModel.InformationClick()
        }


        binding.tvInputName.addTextChangedListener {
            viewModel.onNameChange(it.toString())
        }
        binding.tvInputPhone.addTextChangedListener {
            viewModel.onPhoneChange(it.toString())
        }
        binding.tvInputAddress.addTextChangedListener {
            viewModel.onAddressChange(it.toString())
        }
        binding.tvInputQuantity.addTextChangedListener {
            viewModel.onQuantityChange(it.toString())
        }
        binding.tvInputOther.addTextChangedListener {
            viewModel.onOtherChange(it.toString())
        }


        binding.btnBooking.setOnClickListener {
            viewModel.onCalendarClick()
        }


        binding.btnDog.setOnClickListener {
            viewModel.onDogClick()
        }
        binding.btnCat.setOnClickListener {
            viewModel.onCatClick()
        }
        binding.btnOther.setOnClickListener {
            viewModel.onOtherClick()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->

                    //check information
                    if (state.isInformation) {
                        binding.boxInformation.setBackgroundResource(R.drawable.button_input_errol)
                        binding.tvInformationError.visibility = View.VISIBLE
                    }
                    else {
                        binding.boxInformation.setBackgroundResource(R.drawable.ground_information)
                        binding.tvInformationError.visibility = View.GONE
                    }


                    if (binding.tvInformationError.text.toString() != state.informationError) {
                        binding.tvInformationError.text = state.informationError
                    }


                    //check state input
                    if (state.isName) {
                        binding.tvInputName.setBackgroundResource(R.drawable.button_input)
                    }
                    else {
                        binding.tvInputName.setBackgroundResource(R.drawable.ground_information)
                    }
                    if (state.isPhone) {
                        binding.tvInputPhone.setBackgroundResource(R.drawable.button_input)
                    }
                    else {
                        binding.tvInputPhone.setBackgroundResource(R.drawable.ground_information)
                    }
                    if (state.isAddress) {
                        binding.tvInputAddress.setBackgroundResource(R.drawable.button_input)
                    }
                    else {
                        binding.tvInputAddress.setBackgroundResource(R.drawable.ground_information)
                    }
                    if (state.isQuantity) {
                        binding.tvInputQuantity.setBackgroundResource(R.drawable.button_input)
                    }
                    else {
                        binding.tvInputQuantity.setBackgroundResource(R.drawable.ground_information)
                    }
                    if (state.isInputOther) {
                        binding.tvInputOther.setBackgroundResource(R.drawable.button_input)
                    }
                    else {
                        binding.tvInputOther.setBackgroundResource(R.drawable.ground_information)
                    }



                    //check input
                    if (binding.tvInputName.text.toString() != state.name) {
                        binding.tvInputName.setText(state.name)
                    }
                    if (binding.tvInputPhone.text.toString() != state.phone) {
                        binding.tvInputPhone.setText(state.phone)
                    }
                    if (binding.tvInputAddress.text.toString() != state.address) {
                        binding.tvInputAddress.setText(state.address)
                    }
                    if (binding.tvInputQuantity.text.toString() != state.quantity) {
                        binding.tvInputQuantity.setText(state.quantity)
                    }
                    if (binding.tvInputOther.text.toString() != state.other) {
                        binding.tvInputOther.setText(state.other)
                    }


                    //check type
                    if (state.isDog) {
                        binding.btnDog.setBackgroundResource(R.drawable.icon_open)
                    }
                    else {
                        binding.btnDog.setBackgroundResource(R.drawable.icon_close)
                    }
                    if (state.isCat) {
                        binding.btnCat.setBackgroundResource(R.drawable.icon_open)
                    }
                    else {
                        binding.btnCat.setBackgroundResource(R.drawable.icon_close)
                    }
                    if (state.isOther) {
                        binding.btnOther.setBackgroundResource(R.drawable.icon_open)
                        binding.tvTextOther.visibility = View.VISIBLE
                        binding.tvInputOther.visibility = View.VISIBLE
                    }
                    else {
                        binding.btnOther.setBackgroundResource(R.drawable.icon_close)
                        binding.tvTextOther.visibility = View.GONE
                        binding.tvInputOther.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { state ->
                    when (state) {
                        is CalendarEvent.NavigationInformationRoom -> {
                            findNavController().navigate(R.id.calendar_informationRoom)
                        }
                    }
                }
            }
        }
    }
}