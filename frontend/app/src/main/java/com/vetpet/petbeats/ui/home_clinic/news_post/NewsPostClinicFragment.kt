package com.vetpet.petbeats.ui.home_clinic.news_post

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
import com.bumptech.glide.Glide
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentNewsPostClinicBinding
import com.example.VetPet.databinding.FragmentNewsUserBinding
import com.example.VetPet.databinding.LayoutPopupDialogBinding
import com.vetpet.petbeats.ui.home_user.news.NewsUserViewModel
import kotlinx.coroutines.launch
import kotlin.getValue


class NewsPostClinicFragment : Fragment() {
    private var _binding: FragmentNewsPostClinicBinding?= null
    private val binding get() = _binding!!
    private val viewModel: NewsPostClinicViewModel by viewModels()

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

        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.vector.setOnClickListener {
            viewModel.newsClinicClick()
        }


        binding.btnImgNews.setOnClickListener {
            viewModel.checkImageClickTrue()
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



        binding.btnPost.setOnClickListener {
            viewModel.onNewsPostClinicClick()
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
                    if (binding.editContent.toString() != state.content) {
                        binding.editContent.setText(state.content)
                    }



                    if (state.imageClinic.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.imageClinic)
                            .into(binding.imgClinic)
                    }
                    if (state.imageNews.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.imageNews)
                            .into(binding.imgNews)
                    }






                    if (state.imagePet) {
                        binding.btnCancelImgNews.visibility = View.VISIBLE
                        binding.btnImgNews.visibility = View.VISIBLE
                    }
                    else {
                        binding.btnCancelImgNews.visibility = View.GONE
                        binding.btnImgNews.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->

                }
            }
        }
    }
}