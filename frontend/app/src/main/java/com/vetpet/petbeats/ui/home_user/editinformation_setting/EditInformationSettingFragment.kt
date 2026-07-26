package com.vetpet.petbeats.ui.home_user.editinformation_setting

import android.content.Context
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentEditInformationSettingBinding
import com.example.VetPet.databinding.FragmentEditPasswordSettingBinding
import com.vetpet.petbeats.data.remote.api.ApiUserHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.editpassword_setting.EditPasswordSettingViewModel
import com.vetpet.petbeats.ui.home_user.editpassword_setting.EditPasswordSettingViewModelFactory
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import kotlin.getValue


class EditInformationSettingFragment : Fragment() {
    private var _binding: FragmentEditInformationSettingBinding?= null
    private val binding get() = _binding!!
    private val viewModel: EditInformationSettingViewModel by viewModels {
        EditInformationSettingViewModelFactory(
            HomeUserRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiUserHome::class.java)
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
        _binding = FragmentEditInformationSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onProfile()

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

    private fun setOnClick() {
        binding.btnBack.setOnClickListener {
            viewModel.settingClick()
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
        binding.tvInputEmail.addTextChangedListener {
            viewModel.onEmailChange(it.toString())
        }



        binding.btnUploadImage.setOnClickListener {
            openGallery()
        }
        binding.imgLibrary.setOnClickListener {
            openGallery()
        }

        val id = arguments?.getInt("id") ?: 0
        binding.btnUpdate.setOnClickListener {
            viewModel.onProfile(id)
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {  state ->

                    //check input
                    if (state.isAddress) {
                        binding.tvInputAddress.setBackgroundResource(R.drawable.button_input_errol)
                    }
                    else {
                        binding.tvInputAddress.setBackgroundResource(R.drawable.tittle_search_blue)
                    }



                    //check error
                    if (state.isName) {
                        binding.tvInputName.setBackgroundResource(R.drawable.button_input_errol)
                        binding.tvNameError.visibility = View.VISIBLE

                        val nameError = ContextCompat.getColor(requireContext(),R.color.colorError)
                        binding.tvInputName.setTextColor(nameError)
                    }
                    else {
                        binding.tvInputName.setBackgroundResource(R.drawable.tittle_search_blue)
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
                        binding.tvInputPhone.setBackgroundResource(R.drawable.tittle_search_blue)
                        binding.tvPhoneError.visibility = View.GONE

                        val phoneSub = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.tvInputPhone.setTextColor(phoneSub)
                    }
                    binding.tvPhoneError.text = state.phoneError



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
                    if (binding.tvInputEmail.text.toString() != state.email) {
                        binding.tvInputEmail.text = state.email
                    }

                    if (state.image.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.image)
                            .into(binding.imgLibrary)
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
                        is EditInformationSettingEvent.NavigationSetting -> {
                            findNavController().navigate(R.id.editInformation_setting)
                        }
                    }
                }
            }
        }
    }

}