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
import com.example.md3.databinding.FragmentCheckSheetDetailsBinding
import com.example.md3.utils.SharedViewModel
import com.example.md3.view.checksheet.adapter.SectionAdapter

class CheckSheetDetailsFragment : Fragment() {
    private var _binding: FragmentCheckSheetDetailsBinding? = null
    private lateinit var sharedViewModel: SharedViewModel
    private val args: CheckSheetDetailsFragmentArgs by navArgs()
    var checkSheetParentAdapter = SectionAdapter(true)
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckSheetDetailsBinding.inflate(inflater, container, false)
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
            context?.getString(R.string.view_action_overall) -> {
                binding.apply {
                    parentRv.layoutManager = LinearLayoutManager(requireContext())
                    parentRv.adapter = checkSheetParentAdapter
                }
            }
        }
    }

    private fun observe() {
        sharedViewModel.setCheckSetMutableLiveData.observe(viewLifecycleOwner) {
            if(args.actionType == context?.getString(R.string.view_action_overall)){
                checkSheetParentAdapter.submitList(it.sections)
            }
        }
    }

    private fun initToolbar() {
        binding.toolbar.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.toolbar.topAppBar.title = args.title
    }


    private fun initview() {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
