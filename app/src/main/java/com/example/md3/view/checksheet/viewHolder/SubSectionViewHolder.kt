package com.example.md3.view.checksheet.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.asap.codenicely.pdf.gstinvoicing.free.mobile.easy.gst.invoice.quick.quickinvoice.gstinvoicing.ui.custom_fields.adapters.CustomFieldsAdapter
import com.example.md3.data.model.checksheet.SubSection
import com.example.md3.databinding.SubsectionLayoutBinding
import com.example.md3.utils.KotlinFunctions.addItemDecoration


class SubSectionViewHolder(private val binding: SubsectionLayoutBinding ,val isDisabled : Boolean) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(subSection: SubSection, showCount: Boolean) {

        if(showCount){
            if(subSection.fields.size != 0){
                binding.tvtitle.text = subSection.fields.size.toString() + " Parameters"
            }
        }else{
            binding.tvtitle.text = subSection.title
        }

        val fieldsAdapter = CustomFieldsAdapter(isDisabled)
        fieldsAdapter.submitList(subSection.fields)
        binding.rvFields.adapter = fieldsAdapter
        binding.rvFields.addItemDecoration(binding.root.context)

//        val subSectionAdapter = SubSectionAdapter()
//        subSectionAdapter.submitList(parentItem.subSections)
//        binding.childRv.adapter = subSectionAdapter

    }
}