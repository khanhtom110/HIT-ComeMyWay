package com.vetpet.petbeats.ui.home_admin.statistic_admin

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentStatisticAdminBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue
import kotlin.math.roundToInt

@AndroidEntryPoint
class StatisticAdminFragment : Fragment() {
    private var _binding: FragmentStatisticAdminBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: StatisticAdminViewModel by viewModels()
    private val userTarget = 1000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStatisticAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupPieChart()


        binding.progressUser.isIndeterminate = false
        binding.progressUser.max = userTarget


        observeStatistics()
        stateData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun observeStatistics() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.observeStatistics()
            }
        }
    }



    private fun setupPieChart() {
        binding.pieChartClinic.apply {
            description.isEnabled = false
            legend.isEnabled = false

            setDrawEntryLabels(false)
            setUsePercentValues(true)

            // Tạo hình vòng tròn có lỗ ở giữa
            isDrawHoleEnabled = true
            holeRadius = 60f
            transparentCircleRadius = 60f
            setHoleColor(
                ContextCompat.getColor(requireContext(), R.color.colorBackground)
            )

            setDrawCenterText(true)
            setCenterTextTypeface(Typeface.DEFAULT_BOLD)
            setCenterTextSize(16f)
            setCenterTextColor(
                ContextCompat.getColor(requireContext(), R.color.black)
            )

            rotationAngle = 270f

            setTouchEnabled(false)
            setExtraOffsets(0f, 0f, 0f, 0f)
            minOffset = 0f
        }
    }


    private fun updateClinicChart(
        activeClinics: Int,
        inactiveClinics: Int
    ) {
        val chart = binding.pieChartClinic

        val active = activeClinics.coerceAtLeast(0)
        val inactive = inactiveClinics.coerceAtLeast(0)
        val total = active + inactive


        val activeColor = ContextCompat.getColor(
            requireContext(),
            R.color.colorPrimary
        )
        val inactiveColor = R.color.colorTextContent

        val entries = arrayListOf<PieEntry>()
        val colors = arrayListOf<Int>()

        if (total == 0) {
            // Vòng xám làm nền khi chưa có phòng khám
            entries.add(PieEntry(1f))
            colors.add(inactiveColor)
        } else {
            if (active > 0) {
                entries.add(PieEntry(active.toFloat(), "Đang hoạt động"))
                colors.add(activeColor)
            }

            if (inactive > 0) {
                entries.add(PieEntry(inactive.toFloat(), "Chưa hoạt động"))
                colors.add(inactiveColor)
            }
        }

        val dataSet = PieDataSet(entries, "").apply {
            this.colors = colors
            sliceSpace = 0f

            valueTextColor = android.graphics.Color.WHITE
            valueTextSize = 14f
            valueTypeface = Typeface.DEFAULT
            yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
            // Khi tổng = 0, không hiển thị phần trăm của vòng nền
            setDrawValues(total > 0)
        }

        chart.data = PieData(dataSet).apply {
            setValueFormatter(
                object : PercentFormatter(chart) {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.roundToInt()}%"
                    }
                }
            )
        }

        val totalText = total.toString()

        chart.centerText = SpannableString("$totalText\nTổng").apply {
            // Chỉ số tổng in đậm
            setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                totalText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Số tổng lớn hơn chữ "Tổng"
            setSpan(
                RelativeSizeSpan(1.4f),
                0,
                totalText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        chart.notifyDataSetChanged()
        chart.invalidate()
    }



    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val totalClinic = state.activeClinics + state.inactiveClinics
                    val totalUsers = state.totalUsers.coerceAtLeast(0)

                    binding.tvCurrrentNumber.text = state.activeClinics.toString()
                    binding.tvNotCurrrentNumber.text = state.inactiveClinics.toString()
                    binding.tvCurrrentTotal.text = totalClinic.toString()
                    binding.numbQuantity.text = totalUsers.toString()



                    updateClinicChart(
                        activeClinics = state.activeClinics,
                        inactiveClinics = state.inactiveClinics
                    )

                    val percent = (totalUsers.toDouble() / userTarget * 100).roundToInt()

                    binding.tvPercent.text = "$percent%"
                    binding.progressUser.setProgressCompat(totalUsers.coerceIn(0, userTarget), true)
                }
            }
        }
    }
}