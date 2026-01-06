package com.example.md3.view.commissioning.view



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.md3.databinding.FailureReasonBinding
import com.example.md3.utils.SharedViewModel

class FailureReasonFragment : Fragment() {

    private var _binding: FailureReasonBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FailureReasonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]


        binding.toolbar.topAppBar.title = "Failure Reason"

        binding.toolbar.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnLayout.btnLogin.text = "Save"

        binding.btnLayout.btnLogin.setOnClickListener {
            if (validateInput()) {
                submitData()
            }
        }


        sharedViewModel.getSelectedFailureLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
               binding.etCustomerPhone.setText(it[0])
               binding.description.setText(it[1])
               binding.why3ET.setText(it[2])
               binding.why4ET.setText(it[3])
               binding.why5ET.setText(it[4])
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateInput(): Boolean {
        // Validate input fields here
        // Example: Check if TextInputEditText fields are not empty

        val why1Text = binding.etCustomerPhone.text.toString().trim()
        val why2Text = binding.description.text.toString().trim()
        // Add validation for other fields as needed

        if (why1Text.isEmpty()) {
            binding.etCustomerPhone.error = "Field cannot be empty"
            return false
        }

        if (why2Text.isEmpty()) {
            binding.description.error = "Field cannot be empty"
            return false
        }

        // Add validation for other fields as needed

        return true
    }

    private fun submitData() {
        val why1Text = binding.etCustomerPhone.text.toString().trim()
        val why2Text = binding.description.text.toString().trim()
        val why3Text = binding.why3ET.text.toString().trim()
        val why4Test = binding.why4ET.text.toString().trim()
        val why5Test = binding.why5ET.text.toString().trim()

        sharedViewModel.setSelectedFailureList(listOf(why1Text,why2Text,why3Text,why4Test,why5Test))
        findNavController().popBackStack()

    }
}
