package com.example.md3.view.checksheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.md3.data.model.checksheet.Field
import com.example.md3.databinding.ChecksheetChildItemBinding
import com.example.md3.view.checksheet.viewHolder.FieldViewHolder


class FieldAdapter : ListAdapter<Field, FieldViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChecksheetChildItemBinding.inflate(inflater, parent, false)
        return FieldViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FieldViewHolder, position: Int) {
        val childItem = getItem(position)
        holder.bind(childItem)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Field>() {
            override fun areItemsTheSame(oldItem: Field, newItem: Field): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Field, newItem: Field): Boolean =
                oldItem == newItem
        }
    }
}
