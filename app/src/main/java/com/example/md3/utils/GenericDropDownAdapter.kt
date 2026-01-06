package com.asap.codenicely.pdf.gstinvoicing.free.mobile.easy.gst.invoice.quick.quickinvoice.gstinvoicing.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter


class GenericDropDownAdapter<T>(
    context: Context,
    private val resource: Int,
    private val items: List<T>,
    private val layoutBinder: (view: View, item: T) -> Unit
) : ArrayAdapter<T>(context, resource, items) {

    private var selectedPosition = -1

    fun getSelectedPosition(): Int = selectedPosition

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        parent.setPadding(0, 0, 0, 0)
        return getView(position, convertView, parent)
    }

    override

    fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        layoutBinder(view, getItem(position)!!)
        return view
    }
}