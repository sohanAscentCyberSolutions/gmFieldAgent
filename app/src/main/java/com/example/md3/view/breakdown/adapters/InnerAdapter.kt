package com.example.md3.view.breakdown.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.md3.R

class InnerAdapter(private val innerList: List<String>, private val listener: OnRemoveItemClickListener, private val outerPosition: Int) :
    RecyclerView.Adapter<InnerAdapter.InnerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.edittext_add_quantity_item, parent, false)
        return InnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        val innerItem = innerList[position]
        holder.bind(innerItem)
    }

    override fun getItemCount(): Int {
        return innerList.size
    }

    inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val innerTextView: TextView = itemView.findViewById(R.id.tvQuantity)
        private val removeButton: TextView = itemView.findViewById(R.id.removeButton)

        fun bind(innerItem: String) {
            innerTextView.text = innerItem
            removeButton.setOnClickListener {
                listener.onRemoveItemClick(outerPosition, adapterPosition)
            }
        }
    }

    interface OnRemoveItemClickListener {
        fun onRemoveItemClick(outerPosition: Int, innerPosition: Int)
    }
}