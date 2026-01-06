package com.asap.codenicely.pdf.gstinvoicing.free.mobile.easy.gst.invoice.quick.quickinvoice.gstinvoicing.ui.custom_fields.adapters.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.md3.data.model.checksheet.Field
import com.example.md3.databinding.DateCustomFieldBinding
import com.example.md3.utils.TimePickerUtils

class DateCustomFieldViewHolder(val binding: DateCustomFieldBinding , val isDisabled : Boolean) :
    RecyclerView.ViewHolder(binding.root) {

    private val TAG = "DateCustomFieldViewHold"

    companion object {

        fun create(
            inflater: LayoutInflater,
            viewGroup: ViewGroup
        ): DateCustomFieldViewHolder {
            val binding: ViewBinding
            binding = DateCustomFieldBinding.inflate(inflater, viewGroup, false)
            return DateCustomFieldViewHolder(binding,false)
        }
    }


    fun bind(item: Any) {
        binding.apply {
            when (item) {
                is Field -> {
                    transporterIdLayout.isEnabled = isDisabled
                    transporterIdLayout.hint = item.helpText
                    transporterIdLayout.setEndIconOnClickListener {
                        TimePickerUtils.showDatePicker(
                            binding.root.context,
                            object : TimePickerUtils.TimePickerListener {
                                override fun onSelected(time: String) {
                                    item.value = TimePickerUtils.formatDateFromString(
                                        time,
                                        TimePickerUtils.DATE_FORMAT_FOR_DISPLAY,
                                        TimePickerUtils.DATE_FORMAT_FOR_BACKEND
                                    )
                                }
                            })
                    }

                    transporterIdText.setOnClickListener {
                        TimePickerUtils.showDatePicker(
                            binding.root.context,
                            object : TimePickerUtils.TimePickerListener {
                                override fun onSelected(time: String) {
                                    item.value = TimePickerUtils.formatDateFromString(
                                        time,
                                        TimePickerUtils.DATE_FORMAT_FOR_DISPLAY,
                                        TimePickerUtils.DATE_FORMAT_FOR_BACKEND
                                    )                                }
                            })
                    }
                }
            }
        }
    }
}