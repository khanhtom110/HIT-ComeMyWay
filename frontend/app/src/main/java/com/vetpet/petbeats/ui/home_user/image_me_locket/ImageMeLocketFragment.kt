package com.vetpet.petbeats.ui.home_user.image_me_locket

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentImageMeLocketBinding
import com.vetpet.petbeats.data.remote.api.ApiUserHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.image_me_locket.adapter.ImageLocketChild
import com.vetpet.petbeats.ui.home_user.image_me_locket.adapter.ImageMeLocketAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.net.URL
import kotlin.getValue


class ImageMeLocketFragment : Fragment() {
    private var _binding: FragmentImageMeLocketBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapter: ImageMeLocketAdapter
    private var currentImageLocket: ImageLocketChild? = null
    private val viewModel: ImageMeLocketViewModel by viewModels {
        ImageMeLocketViewModelFactory(
            HomeUserRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiUserHome::class.java)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentImageMeLocketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onMeLocketList()

        setupViewPager()
        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.btnDown.setOnClickListener {
            val imageUrl = currentImageLocket?.imageUrl

            if (!imageUrl.isNullOrEmpty()) {
                //Khóa nút và đổi icon để báo cho người dùng biết app đang xử lý
                binding.btnDown.isEnabled = false

                //Chạy Coroutine luồng IO để tải ảnh từ mạng
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val url = URL(imageUrl)
                        val connection = url.openConnection()
                        connection.doInput = true
                        connection.connect()
                        val input = connection.inputStream
                        val bitmap = BitmapFactory.decodeStream(input)

                        //Quay lại luồng Main để cập nhật giao diện và lưu ảnh
                        withContext(Dispatchers.Main) {
                            if (bitmap != null) {
                                saveImageToGallery(bitmap)
                                binding.btnDown.setImageResource(R.drawable.icon_success)
                            } else {
                                Toast.makeText(requireContext(), "Lỗi khi đọc dữ liệu ảnh", Toast.LENGTH_SHORT).show()
                                binding.btnDown.isEnabled = true
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Tải ảnh thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
                            binding.btnDown.isEnabled = true
                        }
                    }
                }
            }
            else {
                Toast.makeText(requireContext(), "Không tìm thấy đường dẫn ảnh", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun setupViewPager() {
        adapter = ImageMeLocketAdapter()
        binding.imgLocket.adapter = adapter


        //Vuốt dọc
        binding.imgLocket.orientation = ViewPager2.ORIENTATION_VERTICAL

        binding.imgLocket.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)


                if (adapter.currentList.isNotEmpty() && position < adapter.currentList.size) {
                    currentImageLocket = adapter.currentList[position]
                }


                // Nếu lướt đến cách bức ảnh cuối cùng 2 vị trí, gọi API tải thêm
                val totalItemCount = adapter.itemCount
                if (totalItemCount > 0 && position >= totalItemCount - 1) {
                    viewModel.onMeLocketList()
                }
            }
        })
    }


    //Hàm lưu ảnh vào thư viện
    private fun saveImageToGallery(bitmap: Bitmap?) {
        val filename = "Locket_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        var imageUri: Uri? = null

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val resolver = requireContext().contentResolver
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            imageUri?.let { uri ->
                fos = resolver.openOutputStream(uri)
                fos?.let {
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    Toast.makeText(requireContext(), "Đã lưu ảnh vào thư viện!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Lỗi khi lưu ảnh: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            fos?.close()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                imageUri?.let { resolver.update(it, contentValues, null, null) }
            }
        }
    }


    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.submitList(state.listMeLocket)
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