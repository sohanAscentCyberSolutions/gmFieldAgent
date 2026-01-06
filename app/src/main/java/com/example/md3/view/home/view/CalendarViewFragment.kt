package com.example.md3.view.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.md3.R
import com.example.md3.data.model.home.generateCalendarCasess
import com.example.md3.databinding.Example3CalendarHeaderBinding
import com.example.md3.databinding.Example5CalendarDayBinding
import com.example.md3.databinding.FragmentCalendarViewBinding
import com.example.md3.utils.KotlinFunctions.addItemDecoration
import com.example.md3.utils.KotlinFunctions.displayText
import com.example.md3.view.home.adapters.AllCasesAdapter
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


class CalendarViewFragment : Fragment() {

    private var selectedDate: LocalDate? = null
    private val caseAdapter = AllCasesAdapter()
    private val cases = generateCalendarCasess().groupBy { it.time.toLocalDate() }
    private val today = LocalDate.now()


    private lateinit var binding: FragmentCalendarViewBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarViewBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        clickableViews()
    }


    fun initViews() {

        binding.RvCases.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = caseAdapter
        }
        binding.RvCases.addItemDecoration(
            requireContext(),
            R.drawable.line_divider_grey
        )

        caseAdapter.notifyDataSetChanged()

        val daysOfWeek = daysOfWeek()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(200)
        val endMonth = currentMonth.plusMonths(200)

        configureBinders(daysOfWeek)

        binding.rvCalendar.setup(startMonth, endMonth, daysOfWeek.first())
        binding.rvCalendar.scrollToMonth(currentMonth)
        binding.rvCalendar.post { selectDate(today) }

    }


    fun clickableViews() {

        binding.rvCalendar.monthScrollListener = { month ->
            binding.exFiveMonthYearText.text = month.yearMonth.displayText()

            selectedDate?.let {
                selectedDate = null
                binding.rvCalendar.notifyDateChanged(it)
                updateAdapterForDate(null)
            }
        }

        binding.exFiveNextMonthImage.setOnClickListener {
            binding.rvCalendar.findFirstVisibleMonth()?.let {
                binding.rvCalendar.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }

        binding.exFivePreviousMonthImage.setOnClickListener {
            binding.rvCalendar.findFirstVisibleMonth()?.let {
                binding.rvCalendar.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }
    }


    private fun updateAdapterForDate(date: LocalDate?) {
        caseAdapter.cases.clear()
        caseAdapter.cases.addAll(cases[date].orEmpty())
        caseAdapter.notifyDataSetChanged()
    }


    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.rvCalendar.notifyDateChanged(it) }
            binding.rvCalendar.notifyDateChanged(date)
            updateAdapterForDate(date)
        }
    }


    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = Example5CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            val binding = this@CalendarViewFragment.binding
                            binding.rvCalendar.notifyDateChanged(day.date)
                            oldDate?.let { binding.rvCalendar.notifyDateChanged(it) }
                            updateAdapterForDate(day.date)
                        }
                    }
                }
            }
        }


        binding.rvCalendar.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val context = container.binding.root.context
                val textView = container.binding.exFiveDayText
                val layout = container.binding.exFiveDayLayout
                textView.text = data.date.dayOfMonth.toString()

                val flightTopView = container.binding.commissionView
                val flightBottomView = container.binding.breakDownView


//                if (data.position == DayPosition.MonthDate) {
//                    when (data.date) {
//                        today -> {
//                            textView.setTextColor(resources.getColor(R.color.md_theme_onPrimary))
//                            textView.background =
//                                context.getDrawable(R.drawable.example_5_selected_bg)
//                        }
//
//                        selectedDate -> {
//                            textView.setTextColor(resources.getColor(R.color.md_theme_onPrimary))
//                            textView.background =
//                                context.getDrawable(R.drawable.example_5_selected_bg)
//                        }
//
//                        else -> {
//                            textView.setTextColor(resources.getColor(R.color.md_theme_black))
//                            textView.background = null
//                        }
//                    }
//                } else {
////                    textView.makeInVisible()
////                    dotView.makeInVisible()
//                }


//                val cases = cases[data.date]
//                if (cases != null) {
//                    if (cases.count() == 1) {
//                        flightBottomView.isVisible = true
//                    } else {
//                        flightTopView.isVisible = true
//                        flightBottomView.isVisible = true
//                    }
//                }


                if (data.position == DayPosition.MonthDate) {
                    if (selectedDate == data.date) {
                        textView.setTextColor(resources.getColor(R.color.md_theme_onPrimary))
                        textView.background = context.getDrawable(R.drawable.example_5_selected_bg)
                    } else {
                        textView.setTextColor(resources.getColor(R.color.md_theme_black))
                        textView.setBackgroundColor(0)
                    }

                    val cases = cases[data.date]
                    if (cases != null) {
                        if (cases.count() == 1) {
                            flightBottomView.isVisible = true
                        } else {
                            flightTopView.isVisible = true
                            flightBottomView.isVisible = true
                        }
                    }
                } else {
                    textView.setTextColor(resources.getColor(R.color.colorOnPending))
                    layout.background = null
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = Example3CalendarHeaderBinding.bind(view).legendLayout.root
        }



        binding.rvCalendar.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = data.yearMonth
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].displayText(uppercase = false)
                            }
                    }
                }
            }
    }
}




