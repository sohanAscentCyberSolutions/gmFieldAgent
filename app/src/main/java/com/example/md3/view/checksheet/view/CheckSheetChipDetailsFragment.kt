package com.example.md3.view.checksheet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.md3.R
import com.example.md3.databinding.FragmentCheckSheetChipDetailsBinding
import com.example.md3.utils.SharedViewModel
import com.example.md3.view.checksheet.adapter.SubSectionAdapter
import com.google.android.material.chip.Chip

class CheckSheetChipDetailsFragment : Fragment() {
    private var _binding: FragmentCheckSheetChipDetailsBinding? = null
    private lateinit var sharedViewModel: SharedViewModel
    private val args: CheckSheetDetailsFragmentArgs by navArgs()
    var subSectionAdapter = SubSectionAdapter(true, true)
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckSheetChipDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        initRv()
        initToolbar()
        observe()

        binding.btnSubmit.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initRv() {
        when (args.actionType) {
            context?.getString(R.string.view_action_section) -> {
                binding.apply {
                    RvForSubSector.layoutManager = LinearLayoutManager(requireContext())
                    RvForSubSector.adapter = subSectionAdapter
                }
            }
        }
    }

    private fun observe() {
        sharedViewModel.setSectorMutableLiveData.observe(viewLifecycleOwner) {data ->

            val defaultChip = LayoutInflater.from(binding.root.context).inflate(
                R.layout.choice_chip_layout,
                binding.chipGroup,
                false
            ) as Chip
            defaultChip.text = "All"
            defaultChip.setOnClickListener {
                subSectionAdapter.submitList(data.subSections)
                binding.RvForSubSector.scrollToPosition(0)
            }
            binding.chipGroup.addView(defaultChip)

            data.subSections.forEach { subSection ->
                val chip = LayoutInflater.from(binding.root.context).inflate(
                    R.layout.choice_chip_layout,
                    binding.chipGroup,
                    false
                ) as Chip
                chip.text = subSection.title
                chip.setOnClickListener {
                    subSectionAdapter.submitList(listOf(subSection))
                    binding.RvForSubSector.scrollToPosition(0)
                }
                binding.chipGroup.addView(chip)
            }

            defaultChip.performClick()


        }
    }

    private fun initToolbar() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.topAppBar.title = args.title
    }


    private fun initview() {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
