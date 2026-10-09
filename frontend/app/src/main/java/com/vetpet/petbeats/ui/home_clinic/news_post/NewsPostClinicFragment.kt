package com.vetpet.petbeats.ui.home_clinic.news_post

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentNewsPostClinicBinding
import com.example.VetPet.databinding.FragmentNewsUserBinding
import com.example.VetPet.databinding.LayoutPopupDialogBinding
import com.vetpet.petbeats.ui.home_user.news.NewsUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import kotlin.getValue

@AndroidEntryPoint
class NewsPostClinicFragment : Fragment() {
    private var _binding: FragmentNewsPostClinicBinding?= null
    private val binding get() = _binding!!
    private val viewModel: NewsPostClinicViewModel by viewModels()


    //Khởi tạo Photo Picker Launcher
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Glide.with(requireContext())
                .load(uri)
                .circleCrop()
                .into(binding.imgNews)

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
        _binding = FragmentNewsPostClinicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val id = arguments?.getInt("id") ?: 0
        viewModel.onImageClinic(id)


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
        binding.vector.setOnClickListener {
            viewModel.newsClinicClick()
        }


        binding.btnImgNews.setOnClickListener {
            openGallery()
        }
        binding.btnCancelImgNews.setOnClickListener {
            showPopupDialog(
                message = "Bạn có chắc chắn muốn huỷ lịch khám này không?",
                leftButton = "Bỏ",
                rightButton = "Tiếp tục",
                onLeftButtonClick = {
                    viewModel.checkImageClickFalse()
                }
            )
        }


        binding.editTitle.addTextChangedListener {
            viewModel.onTitleChange(it.toString())
        }
        binding.editContent.addTextChangedListener {
            viewModel.onContentChange(it.toString())
        }



        binding.btnPost.setOnClickListener {
            val id = arguments?.getInt("id") ?: 0
            viewModel.onNewsPostClinicClick(id)
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




    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.tvClinicName.text = state.nameClinic



                    if (binding.editTitle.text.toString() != state.title) {
                        binding.editTitle.setText(state.title)
                    }
                    if (binding.editContent.text.toString() != state.content) {
                        binding.editContent.setText(state.content)
                    }



                    if (state.imageClinic.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.imageClinic)
                            .circleCrop()
                            .into(binding.imgClinic)
                    }
                    if (state.imageNews.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.imageNews)
                            .into(binding.imgNews)
                    }




                    if (state.isImageNews) {
                        binding.btnCancelImgNews.visibility = View.VISIBLE
                        binding.imgNews.visibility = View.VISIBLE

                        binding.btnImgNews.isEnabled = false
                    }
                    else {
                        binding.btnCancelImgNews.visibility = View.GONE
                        binding.imgNews.visibility = View.GONE

                        binding.btnImgNews.isEnabled = true
                    }



                    if (binding.editTitle.text.toString() != state.title) {
                        binding.editTitle.setText(state.title)
                    }
                    if (binding.editContent.text.toString() != state.content) {
                        binding.editContent.setText(state.content)
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
                        is NewsPostClinicEvent.NavigationNewsClinic -> {
                            findNavController().navigate(R.id.newsPostClinic_newsClinic)
                        }
                        is NewsPostClinicEvent.NavigationNewsSuccessClinic -> {
                            findNavController().navigate(
                                R.id.newsPostClinic_newsSuccessClinic,
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