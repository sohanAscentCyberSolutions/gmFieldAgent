package com.example.md3.view.checksheet.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.md3.data.model.checksheet.Section
import com.example.md3.databinding.ViewChecksheetLayoutBinding
import com.example.md3.utils.KotlinFunctions

class SubmitCheckSheetAdapter(private val onDismissCallback: (Section) -> Unit) : ListAdapter<Section, SubmitCheckSheetAdapter.SubmitCheckSheetViewHolder>(SubmitCheckSheetItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmitCheckSheetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewChecksheetLayoutBinding.inflate(inflater, parent, false)
        return SubmitCheckSheetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubmitCheckSheetViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)

    }

    inner class SubmitCheckSheetViewHolder(private val binding: ViewChecksheetLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Section, position: Int) {

            binding.root.setOnClickListener {
                onDismissCallback(item)
            }

            var count = position
            binding.count.text = count.plus(1).toString()
            binding.title.text = item.section
            val totalFields = item.countNullFields()
            val inProgress = item.countNonNullFields()
            binding.tvCompleteProgress.text = totalFields.toString()
            binding.textViewInProgress.text = inProgress.toString()
            val nonNullFieldsTimes = inProgress.times(100).toDouble()
            val nullFieldsTimes = totalFields.times(100).toDouble()
            val doneProgress = item.calculatePercentage(nonNullFieldsTimes, nullFieldsTimes).toInt()
            val progressColor = KotlinFunctions.setProgressAndColor(doneProgress)
            binding.progressBar.progress = doneProgress
            binding.progressBar.setIndicatorColor(ContextCompat.getColor(binding.root.context, progressColor))

        }
    }
}


class SubmitCheckSheetItemDiffCallback : DiffUtil.ItemCallback<Section>() {
    override fun areItemsTheSame(oldItem: Section, newItem: Section): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Section, newItem: Section): Boolean {
        return oldItem == newItem
    }
}
