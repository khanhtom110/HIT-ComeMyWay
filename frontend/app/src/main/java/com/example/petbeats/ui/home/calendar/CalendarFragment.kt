package com.example.petbeats.ui.home.calendar

import android.content.res.ColorStateList
import android.graphics.Color
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
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.petbeats.ui.home.calendar.adapter.TimePagerAdapter
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import java.time.YearMonth
import java.time.LocalDate


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
    private var selectedDate: LocalDate? = null

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

        val id = arguments?.getInt("id") ?: 0
        viewModel.onInformationBookingAPI(id)

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
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer {
                return DayViewContainer(view)
            }

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()

                //Ngày thuộc tháng hiện tại thì chữ đen, tháng trước/sau thì chữ xám
                if (data.position == DayPosition.MonthDate) {
                    container.textView.setTextColor(Color.parseColor("#181818"))
                } else {
                    container.textView.setTextColor(Color.parseColor("#A7A7B4"))
                }

                //click thì hiện background xanh, chữ xanh
                if (data.date == selectedDate) {
                    container.textView.setBackgroundResource(R.drawable.ground_book_child)
                    container.textView.setTextColor(Color.parseColor("#486BF3"))
                } else {
                    container.textView.background = null
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

    private fun setOnClick() {
        binding.btnBack.setOnClickListener {
            val id = arguments?.getInt("id") ?: 0
            viewModel.InformationClick(id)
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
        binding.tvInputState.addTextChangedListener {
            viewModel.onStateChange(it.toString())
        }


        binding.btnBooking.setOnClickListener {
            val id = arguments?.getInt("id") ?: 0

            viewModel.onCalendarClick(id)
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
                    if (state.isInputState) {
                        binding.tvInputState.setBackgroundResource(R.drawable.button_input)
                    }
                    else {
                        binding.tvInputState.setBackgroundResource(R.drawable.ground_information)
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
                    if (binding.tvInputState.text.toString() != state.state) {
                        binding.tvInputState.setText(state.state)
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

                    //Service
                    binding.btnService.removeAllViews()
                    state.services.forEach { servieName ->
                        val chip = com.google.android.material.chip.Chip(requireContext()).apply {
                            text = servieName.name
                            id = servieName.id

                            //cho phép bấm chọn
                            isClickable = true
                            isCheckable = true
                            chipStrokeWidth = 3f

                            //nền thay đổi theo click
                            val groundColor = ColorStateList(
                                arrayOf(
                                    intArrayOf(android.R.attr.state_checked), // đang chọn
                                    intArrayOf(-android.R.attr.state_checked) //bình thường
                                ),
                                intArrayOf(
                                    ContextCompat.getColor(context, R.color.colorPrimary), //đã click
                                    ContextCompat.getColor(context, R.color.colorBackground) //chưa click
                                )
                            )
                            chipBackgroundColor = groundColor

                            //màu text thay đổi theo click
                            val textColors = ColorStateList(
                                arrayOf(
                                    intArrayOf(android.R.attr.state_checked),
                                    intArrayOf(-android.R.attr.state_checked)
                                ),
                                intArrayOf(
                                    ContextCompat.getColor(context, R.color.colorBackground), //đã click
                                    ContextCompat.getColor(context, R.color.colorPrimary) //chưa click
                                )
                            )
                            setTextColor(textColors)

                            //màu viền thay đổi theo click
                            val strokeColorState = ColorStateList(
                                arrayOf(
                                    intArrayOf(android.R.attr.state_checked),
                                    intArrayOf(-android.R.attr.state_checked)
                                ),
                                intArrayOf(
                                    ContextCompat.getColor(context, R.color.colorPrimary), //đã click
                                    ContextCompat.getColor(context, R.color.colorPrimary)  //chưa click
                                )
                            )
                            chipStrokeColor = strokeColorState

                            //người dùng click để báo về viewmodel
                            setOnCheckedChangeListener { button, isChecked ->
                                if (isChecked) {

                                }
                                else {

                                }
                            }
                        }
                        binding.btnService.addView(chip)
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
                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is CalendarEvent.NavigationInformationRoom -> {
                            findNavController().navigate(
                                R.id.calendar_informationRoom,
                                Bundle().apply {
                                    putInt("id", event.id)
                                }
                            )
                        }
                        is CalendarEvent.NavigationConfirmAppointment -> {
                            findNavController().navigate(
                                R.id.confirmAppointmentFragment,
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