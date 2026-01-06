package com.asap.codenicely.pdf.gstinvoicing.free.mobile.easy.gst.invoice.quick.quickinvoice.gstinvoicing.ui.custom_fields.adapters.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.md3.R
import com.example.md3.data.model.checksheet.Field
import com.example.md3.databinding.DropdownCustomFieldBinding


class DropdownCustomFieldViewHolder(val binding: DropdownCustomFieldBinding ,val isDisabled : Boolean) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {

        fun create(
            inflater: LayoutInflater,
            viewGroup: ViewGroup
        ): DropdownCustomFieldViewHolder {
            val binding: ViewBinding
            binding = DropdownCustomFieldBinding.inflate(inflater, viewGroup, false)
            return DropdownCustomFieldViewHolder(binding , false)
        }
    }


    fun bind(item: Any) {

        binding.apply {
            when (item) {
                is Field -> {
                    autocompleteLayout.isEnabled = isDisabled
                    autoCompleteTextView.isEnabled = isDisabled
                    tvtitle.text = item.labelName
                    autocompleteLayout.hint = item.helpText
                    autoCompleteTextView.setText(item.value , false)
                    val adapter = ArrayAdapter<String>(
                        binding.root.context,
                        R.layout.dropdown_item_layout,
                        item.dropdownOptions.listOptions as MutableList<String>
                    )
                    autoCompleteTextView.setAdapter(adapter)
                    autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
                        val selectedItem = adapter.getItem(position).toString()
                        item.value = selectedItem
                    }

                }

            }
        }


    }


}