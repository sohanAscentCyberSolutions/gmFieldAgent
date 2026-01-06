package com.example.md3.view.breakdown.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.md3.data.model.Identifier
import com.example.md3.data.model.mrn.MrnDetailsItem
import com.example.md3.databinding.ClaimPartListItemBinding


class ClaimPartParenAdapter : ListAdapter<MrnDetailsItem, ClaimPartParenAdapter.ClaimItemViewHolder>(ClaimItemDiffCallback()) {

    private  val TAG = "`ClaimPartParenAdapter`"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimItemViewHolder {
        val binding =
            ClaimPartListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClaimItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClaimItemViewHolder, position: Int) {
        val claimItem = getItem(position)
        holder.bind(claimItem)
    }

    inner class ClaimItemViewHolder(private val binding: ClaimPartListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(claimItem: MrnDetailsItem) {
            binding.apply {

                if(claimItem.productDetails.warranty != 0){
                    chipOutWarranty.visibility = View.INVISIBLE
                    chipInWarranty.visibility = View.VISIBLE
                }else{
                    chipOutWarranty.visibility = View.VISIBLE
                    chipInWarranty.visibility = View.INVISIBLE
                }


                identificationNumber.text = claimItem.productDetails.partNumber
                partName.text = claimItem.productDetails.name
                partDescription.text = claimItem.productDetails.description

                when (claimItem.productDetails.inventoryType) {
                    "Batch Number" -> {
                        claimItem.productDetails.identifier = mutableListOf(Identifier("" , 0))
                        val childAdapter = ClaimPartChildAdapter("Batch Number" , getItem(position).quantity){
                            claimItem.productDetails.identifier = it
                        }
                        recyclerView.adapter = childAdapter
                        childAdapter.submitList(claimItem.productDetails.identifier)

                        addAnotherBatchLayout.addAnotherBatch.setOnClickListener {
                            claimItem.productDetails.identifier?.add(Identifier("", 0))
                            childAdapter.submitList(claimItem.productDetails.identifier)
                            childAdapter.notifyDataSetChanged()
                        }
                        binding.addAnotherBatchLayout.addAnotherBatch.text = "Add Another Batch"
                    }
                    "Serial Number" -> {
                        claimItem.productDetails.identifier = mutableListOf(Identifier("" , 0))
                        val childAdapter = ClaimPartChildAdapter("Serial Number", getItem(position).quantity){
                            claimItem.productDetails.identifier = it
                        }
                        recyclerView.adapter = childAdapter
                        childAdapter.submitList(claimItem.productDetails.identifier)
                        addAnotherBatchLayout.addAnotherBatch.setOnClickListener {
                            claimItem.productDetails.identifier?.add(Identifier("", 0))
                            childAdapter.submitList(claimItem.productDetails.identifier)
                            childAdapter.notifyDataSetChanged()
                        }

                        binding.addAnotherBatchLayout.addAnotherBatch.text = "Add Another Serial#"

                        binding.quantityCard.visibility = View.VISIBLE
                        binding.tvQuantity.text = claimItem.quantity.toString()

                    }
                    "Normal" -> {

                        quantityCardWithMinusPlus.isVisible = true
                        tvQuantityWithMinus.visibility =  View.VISIBLE
                        addAnotherBatchLayout.addAnotherBatch.isVisible = false
                        addAnotherBatchLayout.btnPlus.isVisible = false
                        tvQuantityWithMinus.text = claimItem.userEnteredQnty.toString()

                        if(claimItem.productDetails.description != null){
                            binding.partDescription.text = claimItem.productDetails.description ?: ""
                        }else{
                            binding.partDescription.isVisible = false
                        }


                        binding.btnMinus.setOnClickListener {
                            val position = adapterPosition
                            if (position != RecyclerView.NO_POSITION) {
                                if (getItem(position).userEnteredQnty > 1) {
                                    val newQuantity = getItem(position).userEnteredQnty - 1
                                    getItem(position).userEnteredQnty = newQuantity
                                    binding.tvQuantityWithMinus.text = getItem(position).userEnteredQnty.toString()
                                }else {
                                    getItem(position).userEnteredQnty = 0
                                }
                            }
                        }

                        binding.btnPlus.setOnClickListener {
                            val position = adapterPosition
                            if (position != RecyclerView.NO_POSITION) {
                                val currentQuantity = getItem(position).quantity
                                if(getItem(position).userEnteredQnty != currentQuantity){
                                    val newQuantity = getItem(position).userEnteredQnty + 1
                                    getItem(position).userEnteredQnty = newQuantity
                                }else{
                                    Toast.makeText(binding.root.context, "Entered Quantity can't be greater then available", Toast.LENGTH_SHORT).show()
                                }
                                binding.tvQuantityWithMinus.text = getItem(position).userEnteredQnty.toString()
                            }else {
                                getItem(position).userEnteredQnty = 0
                            }
                        }


                    }
                }
            }
        }
    }

    private class ClaimItemDiffCallback : DiffUtil.ItemCallback<MrnDetailsItem>() {
        override fun areItemsTheSame(oldItem: MrnDetailsItem, newItem: MrnDetailsItem): Boolean {
            return oldItem.productDetails.id == newItem.productDetails.id
        }

        override fun areContentsTheSame(oldItem: MrnDetailsItem, newItem: MrnDetailsItem): Boolean {
            return oldItem == newItem
        }
    }
}