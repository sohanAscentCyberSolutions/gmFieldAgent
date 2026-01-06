package com.asap.codenicely.pdf.gstinvoicing.free.mobile.easy.gst.invoice.quick.quickinvoice.gstinvoicing.ui.custom_fields.adapters.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.md3.data.model.checksheet.Field
import com.example.md3.databinding.SingleLineTextCustomFieldBinding
import com.example.md3.utils.KotlinFunctions

class TextViewCustomFieldViewHolder(val binding: SingleLineTextCustomFieldBinding ,  val isDisabled : Boolean) :
    RecyclerView.ViewHolder(binding.root) {

    private val TAG = "TextViewCustomFieldView"

    companion object {

        fun create(
            inflater: LayoutInflater,
            viewGroup: ViewGroup
        ): TextViewCustomFieldViewHolder {
            val binding: ViewBinding
            binding = SingleLineTextCustomFieldBinding.inflate(inflater, viewGroup, false)
            return TextViewCustomFieldViewHolder(binding,false)
        }
    }


    fun bind(item: Any) {
        binding.apply {
            when (item) {
                is Field ->{
                    binding.transporterIdText.isEnabled =  isDisabled
                    KotlinFunctions.setupEditTextWithAddCustomFields(
                        transporterIdText,
                        item,
                        transporterIdLayout
                    )
                }
            }
        }
    }
}