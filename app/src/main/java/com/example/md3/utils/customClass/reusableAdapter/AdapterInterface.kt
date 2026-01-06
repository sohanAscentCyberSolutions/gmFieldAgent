package com.example.foodii.adapter

import androidx.recyclerview.widget.RecyclerView


interface AdapterInterface<T> {

    // set layout
    fun setLayout(layout: Int): ReuseAdapter<T>

    // filterable
//    fun filterable(): Adapter<T>

    // append data
    fun addData(items: List<T>): ReuseAdapter<T>

    // realtime change
    fun updateData(item: T): ReuseAdapter<T>

    // adapter callback
    fun adapterCallback(adapterCallback: AdapterCallback<T>): ReuseAdapter<T>

    // layout orientation
    fun isVerticalView(): ReuseAdapter<T>
    fun isHorizontalView(): ReuseAdapter<T>

    // layout orientation
    fun setGridLayout(count:Int ): ReuseAdapter<T>

    // build view
    fun build(recyclerView: RecyclerView): ReuseAdapter<T>

    //deleteView
    fun deleteItem(item: T) : ReuseAdapter<T>

}