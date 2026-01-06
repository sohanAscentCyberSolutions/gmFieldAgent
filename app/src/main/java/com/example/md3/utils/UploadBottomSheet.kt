package com.asap.codenicely.pdf.gstinvoicing.free.mobile.easy.gst.invoice.quick.quickinvoice.gstinvoicing.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.md3.MainActivity
import com.example.md3.databinding.UploadSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UploadBottomSheet() : BottomSheetDialogFragment() {

    private var _binding: UploadSheetLayoutBinding? = null
    private val binding get() = _binding!!

    companion object{
         const val IS_FOR_PROFILE = "is_for_profile"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UploadSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.cameraButton.root.setOnClickListener {
            (requireActivity() as MainActivity).showCamera()
            dismiss()
        }

        binding.galleryButton.root.setOnClickListener {
            (requireActivity() as MainActivity).openGallery()
            dismiss()
        }



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
