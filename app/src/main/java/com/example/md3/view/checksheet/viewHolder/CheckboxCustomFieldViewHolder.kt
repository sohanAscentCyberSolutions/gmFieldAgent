package com.asap.codenicely.pdf.gstinvoicing.free.mobile.easy.gst.invoice.quick.quickinvoice.gstinvoicing.ui.custom_fields.adapters.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.md3.data.model.checksheet.Field
import com.example.md3.databinding.CheckboxCustomFieldBinding

class CheckboxCustomFieldViewHolder(val binding: CheckboxCustomFieldBinding ,val isDisabled : Boolean) :
    RecyclerView.ViewHolder(binding.root) {

    val transporterCheckbox = binding.transporterCheckbox
    val transporterTitle = binding.transporterTitle

    companion object {

        fun create(
            inflater: LayoutInflater,
            viewGroup: ViewGroup
        ): CheckboxCustomFieldViewHolder {
            val binding: ViewBinding
            binding = CheckboxCustomFieldBinding.inflate(inflater, viewGroup, false)
            return CheckboxCustomFieldViewHolder(binding,false)
        }
    }


    fun bind(item: Any) {
        binding.apply {
            when (item) {
                is Field -> {
                    bindCustomFieldsDataTypes(item)
                }
            }
        }
    }

    private fun bindCustomFieldsDataTypes(item: Field) {
        transporterCheckbox.isClickable = isDisabled
        transporterTitle.text = item.labelName
        transporterCheckbox.setOnCheckedChangeListener { _, isChecked ->
            item.value = if (isChecked) "true" else "null"
        }
    }

    
}