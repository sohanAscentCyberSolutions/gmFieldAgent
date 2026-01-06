package com.example.md3.view.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.md3.R
import com.example.md3.data.model.home.CalendarCases
import com.example.md3.databinding.Example5EventItemViewBinding
import com.example.md3.utils.CASETYPE


class AllCasesAdapter : RecyclerView.Adapter<AllCasesAdapter.Example5FlightsViewHolder>() {
    val cases = mutableListOf<CalendarCases>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Example5FlightsViewHolder {
        return Example5FlightsViewHolder(
            Example5EventItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun onBindViewHolder(viewHolder: Example5FlightsViewHolder, position: Int) {
        viewHolder.bind(cases[position])
    }

    override fun getItemCount(): Int = cases.size

    inner class Example5FlightsViewHolder(val binding: Example5EventItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(flight: CalendarCases) {

            if(flight.caseType.property == CASETYPE.BREAKDOWN.property){
                binding.root.setBackgroundColor(binding.root.context.getColor(R.color.md_theme_yellow))
                binding.imageView7.setImageResource(R.drawable.breakdown)
            }else{
                binding.root.setBackgroundColor(binding.root.context.getColor(R.color.md_theme_primaryContainer))
                binding.imageView7.setImageResource(R.drawable.commissioninglogo)
            }

//            binding.tvDate.text = CalendarCasesDateTimeFormatter.format(flight.time)
            binding.textTitle.text =  flight.caseType.property
            binding.textCaseId.text = flight.caseId

        }
    }
}
