package com.example.md3.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.md3.databinding.DismissBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DismissBottomSheet(
    private val isForCheckSheet: Boolean,
    private val isForVisitForm : Boolean = false,
    private val onDismiss: (Boolean) -> Unit,
) : BottomSheetDialogFragment() {

    private var _binding: DismissBottomsheetBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DismissBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnReject.setOnClickListener {
            onDismiss(true)
            dismiss()
        }

        binding.btnObservationcantbeedit.setOnClickListener {
            onDismiss(false)
            dismiss()
        }

        if (isForCheckSheet) {
            binding.textTitle.text = "Checksheet changes are not saved, are you sure?"
            binding.btnReject.text = "Yes go back"
            binding.btnObservationcantbeedit.text = "Cancel"
        } else if(isForVisitForm){
            binding.textTitle.text = "All changes are not saved, are you sure?"
            binding.btnReject.text = "Yes go back"
            binding.btnObservationcantbeedit.text = "Cancel"
        }else {
            binding.textTitle.text = "Are you sure you want delete ?"
            binding.btnReject.text = "Yes "
            binding.btnObservationcantbeedit.text = "No"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
