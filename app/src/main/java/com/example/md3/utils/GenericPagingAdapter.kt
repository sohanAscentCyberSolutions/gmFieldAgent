package com.example.md3.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


class GenericPagingAdapter<T : Any>(
    private val layoutResId: Int,
    private val onBindData: (View, T, Int?) -> Unit,
    private val onItemClick: ((Any) -> Unit)?
) : PagingDataAdapter<T, GenericPagingAdapter.GenericViewHolder>(GenericDiffCallback<T>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return GenericViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        getItem(position)?.let { data -> onBindData(holder.itemView, data , position) }
    }

    class GenericViewHolder(itemView: View, onItemClick: ((Any) -> Unit)?) :
        RecyclerView.ViewHolder(itemView) {

        init {
//            if (onItemClick != null) {
//                itemView.setOnClickListener { onItemClick(itemView.tag) } // Attach data to tag
//            }
        }
    }

    class GenericDiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }
}