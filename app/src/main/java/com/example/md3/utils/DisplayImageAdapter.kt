package com.example.md3.utils




import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.md3.databinding.DisplayCardLayoutBinding
import com.example.md3.utils.glide.GlideImageLoader


class DisplayImageAdapter(private val context: Context) : ListAdapter<String, DisplayImageAdapter.ViewHolder>(StringDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DisplayCardLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUri = getItem(position)
        holder.bind(imageUri)
    }

    inner class ViewHolder(private val binding: DisplayCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUri: String) {
            GlideImageLoader(context).loadImage(imageUri , binding.ivUpload , ProgressBar(context))
        }
    }
}


class StringDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}