package com.example.md3.view.breakdown.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.md3.R
import com.example.md3.data.model.mrn.MrnResult
import com.example.md3.databinding.FragmentPartBinding
import com.example.md3.databinding.SupplerListItemLayoutBinding
import com.example.md3.utils.GenericPagingAdapter
import com.example.md3.utils.SharedViewModel
import com.example.md3.view.commissioning.CommissioningViewModel
import org.koin.android.ext.android.inject

class PartsFragment : Fragment() {
    private lateinit var supplierAdapter: GenericPagingAdapter<MrnResult>
    private lateinit var sharedViewModel: SharedViewModel
    private val commissioningViewModel: CommissioningViewModel by inject()
    private val selectedItems: MutableList<MrnResult> = mutableListOf()
    private var _binding: FragmentPartBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPartBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        initToolbar()
        initRecyclerview()
        initAdapters()
        observe()



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { commissioningViewModel.searchSupplier(hashMapOf("query" to it)) }
                return true
            }
        })


        binding.addFab.setOnClickListener {
            sharedViewModel.setSelectedPartList(selectedItems)
            findNavController().popBackStack()
        }
    }

    private fun observe() {
        commissioningViewModel.getAllSuppliers.observe(viewLifecycleOwner){
            supplierAdapter.submitData(lifecycle, it)
        }
    }

    private fun initToolbar() {
        binding.toolbarlayout.apply {
            binding.topAppBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }



    private fun initRecyclerview() {
        binding.recyclerViewLayout.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    private fun initAdapters() {
        supplierAdapter = GenericPagingAdapter(
            R.layout.suppler_list_item_layout,
            { view, item, position ->
                val binding = SupplerListItemLayoutBinding.bind(view)
                binding.tvProductSerial.text = item.productSerial
                binding.tvProductName.text = item.partName



                binding.addcheckbox.isChecked = selectedItems.contains(item)

                binding.addcheckbox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedItems.add(item)
                    } else {
                        selectedItems.remove(item)
                    }
                    updateFabCount()
                }
            },
            { item ->

            }
        )
        binding.recyclerViewLayout.recyclerView.adapter = supplierAdapter
    }






    private fun updateFabCount() {
        binding.addFab.isVisible = selectedItems.isNotEmpty() // Make sure FAB is visible if there are selected items
        val count = selectedItems.size
        binding.addFab.text = "Add ($count)"
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PartsFragment().apply {

            }
    }
}