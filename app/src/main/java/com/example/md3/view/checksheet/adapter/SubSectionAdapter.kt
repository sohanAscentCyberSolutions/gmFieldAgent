package com.example.md3.view.checksheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.md3.data.model.checksheet.SubSection
import com.example.md3.databinding.SubsectionLayoutBinding
import com.example.md3.view.checksheet.viewHolder.SubSectionViewHolder


class SubSectionAdapter(private val showCount: Boolean , val isDisabled : Boolean) :
    ListAdapter<SubSection, RecyclerView.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SubsectionLayoutBinding.inflate(inflater, parent, false)
        return  SubSectionViewHolder(binding ,isDisabled)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is SubSectionViewHolder -> holder.bind(item,showCount)
        }
    }

//    override fun getItemViewType(position: Int): Int {
//        val item = getItem(position)
//
//        return if(viewType == VIEW_TYPE_CHIP){
//            VIEW_TYPE_CHIP
//        }else{
//            VIEW_TYPE_NORMAL
//        }
//    }


    companion object {
        const val VIEW_TYPE_NORMAL = 0
        const val VIEW_TYPE_CHIP = 1

        private val COMPARATOR = object : DiffUtil.ItemCallback<SubSection>() {
            override fun areItemsTheSame(oldItem: SubSection, newItem: SubSection): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SubSection, newItem: SubSection): Boolean =
                oldItem == newItem
        }
    }
}



