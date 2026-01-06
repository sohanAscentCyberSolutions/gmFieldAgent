package com.example.md3.view.commissioning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.md3.databinding.CaseInfoLayoutBinding


data class ItemModel(val title: String, val value: String)



class CaseInfoAdapter(val onClick:()  -> Unit) : ListAdapter<ItemModel, CaseInfoAdapter.ViewHolder>(ItemModelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CaseInfoLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: CaseInfoLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemModel) {

            binding.root.setOnClickListener {
                onClick()
            }

            binding.tvTitle.text = item.title
            binding.tvValue.text = item.value
        }
    }



    private class ItemModelDiffCallback : DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return oldItem == newItem
        }
    }
}

