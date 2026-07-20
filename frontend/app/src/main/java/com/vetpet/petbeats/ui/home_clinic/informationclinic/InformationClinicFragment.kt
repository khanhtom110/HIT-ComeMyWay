package com.vetpet.petbeats.ui.home_clinic.informationclinic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentInformationClinicBinding
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import com.vetpet.petbeats.ui.home_user.calendar.adapter.TimePagerAdapter
import kotlinx.coroutines.launch
import kotlin.getValue


class InformationClinicFragment : Fragment() {
    private var _binding: FragmentInformationClinicBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: InformationClinicViewModel by viewModels {
        InformationClinicViewModelFactory(
            HomeClinicRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiClinicHome::class.java)
            )
        )
    }

    //Khởi tạo Photo Picker Launcher
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Glide.with(requireContext())
                .load(uri)
                .circleCrop()
                .into(binding.imgLibrary)
        }
        else {
            return@registerForActivityResult
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInformationClinicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTime()
        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Lọc lấy ảnh
    private fun openGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private fun setupTime() {
        val hourList = (0..23).map {
            String.format("%02d", it)
        }
        val minuteList = listOf("00", "15", "30", "45")

        binding.vpOpenHour.apply {
            adapter = TimePagerAdapter(hourList)
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }
        binding.vpOpenMinute.apply {
            adapter = TimePagerAdapter(minuteList)
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }

        binding.vpCloseHour.apply {
            adapter = TimePagerAdapter(hourList)
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }
        binding.vpCloseMinute.apply {
            adapter = TimePagerAdapter(minuteList)
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }
    }

    private fun getSelectTime() {
        val openHourTime = binding.vpOpenHour.currentItem
        val openMinuteTime = binding.vpOpenMinute.currentItem

        val closeHourTime = binding.vpCloseHour.currentItem
        val closeMinuteTime = binding.vpCloseMinute.currentItem

        val openHour = String.format("%02d", openHourTime)
        val openMinute = when (openMinuteTime) {
            0 -> {
                "00"
            }
            1 -> {
                "15"
            }
            2 -> {
                "30"
            }
            else -> {
                "45"
            }
        }

        val closeHour = String.format("%02d", closeHourTime)
        val closeMinute = when (closeMinuteTime) {
            0 -> {
                "00"
            }
            1 -> {
                "15"
            }
            2 -> {
                "30"
            }
            else -> {
                "45"
            }
        }

        viewModel.onTimeOpenSelect(openHour, openMinute)
        viewModel.onTimeCloseSelect(closeHour, closeMinute)

    }

    private fun setOnClick() {
        binding.tvInputName.addTextChangedListener {
            viewModel.onNameChange(it.toString())
        }
        binding.tvInputPhone.addTextChangedListener {
            viewModel.onPhoneChange(it.toString())
        }
        binding.tvInputAddress.addTextChangedListener {
            viewModel.onAddressChange(it.toString())
        }
        binding.tvInputLink.addTextChangedListener {
            viewModel.onLinkChange(it.toString())
        }
        binding.tvInputState.addTextChangedListener {
            viewModel.onStateChange(it.toString())
        }



        binding.btnUpdate.setOnClickListener {
            getSelectTime()

            viewModel.onInformationClinicClick()
        }


        binding.btnInstall.setOnClickListener {
            openGallery()
        }
        binding.imgLibrary.setOnClickListener {
            openGallery()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->

                    //check input
                    if (state.isAddress) {
                        binding.tvInputAddress.setBackgroundResource(R.drawable.button_input_errol)
                    }
                    else {
                        binding.tvInputAddress.setBackgroundResource(R.drawable.ground_information)
                    }

                    //check error
                    if (state.isInformation) {
                        binding.boxInformation.setBackgroundResource(R.drawable.button_input_errol)
                        binding.tvInformationError.visibility = View.VISIBLE
                    }
                    else {
                        binding.boxInformation.setBackgroundResource(R.drawable.ground_information)
                        binding.tvInformationError.visibility = View.GONE
                    }
                    binding.tvInformationError.text = state.informationError

                    if (state.isName) {
                        binding.tvInputName.setBackgroundResource(R.drawable.button_input_errol)
                        binding.tvNameError.visibility = View.VISIBLE

                        val nameError = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.tvInputName.setTextColor(nameError)
                    }
                    else {
                        binding.tvInputName.setBackgroundResource(R.drawable.ground_information)
                        binding.tvNameError.visibility = View.GONE

                        val nameSub = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.tvInputName.setTextColor(nameSub)
                    }
                    binding.tvNameError.text = state.nameError

                    if (state.isPhone) {
                        binding.tvInputPhone.setBackgroundResource(R.drawable.button_input_errol)
                        binding.tvPhoneError.visibility = View.VISIBLE

                        val phoneError = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.tvInputPhone.setTextColor(phoneError)
                    }
                    else {
                        binding.tvInputPhone.setBackgroundResource(R.drawable.ground_information)
                        binding.tvPhoneError.visibility = View.GONE

                        val phoneSub = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.tvInputPhone.setTextColor(phoneSub)
                    }
                    binding.tvPhoneError.text = state.phoneError

                    if (state.isLink) {
                        binding.tvInputLink.setBackgroundResource(R.drawable.button_input_errol)
                        binding.tvLinkError.visibility = View.VISIBLE

                        val linkError = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.tvInputLink.setTextColor(linkError)
                    }
                    else {
                        binding.tvInputLink.setBackgroundResource(R.drawable.ground_information)
                        binding.tvLinkError.visibility = View.GONE

                        val linkSub = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.tvInputLink.setTextColor(linkSub)
                    }
                    binding.tvLinkError.text = state.linkError

                    if (state.isTime) {
                        binding.layoutOpenTime.setBackgroundResource(R.drawable.button_input_errol)
                        binding.layoutCloseTime.setBackgroundResource(R.drawable.button_input_errol)
                        binding.tvTimeError.visibility = View.VISIBLE
                    }
                    else {
                        binding.layoutOpenTime.setBackgroundResource(R.drawable.ground_information)
                        binding.layoutCloseTime.setBackgroundResource(R.drawable.ground_information)
                        binding.tvTimeError.visibility = View.GONE
                    }

                    if (state.isService) {
                        binding.boxService.setBackgroundResource(R.drawable.button_input_errol)
                        binding.tvServiceError.visibility = View.VISIBLE
                    }
                    else {
                        binding.boxService.setBackgroundResource(R.drawable.ground_information)
                        binding.tvServiceError.visibility = View.GONE
                    }
                    binding.tvServiceError.text = state.serviceError





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
                    if (binding.tvInputLink.text.toString() != state.link) {
                        binding.tvInputLink.setText(state.link)
                    }
                    if (binding.tvInputState.text.toString() != state.state) {
                        binding.tvInputState.setText(state.state)
                    }
                    if (binding.tvInputLink.text.toString() != state.link) {
                        binding.tvInputLink.setText(state.link)
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
                        is InformationClinicEvent.NavigationInformationSuccess -> {
                            findNavController().navigate(R.id.informationClinicSuccessFragment)
                        }
                    }
                }
            }
        }
    }


}