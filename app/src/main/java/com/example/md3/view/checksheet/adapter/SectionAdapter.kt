package com.example.md3.view.checksheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.md3.data.model.checksheet.Section
import com.example.md3.databinding.ChecksheetParentItemBinding
import com.example.md3.view.checksheet.viewHolder.SectionViewHolder


class SectionAdapter(private val isDisabled : Boolean) : ListAdapter<Section, SectionViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChecksheetParentItemBinding.inflate(inflater, parent, false)
        return SectionViewHolder(binding,isDisabled)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val parentItem = getItem(position)
        holder.bind(parentItem)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Section>() {
            override fun areItemsTheSame(oldItem: Section, newItem: Section): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Section, newItem: Section): Boolean =
                oldItem == newItem
        }
    }
}
