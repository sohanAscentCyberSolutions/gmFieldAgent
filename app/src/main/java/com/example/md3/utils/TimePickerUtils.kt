package com.example.md3.utils


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TimePickerUtils {

     val DATE_FORMAT_FOR_DISPLAY = "dd/MM/yyyy"
     val DATE_FORMAT_FOR_BACKEND = "yyyy-MM-dd"



    fun showDateRangePicker(context: Context, listener: DateRangePickerListener) {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val constraintsBuilder = CalendarConstraints.Builder()

        // Set constraints to disable past dates
        constraintsBuilder.setStart(Calendar.getInstance().timeInMillis)

        // Set the constraints
        builder.setCalendarConstraints(constraintsBuilder.build())

        val datePicker = builder.build()

        // Add listener for date selection
        datePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second

            val formattedStartDate = formatDate(startDate)
            val formattedEndDate = formatDate(endDate)
            val formattedStartDateForBackend = formatDate(startDate, DATE_FORMAT_FOR_BACKEND)
            val formattedEndDateForBackend = formatDate(endDate,DATE_FORMAT_FOR_BACKEND)
            listener.onSelected(formattedStartDate.toString(), formattedEndDate.toString() , formattedStartDateForBackend , formattedEndDateForBackend)
        }

        // Show the date range picker
        datePicker.show((context as FragmentActivity).supportFragmentManager, datePicker.toString())
    }


    fun showDatePicker(
        context: Context,
        listener: TimePickerListener,
        disablePastDates: Boolean = false
    ) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                // Format the selected date
                val selectedDate = formatDate(year, month, dayOfMonth)
                listener.onSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        if (disablePastDates) {
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        }

        datePickerDialog.show()
    }


    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dateFormat = SimpleDateFormat(DATE_FORMAT_FOR_DISPLAY, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }



     fun formatDateFromString(inputDate: String, inputFormat: String, outputFormat: String): String {
        val inputDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
        val outputDateFormat = SimpleDateFormat(outputFormat, Locale.getDefault())
        val date = inputDateFormat.parse(inputDate)
        return outputDateFormat.format(date)
    }


    private fun formatDate(dateInMillis: Long , format : String = DATE_FORMAT_FOR_DISPLAY): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = Date(dateInMillis)
        return dateFormat.format(date)
    }




    fun showTimePicker(context: Context, listener: TimePickerListener) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)


        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val selectedTime = getFormattedTime(selectedHour, selectedMinute)
                listener.onSelected(selectedTime)
            },
            hourOfDay,
            minute,
            false
        )

        timePickerDialog.show()
    }

     fun getFormattedTime(hourOfDay: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }


    fun convertTo24HourFormat(time12Hour: String): String {
        val inputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        return try {
            val date = inputFormat.parse(time12Hour)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
            "" // Return empty string in case of any error
        }
    }


    fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }


    interface TimePickerListener {
        fun onSelected(time: String)
    }

    interface DateRangePickerListener {
        fun onSelected(
            startDate: String,
            endDate: String,
            formattedStartDateForBackend: String,
            formattedEndDateForBackend: String
        )
    }
}
