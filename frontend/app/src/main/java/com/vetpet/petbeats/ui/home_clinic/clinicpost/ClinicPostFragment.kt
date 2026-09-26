package com.vetpet.petbeats.ui.home_clinic.clinicpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.VetPet.databinding.FragmentClinicPostBinding
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.CreateClinicPostRequest
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class ClinicPostFragment : Fragment() {
    private var _binding: FragmentClinicPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClinicPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnPublish.setOnClickListener { publish() }
    }

    private fun publish() {
        val title = binding.inputTitle.text.toString().trim()
        val content = binding.inputContent.text.toString().trim()
        binding.inputTitle.error = when {
            title.isEmpty() -> "Nhập tiêu đề"
            title.length > 200 -> "Tiêu đề tối đa 200 ký tự"
            else -> null
        }
        binding.inputContent.error = when {
            content.isEmpty() -> "Nhập nội dung"
            content.length > 10000 -> "Nội dung tối đa 10000 ký tự"
            else -> null
        }
        if (binding.inputTitle.error != null || binding.inputContent.error != null) return

        binding.btnPublish.isEnabled = false
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val api = RetrofitInstance.getAuthRetrofit(requireContext())
                    .create(ApiClinicHome::class.java)
                val response = api.createClinicPost(CreateClinicPostRequest(title, content))
                if (response.statusCode == 201 && response.data != null) {
                    binding.inputTitle.text?.clear()
                    binding.inputContent.text?.clear()
                    Toast.makeText(requireContext(), "Đăng tin thành công", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                Toast.makeText(requireContext(), "Không thể đăng tin. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
            } finally {
                _binding?.btnPublish?.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
