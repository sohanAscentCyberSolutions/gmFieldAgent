package com.example.md3.view.breakdown.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.md3.R

class OuterAdapter(var outerList: List<OuterData>, private val listener: OnIncreaseItemClickListener
                   , private val listenerInner: InnerAdapter.OnRemoveItemClickListener) :
    RecyclerView.Adapter<OuterAdapter.OuterViewHolder>() {

    interface OnIncreaseItemClickListener {
        fun onAddItemClick(outerPosition: Int, outerData: OuterData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OuterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.claim_part_list_item, parent, false)
        return OuterViewHolder(view)
    }

    override fun onBindViewHolder(holder: OuterViewHolder, position: Int) {
        val outerData = outerList[position]
        holder.bind(outerData)
    }

    override fun getItemCount(): Int {
        return outerList.size
    }

    inner class OuterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.partName)
        private val addAnotherBatch: TextView = itemView.findViewById(R.id.addAnotherBatch)
        private val innerRecyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)

        fun bind(outerData: OuterData) {
            titleTextView.text = outerData.title
            innerRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            innerRecyclerView.adapter = InnerAdapter(outerData.innerList, listenerInner, position)
            addAnotherBatch.setOnClickListener {
                listener.onAddItemClick(adapterPosition, outerData)
            }
        }
    }
}

data class OuterData(val title: String, val innerList: List<String>)
