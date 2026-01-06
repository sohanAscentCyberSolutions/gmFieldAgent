package com.example.md3.view.breakdown.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.md3.data.model.Identifier
import com.example.md3.databinding.EdittextAddQuantityItemBinding


class ClaimPartChildAdapter(
    private val type: String,
    private val productQty: Int,
    private val onCurrentListChanged: (MutableList<Identifier>?) -> Unit
) : ListAdapter<Identifier, ClaimPartChildAdapter.IdentifierViewHolder>(IdentifierDiffCallback()) {

    private val TAG = "ClaimPartChildAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdentifierViewHolder {
        val binding = EdittextAddQuantityItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IdentifierViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IdentifierViewHolder, position: Int) {
        val identifier = getItem(position)
        holder.bind(identifier)
    }

    inner class IdentifierViewHolder(private val binding: EdittextAddQuantityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(identifier: Identifier) {

            when (type) {
                "Batch Number" -> {
                    identifier.quantity = 1
                    binding.batchNumberOfNewPart.hint = "Batch Number of new part"
                    binding.etCustomerPhone.setHint("Enter Batch Number")
                    binding.quantityCard.visibility = View.VISIBLE
                    binding.removeButton.visibility = View.GONE
                }

                "Serial Number" -> {
                    identifier.quantity = 1
                    binding.batchNumberOfNewPart.hint = "Serial number of new product"
                    binding.etCustomerPhone.setHint("Enter Serial Number")
                    binding.removeButton.visibility = View.VISIBLE
                    binding.quantityCard.visibility = View.GONE
                }
            }


            binding.etCustomerPhone.setText(identifier.identifier)
            binding.tvQuantity.text = identifier.quantity.toString()

            binding.removeButton.setOnClickListener {
                removeItem(absoluteAdapterPosition)
            }


            binding.btnMinus.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentQuantity = getItem(position).quantity
                    if (currentQuantity > 0) {
                        val newQuantity = currentQuantity - 1
                        getItem(position).quantity = newQuantity
                        binding.tvQuantity.text = getItem(position).quantity.toString()
                    }
                }
            }

            binding.btnPlus.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val newQuantity = getItem(position).quantity + 1
                    getItem(position).quantity = newQuantity
                    binding.tvQuantity.text = getItem(position).quantity.toString()
                }
            }

            binding.etCustomerPhone.doAfterTextChanged {
                getItem(adapterPosition).identifier = it.toString()
            }
        }
    }


    override fun submitList(list: MutableList<Identifier>?) {
        onCurrentListChanged(list)
        super.submitList(list)
    }


    private fun removeItem(position: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(position)
        submitList(newList)
        notifyDataSetChanged()
    }


    class IdentifierDiffCallback : DiffUtil.ItemCallback<Identifier>() {
        override fun areItemsTheSame(oldItem: Identifier, newItem: Identifier): Boolean {
            return oldItem.identifier == newItem.identifier
        }

        override fun areContentsTheSame(oldItem: Identifier, newItem: Identifier): Boolean {
            return oldItem == newItem
        }
    }
}

