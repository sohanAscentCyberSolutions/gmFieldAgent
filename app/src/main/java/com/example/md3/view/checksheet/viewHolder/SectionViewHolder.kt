package com.example.md3.view.checksheet.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.md3.data.model.checksheet.Section
import com.example.md3.databinding.ChecksheetParentItemBinding
import com.example.md3.view.checksheet.adapter.SubSectionAdapter


class SectionViewHolder(private val binding: ChecksheetParentItemBinding ,private val isDisable : Boolean) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(parentItem: Section) {
        binding.tvTitle.text = parentItem.section
        val subSectionAdapter = SubSectionAdapter(false, isDisable)
        subSectionAdapter.submitList(parentItem.subSections)
        binding.childRv.adapter = subSectionAdapter
    }
}

