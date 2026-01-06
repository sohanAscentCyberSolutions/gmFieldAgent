package com.example.md3.view.commissioning.view


import Status.ERROR
import Status.LOADING
import Status.SUCCESS
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.md3.databinding.RootAnalysisBinding
import com.example.md3.utils.ViewUtils
import com.example.md3.view.commissioning.CommissioningViewModel
import org.koin.android.ext.android.inject

class RootAnalysisFragment : Fragment() {

    private var _binding: RootAnalysisBinding? = null
    private val commissioningViewModel : CommissioningViewModel by inject()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RootAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.topAppBar.title = "Root Analysis"
        binding.btnLayout.btnLogin.text = "Submit"


        binding.toolbar.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnLayout.btnLogin.setOnClickListener {
            submitDataToApi()
        }
        commissioningViewModel.getSubmitRootAnalysisLiveData.observe(viewLifecycleOwner){
            if(!it.isResponseHandled()){
                when(it.status){
                    SUCCESS -> {
                        findNavController().popBackStack()
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }
        }
    }



    private fun submitDataToApi() {
        val whyOne = binding.whyOne.text.toString().trim()
        val whyTwo = binding.whyTwo.text.toString().trim()
        val whyThree = binding.whyThree.text.toString().trim()

        if (whyOne.isEmpty()) {
            binding.whyOne.error = "Field cannot be empty"
            return
        }
        if (whyTwo.isEmpty()) {
            binding.whyTwo.error = "Field cannot be empty"
            return
        }
        if (whyThree.isEmpty()) {
            binding.whyThree.error = "Field cannot be empty"
            return
        }

//        commissioningViewModel.submitRootAnalysis("",whyOne,whyTwo,whyThree)
        ViewUtils.showSnackbar(binding.btnLayout.btnLogin,"Root Analysis Submitted")
        findNavController().popBackStack()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
