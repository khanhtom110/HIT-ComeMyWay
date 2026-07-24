package com.vetpet.petbeats.ui.home_clinic.informationclinic

import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
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
import com.example.VetPet.databinding.ItemCustomServiceBinding
import com.google.android.material.chip.Chip
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import com.vetpet.petbeats.ui.home_user.calendar.adapter.TimePagerAdapter
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
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

            val file = createMultipartFromUri(requireContext(), uri)
            if (file != null) {
                viewModel.onUploadImage(file)
            }
        }
        else {
            return@registerForActivityResult
        }
    }

    private fun createMultipartFromUri(context: Context, uri: Uri): MultipartBody.Part? {
        val inputStream = context.contentResolver.getType(uri) ?: "image/*"
        val tempFile = File(context.cacheDir, "clinic_avatar.jpg")

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        } ?: return null

        val mediaType = MediaType.parse(inputStream)
        val requestBody = RequestBody.create(mediaType, tempFile)

        return MultipartBody.Part.createFormData("file", tempFile.name, requestBody)
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


        binding.btnOther.setOnClickListener {
            viewModel.onOtherClinic()
        }
        binding.btnAddRow.setOnClickListener {
            viewModel.onAddRowClick()
        }
        binding.btnCancel.setOnClickListener {
            viewModel.onCancelClick()
        }
        binding.btnAdd.setOnClickListener {
            val newServices = mutableListOf<String>()
            for (i in 0 until binding.containInput.childCount) {
                val rowView = binding.containInput.getChildAt(i)
                val rowBinding = ItemCustomServiceBinding.bind(rowView)
                val input = rowBinding.editCustomService.text.toString().trim()

                if (input.isNotEmpty()) {
                    newServices.add(input)
                }
            }
            viewModel.onSubmitClick(newServices)
        }
    }


    private fun createInputRowView(index: Int): View {
        val rowBinding = ItemCustomServiceBinding.inflate(
            layoutInflater,
            binding.containInput,
            false
        )
        rowBinding.editCustomService.hint = "Dịch vụ $index"

        rowBinding.tvDelete.setOnClickListener {
            viewModel.onRemoteRowClick()
        }
        return rowBinding.root
    }
    private fun addChipToGroup(state: InformationClinicState, serviceName: String, isChecked: Boolean = false) {
        //nền thay đổi theo click
        val groundColor = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked), // đang chọn
                intArrayOf(-android.R.attr.state_checked) //bình thường
            ),
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary), //đã click
                ContextCompat.getColor(requireContext(), R.color.colorBackground) //chưa click
            )
        )

        //màu text thay đổi theo click
        val textColors = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.colorBackground), //đã click
                ContextCompat.getColor(requireContext(), R.color.colorPrimary) //chưa click
            )
        )

        //màu viền thay đổi theo click
        val strokeColorState = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary), //đã click
                ContextCompat.getColor(requireContext(), R.color.colorPrimary)  //chưa click
            )
        )


        if (binding.btnService.isEmpty() && state.services.isNotEmpty()) {
            state.services.forEach { serviceName ->
                val chip = Chip(requireContext()).apply {
                    text = serviceName

                    //cho phép bấm chọn
                    isClickable = true
                    isCheckable = true
                    chipStrokeWidth = 3f

                    // Chỉ việc gọi lại biến đã tạo ở trên, không khởi tạo lại
                    chipBackgroundColor = groundColor
                    setTextColor(textColors)
                    chipStrokeColor = strokeColorState

                    //người dùng click để báo về viewmodel
                    setOnCheckedChangeListener { button, isChecked ->
                        if (isChecked) {
                            viewModel.onServiceOpen(serviceName)
                        }
                        else {
                            viewModel.onServiceClose(serviceName)
                        }
                    }
                }
                binding.btnService.addView(chip)
            }
        }
        if (!binding.btnService.isEmpty()) {
            for (i in 0 until binding.btnService.childCount) {
                val chip = binding.btnService.getChildAt(i) as? com.google.android.material.chip.Chip
                if (chip != null) {
                    chip.isChecked = state.selectService.contains(chip.text.toString())
                }
            }
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


                    //service
                    if (state.isFormVisible) {
                        binding.layoutCustomService.visibility = View.VISIBLE
                        binding.btnOther.visibility = View.GONE
                    } else {
                        binding.layoutCustomService.visibility = View.GONE
                        binding.btnOther.visibility = View.VISIBLE
                    }

                    while (binding.containInput.childCount < state.inputCount) {
                        val nextIndex = binding.containInput.childCount + 1
                        binding.containInput.addView(createInputRowView(nextIndex))
                    }
                    while (binding.containInput.childCount > state.inputCount) {
                        binding.containInput.removeViewAt(binding.containInput.childCount - 1)
                    }

                    binding.btnService.removeAllViews()
                    state.services.forEach { serviceName ->
                        addChipToGroup(state, serviceName, isChecked = true)
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