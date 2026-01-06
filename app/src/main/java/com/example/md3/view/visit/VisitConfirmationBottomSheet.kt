package com.example.md3.view.visit



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.md3.databinding.ConfirmationBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class VisitConfirmationBottomSheet(private val onClick : () -> Unit) : BottomSheetDialogFragment() {

    private var _binding: ConfirmationBottomsheetBinding? = null
    private val binding get() = _binding!!

    companion object{
        const val TAG = "VisitConfirmationBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ConfirmationBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartWork.setOnClickListener {
            onClick()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


