package com.vetpet.petbeats.ui.home_user.locket

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentLocketBinding
import com.vetpet.petbeats.data.remote.api.ApiUserHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeUserRepository
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.Manifest
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.widget.addTextChangedListener
import java.io.File
import java.io.OutputStream


class LocketFragment : Fragment() {
    private var _binding: FragmentLocketBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: LocketViewModel by viewModels {
        LocketViewModelFactory(
            HomeUserRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiUserHome::class.java)
            )
        )
    }


    private var currentFlashMode = ImageCapture.FLASH_MODE_OFF


    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private var currentLensFacing = CameraSelector.LENS_FACING_FRONT


    // 1. Tạo một Launcher để hiển thị hộp thoại xin quyền
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Người dùng đã cho phép -> Mở camera
            startCamera(currentLensFacing)
        } else {
            // Người dùng từ chối
            Toast.makeText(requireContext(), "Bạn cần cấp quyền Camera để sử dụng tính năng này", Toast.LENGTH_LONG).show()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLocketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()
        checkCameraPermissionAndStart()


        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.btnFriend.setOnClickListener {
            viewModel.listFriendLocket()
        }
        binding.btnImage.setOnClickListener {
            viewModel.imageMeLocket()
        }
        binding.btnAll.setOnClickListener {
            viewModel.imageEverybodyLocket()
        }


        binding.editFeel.addTextChangedListener {
            viewModel.onFeelChange(it.toString())
        }


        binding.btnCamera.setOnClickListener {
            takePhoto()
        }
        binding.btnReflect.setOnClickListener {
            currentLensFacing = if (currentLensFacing == CameraSelector.LENS_FACING_FRONT) {
                CameraSelector.LENS_FACING_BACK

            }
            else {
                CameraSelector.LENS_FACING_FRONT
            }
            startCamera(currentLensFacing)
        }
        binding.btnCancel.setOnClickListener {
            binding.imgResult.visibility = View.GONE
            binding.btnCancel.visibility = View.GONE
            binding.btnDown.visibility = View.GONE
            binding.btnCameraSend.visibility = View.GONE
            binding.editFeel.visibility = View.GONE

            binding.btnCamera.visibility = View.VISIBLE
            binding.btnImage.visibility = View.VISIBLE
            binding.btnReflect.visibility = View.VISIBLE
            binding.btnFlash.visibility = View.VISIBLE

            viewModel.downCheckLocketFalse()

            startCamera(currentLensFacing)
        }
        binding.btnDown.setOnClickListener {
            viewModel.downCheckLocketTrue()
        }


        binding.btnCameraSend.setOnClickListener {
            val drawable = binding.imgResult.drawable as? android.graphics.drawable.BitmapDrawable
            val bitmap = drawable?.bitmap
            val imageFile = bitmapToFile(bitmap)


            viewModel.onImageLocketSend(imageFile)
        }


        binding.btnFlash.setOnClickListener {
            currentFlashMode = if (currentFlashMode == ImageCapture.FLASH_MODE_OFF) {
                ImageCapture.FLASH_MODE_ON
            } else {
                ImageCapture.FLASH_MODE_OFF
            }

            imageCapture?.flashMode = currentFlashMode

            if (currentFlashMode == ImageCapture.FLASH_MODE_ON) {
                Toast.makeText(requireContext(), "Đã BẬT Flash", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Đã TẮT Flash", Toast.LENGTH_SHORT).show()
            }

            viewModel.changeFlash()
        }
    }

    
    
    // 3. Hàm kiểm tra quyền
    private fun checkCameraPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera(currentLensFacing)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera(lensFacing: Int) {
        val cameraProviderFutures = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFutures.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFutures.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.camera.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().setFlashMode(currentFlashMode).build()

            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            try {
                cameraProvider.unbindAll()

                //Camera tự mở khi Fragment mở, tự tắt khi Fragment tắt
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture!!
                )
            } catch (exc: Exception) {
                Toast.makeText(requireContext(), "Lỗi mở camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {

                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    val bitmap = imageProxyToBitmap(imageProxy)
                    imageProxy.close()


                    binding.imgResult.setImageBitmap(bitmap)

                    binding.imgResult.visibility = View.VISIBLE
                    binding.btnCancel.visibility = View.VISIBLE
                    binding.btnDown.visibility = View.VISIBLE
                    binding.btnCameraSend.visibility = View.VISIBLE
                    binding.editFeel.visibility = View.VISIBLE

                    //INVISIBLE: tàn hình chứ k ẩn hoàn toàn
                    binding.btnCamera.visibility = View.INVISIBLE
                    binding.btnImage.visibility = View.INVISIBLE
                    binding.btnReflect.visibility = View.INVISIBLE
                    binding.btnFlash.visibility = View.INVISIBLE
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(context, "Chụp thất bại: ${exception.message}", Toast.LENGTH_SHORT).show()
                    binding.camera.isEnabled = true
                }
            }
        )
    }

    //Hàm hỗ trợ xử lý ảnh
    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)!!

        val matrix = Matrix()
        matrix.postRotate(image.imageInfo.rotationDegrees.toFloat())

        if (currentLensFacing == CameraSelector.LENS_FACING_FRONT) {
            matrix.postScale(-1f, 1f)
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    //Hàm chuyển Bitmap thành File
    private fun bitmapToFile(bitmap: Bitmap?): File {
        val file = File(requireContext().cacheDir, "locket_${System.currentTimeMillis()}.jpg")
        file.outputStream().use { out ->
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        return file
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
                    //check input
                    if (binding.editFeel.text.toString() != state.message) {
                        binding.editFeel.setText(state.message)
                    }



                    //check
                    if (state.isDown) {
                        val drawable = binding.imgResult.drawable as? android.graphics.drawable.BitmapDrawable
                        val bitmap = drawable?.bitmap

                        saveImageToGallery(bitmap)

                        binding.btnDown.isEnabled = false
                        binding.btnDown.setImageResource(R.drawable.icon_success)

                    }
                    else {
                        binding.btnDown.isEnabled = true
                        binding.btnDown.setImageResource(R.drawable.icon_down)
                    }

                    if (state.isFlash) {
                        binding.btnFlash.setImageResource(R.drawable.icon_flash_open)
                    }
                    else {
                        binding.btnFlash.setImageResource(R.drawable.icon_flash_close)
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
                        is LocketEvent.NavigationListFriendLocket -> {
                            findNavController().navigate(R.id.listFriendLocketFragment)
                        }
                        is LocketEvent.NavigationMeLocket -> {
                            findNavController().navigate(R.id.imageMeLocketFragment)
                        }
                        is LocketEvent.NavigationEverybodyLocket -> {
                            findNavController().navigate(R.id.imageEverybodyLocketFragment)
                        }
                    }
                }
            }
        }
    }

}