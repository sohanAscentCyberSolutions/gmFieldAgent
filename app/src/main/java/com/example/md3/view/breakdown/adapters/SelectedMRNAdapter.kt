package com.example.md3.view.breakdown.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.md3.data.model.mrn.MrnItem
import com.example.md3.databinding.MrnSelectedItemBinding


class SelectedMRNAdapter : ListAdapter<MrnItem, SelectedMRNAdapter.ViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MrnSelectedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: MrnSelectedItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnMinus.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = getItem(position)
                    if ((currentItem.userEnteredQnty ?: 0) >= 1) {
                        currentItem.userEnteredQnty = currentItem.userEnteredQnty.minus(1)
                        binding.tvQuantity.text = currentItem.userEnteredQnty.toString()
                    } else {
                        currentItem.userEnteredQnty = 0
                    }
                }
            }

            binding.btnPlus.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = getItem(position)
                    if(currentItem.quantity != currentItem.userEnteredQnty){
                        currentItem.userEnteredQnty = currentItem.userEnteredQnty.plus(1)
                    }else{
                        Toast.makeText(binding.root.context, "Entered Quantity can't be greater then available", Toast.LENGTH_SHORT).show()
                    }
                    binding.tvQuantity.text = currentItem.userEnteredQnty.toString()
                }
            }

        }

        fun bind(item: MrnItem) {
            binding.apply {
                if(item.warrantyDetails.status != "Out of Warranty" || item.warrantyDetails.coverageMonths != 0){
                    chipInWarranty.isVisible = true
                    chipInWarranty.isChecked = true
                }
                tvProductName.isVisible = item.bomItemDetails.modelNumber.isNotBlank()
                tvProductName.text = item.bomItemDetails.modelNumber
                binding.tvQuantity.text = item.userEnteredQnty.toString()
            }
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<MrnItem>() {
        override fun areItemsTheSame(oldItem: MrnItem, newItem: MrnItem): Boolean {
            return oldItem.bomItemDetails.id == newItem.bomItemDetails.id
        }

        override fun areContentsTheSame(oldItem: MrnItem, newItem: MrnItem): Boolean {
            return oldItem == newItem
        }
    }
}
