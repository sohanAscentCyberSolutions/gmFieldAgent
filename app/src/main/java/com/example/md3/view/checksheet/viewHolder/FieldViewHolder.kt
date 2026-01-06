package com.example.md3.view.checksheet.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.md3.data.model.checksheet.Field
import com.example.md3.databinding.ChecksheetChildItemBinding


class FieldViewHolder(private val binding: ChecksheetChildItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(childItem: Field) {
        binding.tvtitle.text = childItem.labelName

    }
}
