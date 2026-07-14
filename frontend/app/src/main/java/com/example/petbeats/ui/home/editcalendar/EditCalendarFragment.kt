package com.example.petbeats.ui.home.editcalendar

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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
import com.example.petbeats.R
import com.example.petbeats.data.remote.api.ApiHome
import com.example.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.databinding.FragmentCalendarBinding
import com.example.petbeats.databinding.FragmentEditCalendarBinding
import com.example.petbeats.databinding.LayoutPopupDialogBinding
import com.example.petbeats.ui.home.calendar.CalendarEvent
import com.example.petbeats.ui.home.calendar.CalendarViewModel
import com.example.petbeats.ui.home.calendar.CalendarViewModelFactory
import com.example.petbeats.ui.home.calendar.DayViewContainer
import com.example.petbeats.ui.home.calendar.adapter.TimePagerAdapter
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import kotlin.getValue
import androidx.core.graphics.drawable.toDrawable

class EditCalendarFragment : Fragment() {
    private var _binding: FragmentEditCalendarBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: EditCalendarViewModel by viewModels {
        EditCalendarViewModelFactory(
            HomeRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiHome::class.java)
            )
        )
    }
    private var selectedDate: LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clinicId = arguments?.getInt("clinicId") ?: 0
        val id = arguments?.getInt("id") ?: 0
        viewModel.onInformationBookingAPI(clinicId)
        viewModel.onAppointmentShow(id)

        calendar()
        setupTime()
        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun calendar() {
        val today = LocalDate.now()

        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer {
                return DayViewContainer(view)
            }

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()

                //Ngày thuộc tháng hiện tại thì chữ đen, tháng trước/sau thì chữ xám
                if (data.position != DayPosition.MonthDate || data.date.isBefore(today)) {
                    container.textView.setTextColor(Color.parseColor("#A7A7B4"))
                } else {
                    container.textView.setTextColor(Color.parseColor("#181818"))
                }

                //click thì hiện background xanh, chữ xanh
                if (data.date == selectedDate) {
                    container.textView.setBackgroundResource(R.drawable.ground_book_child_blue)
                    container.textView.setTextColor(Color.parseColor("#FAFCFF"))
                } else {
                    container.textView.background = null
                }

                //background của ngày hôm nay
                if (data.date == today) {
                    container.textView.setBackgroundResource(R.drawable.ground_book_child)
                    container.textView.setTextColor(Color.parseColor("#486BF3"))
                }


                //Lắng nghe sự kiện click
                container.view.setOnClickListener {
                    // Chỉ cho phép click vào ngày của tháng hiện tại
                    if (data.position == DayPosition.MonthDate) {
                        val currentSelection = selectedDate
                        // click lại vào ngày đang chọn bỏ chọn ô đó
                        if (currentSelection == data.date) {
                            selectedDate = null
                            binding.calendarView.notifyDateChanged(currentSelection)
                        } else {
                            // Cập nhật biến selectedDate và load lại màu 2 ô
                            selectedDate = data.date
                            viewModel.onDateSelect(data.date.toString())
                            binding.calendarView.notifyDateChanged(data.date)
                            if (currentSelection != null) {
                                binding.calendarView.notifyDateChanged(currentSelection)
                            }
                        }
                    }
                }

            }
        }
        val currentMonth = YearMonth.now() // Tháng hiện tại (Ví dụ: Tháng 7/2026)
        val startMonth = currentMonth.minusMonths(100) // Trừ đi 100 tháng làm mốc bắt đầu
        val endMonth = currentMonth.plusMonths(100)    // Cộng thêm 100 tháng làm mốc kết thúc
        val firstDayOfWeek = firstDayOfWeekFromLocale() // Lấy thứ đầu tiên của tuần (Ví dụ: Thứ 2)

        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)

        //Hiển thị ngày tháng hiện tại
        if (selectedDate == null) {
            selectedDate = today
            viewModel.onDateSelect(today.toString())
            binding.calendarView.notifyDateChanged(today)
        }

        //Vuốt lịch thì năm, tháng cũng đổi theo
        binding.calendarView.monthScrollListener = { month ->
            binding.tvTime.text = "Tháng ${month.yearMonth.monthValue} năm ${month.yearMonth.year}"
        }

        // Nút lùi về tháng trước
        binding.btnBackCalendarStart.setOnClickListener {
            val firstVisibleMonth = binding.calendarView.findFirstVisibleMonth()
            if (firstVisibleMonth != null) {
                binding.calendarView.smoothScrollToMonth(firstVisibleMonth.yearMonth.minusMonths(1))
            }
        }

        // Nút tiến tới tháng sau
        binding.btnBackCalendarEnd.setOnClickListener {
            val firstVisibleMonth = binding.calendarView.findFirstVisibleMonth()
            if (firstVisibleMonth != null) {
                binding.calendarView.smoothScrollToMonth(firstVisibleMonth.yearMonth.plusMonths(1))
            }
        }
    }

    private fun setupTime() {
        val hourList = (0..23).map {
            String.format("%02d", it)
        }
        val minuteList = listOf("00", "30")

        binding.vpTimeOpen.apply {
            adapter = TimePagerAdapter(hourList)
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }
        binding.vpTimeClose.apply {
            adapter = TimePagerAdapter(minuteList)
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }
    }

    private fun getSelectTime() {
        val openTime = binding.vpTimeOpen.currentItem
        val closeTime = binding.vpTimeClose.currentItem

        val hour = String.format("%02d", openTime)
        val minute = if (closeTime == 0) {
            "00"
        }
        else {
            "30"
        }

        viewModel.onTimeSelect(hour, minute)
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener {
            val id = arguments?.getInt("id") ?: 0
            val clinicId = arguments?.getInt("clinicId") ?: 0
            viewModel.informationClick(id, clinicId)
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
            val quantity = it.toString().toIntOrNull() ?: 0
            viewModel.onQuantityChange(quantity)
        }
        binding.tvInputOther.addTextChangedListener {
            viewModel.onOtherChange(it.toString())
        }
        binding.tvInputState.addTextChangedListener {
            viewModel.onStateChange(it.toString())
        }


        binding.btnBooking.setOnClickListener {
            val clinicId = arguments?.getInt("clinicId") ?: 0
            val appointmentId = arguments?.getInt("id") ?: 0

            getSelectTime()

            showPopupDialog(
                message = "Bạn có chắc chắn muốn đặt lịch khám này không?",
                leftButton = "Huỷ",
                rightButton = "Xác nhận",
                onRightButtonClick = {
                    viewModel.onCalendarClick(appointmentId, clinicId)
                }
            )
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


        binding.btnClinicRoom.setOnClickListener {
            viewModel.onClinicClick()
        }
        binding.btnHomeRoom.setOnClickListener {
            viewModel.onHomeClick()
        }

        binding.btnCancel.setOnClickListener {
            showPopupDialog(
                message = "Bạn có chắc chắn muốn huỷ lịch khám này không?",
                leftButton = "Huỷ lịch",
                rightButton = "Quay lại",
                onLeftButtonClick = {
                    viewModel.onCancelAppointment()
                }
            )
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
                        binding.tvInputName.setBackgroundResource(R.drawable.button_input_errol)
                    }
                    else {
                        binding.tvInputName.setBackgroundResource(R.drawable.ground_information)
                    }
                    if (state.isAddress) {
                        binding.tvInputAddress.setBackgroundResource(R.drawable.button_input_errol)
                    }
                    else {
                        binding.tvInputAddress.setBackgroundResource(R.drawable.ground_information)
                    }
                    if (state.isInputOther) {
                        binding.tvInputOther.setBackgroundResource(R.drawable.ground_information)
                    }
                    else {
                        binding.tvInputOther.setBackgroundResource(R.drawable.ground_information)
                    }
                    if (state.isClinic) {
                        binding.btnClinicRoom.setBackgroundResource(R.drawable.ground_book_child_blue)

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorBackground)
                        binding.btnClinicRoom.setTextColor(clinic)
                    }
                    else {
                        binding.btnClinicRoom.setBackgroundResource(R.drawable.ground_book_child)

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                        binding.btnClinicRoom.setTextColor(clinic)
                    }
                    if (state.isHome) {
                        binding.btnHomeRoom.setBackgroundResource(R.drawable.ground_book_child_blue)

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorBackground)
                        binding.btnHomeRoom.setTextColor(clinic)
                    }
                    else {
                        binding.btnHomeRoom.setBackgroundResource(R.drawable.ground_book_child)

                        val clinic = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                        binding.btnHomeRoom.setTextColor(clinic)
                    }


                    //check error
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

                    if (state.isInformation) {
                        binding.boxInformation.setBackgroundResource(R.drawable.button_input_errol)
                        binding.tvInformationError.visibility = View.VISIBLE
                    }
                    else {
                        binding.boxInformation.setBackgroundResource(R.drawable.ground_information)
                        binding.tvInformationError.visibility = View.GONE
                    }
                    binding.tvInformationError.text = state.informationError

                    if (state.isService) {
                        binding.boxService.setBackgroundResource(R.drawable.button_input_errol)
                        binding.tvServiceError.visibility = View.VISIBLE
                    }
                    else {
                        binding.boxService.setBackgroundResource(R.drawable.ground_information)
                        binding.tvServiceError.visibility = View.GONE
                    }
                    binding.tvServiceError.text = state.serviceError

                    if (state.isCalendar) {
                        binding.boxCalendar.setBackgroundResource(R.drawable.button_input_errol)
                        binding.tvCalendarError.visibility = View.VISIBLE
                    }
                    else {
                        binding.boxCalendar.setBackgroundResource(R.drawable.ground_information)
                        binding.tvCalendarError.visibility = View.GONE
                    }
                    binding.tvCalendarError.text = state.calendarError

                    if (state.isTime) {
                        binding.boxTime.setBackgroundResource(R.drawable.button_input_errol)
                        binding.tvTimeError.visibility = View.VISIBLE
                    }
                    else {
                        binding.boxTime.setBackgroundResource(R.drawable.ground_information)
                        binding.tvTimeError.visibility = View.GONE
                    }
                    binding.tvTimeError.text = state.timeError
                    if (state.isQuantity) {
                        binding.tvInputQuantity.setBackgroundResource(R.drawable.button_input_errol)
                        binding.tvQuantityError.visibility = View.VISIBLE
                    }
                    else {
                        binding.tvInputQuantity.setBackgroundResource(R.drawable.ground_information)
                        binding.tvQuantityError.visibility = View.GONE
                    }
                    binding.tvQuantityError.text = state.quantityError





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
                    if (binding.tvInputOther.text.toString() != state.other) {
                        binding.tvInputOther.setText(state.other)
                    }
                    if (binding.tvInputState.text.toString() != state.state) {
                        binding.tvInputState.setText(state.state)
                    }


                    //quantity
                    val quantity = if (state.quantity == 0) "" else state.quantity.toString()
                    if (binding.tvInputQuantity.text.toString() != quantity) {
                        binding.tvInputQuantity.setText(quantity)
                    }



                    binding.tvClinicName.text = state.tittle
                    binding.tvRating.text = "Đánh giá: ${state.rating}/5"

                    if (state.isOperating) {
                        binding.tvStatus.text = "Đang hoạt động"
                    } else {
                        binding.tvStatus.text = "Không hoạt động"
                    }

                    if (state.thumbnailUrl.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(state.thumbnailUrl)
                            .into(binding.imgClinic)
                    }

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

                    //Service
                    if (binding.btnService.isEmpty() && state.services.isNotEmpty()) {
                        state.services.forEach { serviceName ->
                            val chip = com.google.android.material.chip.Chip(requireContext()).apply {
                                text = serviceName.name
                                id = serviceName.id

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
                                        viewModel.onServiceOpen(serviceName.id)
                                    }
                                    else {
                                        viewModel.onServiceClose(serviceName.id)
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
                                chip.isChecked = state.selectService.contains(chip.id)
                            }
                        }
                    }




                    //check type
                    if (state.isDog) {
                        binding.btnDog.setBackgroundResource(R.drawable.icon_open)

                        val dog = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.tvDog.setTextColor(dog)
                    }
                    else {
                        binding.btnDog.setBackgroundResource(R.drawable.icon_close)

                        val dog = ContextCompat.getColor(requireContext(),R.color.colorTextContent)
                        binding.tvDog.setTextColor(dog)
                    }
                    if (state.isCat) {
                        binding.btnCat.setBackgroundResource(R.drawable.icon_open)

                        val cat = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.tvCat.setTextColor(cat)
                    }
                    else {
                        binding.btnCat.setBackgroundResource(R.drawable.icon_close)

                        val cat = ContextCompat.getColor(requireContext(),R.color.colorTextContent)
                        binding.tvCat.setTextColor(cat)
                    }
                    if (state.isOther) {
                        binding.btnOther.setBackgroundResource(R.drawable.icon_open)
                        binding.tvTextOther.visibility = View.VISIBLE
                        binding.tvInputOther.visibility = View.VISIBLE

                        val other = ContextCompat.getColor(requireContext(),R.color.colorTextSub)
                        binding.tvOther.setTextColor(other)
                    }
                    else {
                        binding.btnOther.setBackgroundResource(R.drawable.icon_close)
                        binding.tvTextOther.visibility = View.GONE
                        binding.tvInputOther.visibility = View.GONE

                        val other = ContextCompat.getColor(requireContext(),R.color.colorTextContent)
                        binding.tvOther.setTextColor(other)
                    }


                    //calendar
                    if (state.appointmentDate.isNotEmpty() && selectedDate?.toString() != state.appointmentDate) {
                        try {
                            val fetchedDate = LocalDate.parse(state.appointmentDate)
                            val previousDate = selectedDate

                            // Cập nhật biến selectedDate toàn cục của Fragment
                            selectedDate = fetchedDate

                            // Tự động cuộn lịch tới đúng cái tháng chứa ngày đó
                            binding.calendarView.scrollToMonth(YearMonth.from(fetchedDate))

                            // Bôi xanh ngày vừa lấy từ API
                            binding.calendarView.notifyDateChanged(fetchedDate)

                            // Xóa màu xanh ở ngày cũ (nếu có)
                            if (previousDate != null) {
                                binding.calendarView.notifyDateChanged(previousDate)
                            }
                        } catch (e: Exception) {
                            Log.e("EditCalendar", "Lỗi parse ngày API: ${e.message}")
                        }
                    }

                    //time
                    if (state.hour.isNotEmpty()) {
                        val hour = state.hour.toIntOrNull() ?: 0

                        if (binding.vpTimeOpen.currentItem != hour) {
                            binding.vpTimeOpen.setCurrentItem(hour, false)
                        }
                    }
                    if (state.minute.isNotEmpty()) {
                        if (state.minute == "30") {
                            binding.vpTimeClose.currentItem = 1
                        }
                        else {
                            binding.vpTimeClose.currentItem = 0
                        }


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
                        is EditCalendarEvent.NavigationConfirmAppointment -> {
                            findNavController().navigate(
                                R.id.editCalendar_confirm,
                                Bundle().apply {
                                    putInt("id", event.id)
                                    putInt("clinicId", event.clinicId)
                                }
                            )
                        }
                        is EditCalendarEvent.NavigationSuccessAppointment -> {
                            findNavController().navigate(
                                R.id.successAppointFragment,
                                Bundle().apply {
                                    putInt("clinicId", event.clinicId)
                                    putInt("id", event.id)
                                }
                            )
                        }
                        is EditCalendarEvent.NavigationNextRoom -> {
                            findNavController().navigate(R.id.editCalendar_book)
                        }
                    }
                }
            }
        }
    }

}