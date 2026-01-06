package com.example.md3.view.checksheet.viewHolder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.md3.data.model.checksheet.SubSection
import com.example.md3.databinding.ChipSubsectionLayoutBinding
import com.google.android.material.chip.Chip


class SubSectionChipsViewHolder(private val binding: ChipSubsectionLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(subSection: SubSection) {
        // Clear existing chips
        binding.chipGroup.removeAllViews()
        binding.chipGroup.isVisible = true

        val chip = Chip(binding.root.context)
        chip.text = "Texting"
        chip.isClickable = true
//        chip.setOnClickListener {
//            val fieldsAdapter = CustomFieldsAdapter()
//            fieldsAdapter.submitList(subSection.fields)
//            binding.rvChip.adapter = fieldsAdapter
//        }
        binding.chipGroup.addView(chip)
    }
}