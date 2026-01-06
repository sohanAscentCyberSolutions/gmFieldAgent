package com.example.md3.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import com.example.md3.databinding.AcceptAssignmentAlertBoxBinding
import com.example.md3.databinding.AnotherCaseAlertBoxBinding
import com.example.md3.databinding.JounaryStartedAlertBoxBinding
import com.example.md3.databinding.RejectAlertBoxBinding
import com.example.md3.databinding.SubmitVisitDailogBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ViewKotlinUtils {


    fun navigationAlertDialog(
        context: Context,
        positiveButtonClickListener: () -> Unit,
        negativeButtonClickListener: () -> Unit,
        title: String? = null,
        subHeading: String? = null,
        positiveBtnName: String? = null,
        negativeBtnName: String? = null,
        isForConfirmation: Boolean? = false
    ) {
        val binding = JounaryStartedAlertBoxBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)

        val dialog = builder.create()
        title?.let {
            binding.tvTitle.text = title
        }


        subHeading?.let {
            binding.tvSubHeading.text = subHeading
        }

        positiveBtnName?.let {
            binding.btnPositive.text = positiveBtnName
        }

        if(isForConfirmation == true){
            binding.btnNegative.icon = null
        }
        negativeBtnName?.let {
            binding.btnNegative.text = negativeBtnName

        }



        binding.btnPositive.setOnClickListener {
            dialog.dismiss()
            positiveButtonClickListener.invoke()
        }

        binding.btnNegative.setOnClickListener {
            dialog.dismiss()
            negativeButtonClickListener.invoke()
        }

        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    fun navigationAnotherCaseDialogBox(
        context: Context,
        positiveButtonClickListener: () -> Unit,
        negativeButtonClickListener: () -> Unit,
        title: String? = null,
        subHeading: String? = null,
        positiveBtnName: String? = null,
        negativeBtnName: String? = null
    ) {
        val binding = AnotherCaseAlertBoxBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)

        val dialog = builder.create()
        title?.let {
            binding.tvTitle.text = title
        }

        subHeading?.let {
            binding.tvSubHeading.text = subHeading
        }

        positiveBtnName?.let {
            binding.btnPositive.text = positiveBtnName
        }

        negativeBtnName?.let {
            binding.tvCancel.text = negativeBtnName
        }


        binding.btnPositive.setOnClickListener {
            dialog.dismiss()
            positiveButtonClickListener.invoke()
        }

        binding.tvCancel.setOnClickListener {
            dialog.dismiss()
            negativeButtonClickListener.invoke()
        }

        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    fun assignmentAlertDialog(
        context: Context,
        positiveButtonClickListener: () -> Unit,
        negativeButtonClickListener: () -> Unit
    ) {
        val binding = AcceptAssignmentAlertBoxBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)

        val dialog = builder.create()

        binding.btnPositive.setOnClickListener {
            dialog.dismiss()
            positiveButtonClickListener.invoke()
        }

        binding.tvCancel.setOnClickListener {
            dialog.dismiss()
            negativeButtonClickListener.invoke()
        }

        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }




    fun submitAssignmentAlert(
        context: Context,
        positiveButtonClickListener: () -> Unit,
        negativeButtonClickListener: () -> Unit
    ) {
        val binding = SubmitVisitDailogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)

        val dialog = builder.create()

        binding.btnPositive.setOnClickListener {
            dialog.dismiss()
            positiveButtonClickListener.invoke()
        }

        binding.tvCancel.setOnClickListener {
            dialog.dismiss()
            negativeButtonClickListener.invoke()
        }

        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }







    fun rejectAlertDialog(
        context: Context,
        positiveButtonClickListener: (String) -> Unit,
        negativeButtonClickListener: () -> Unit
    ) {
        val binding = RejectAlertBoxBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)

        val dialog = builder.create()

        binding.btnPositive.setOnClickListener {
            dialog.dismiss()
            positiveButtonClickListener.invoke(binding.etInput.text.toString())
        }

        binding.tvCancel.setOnClickListener {
            dialog.dismiss()
            negativeButtonClickListener.invoke()
        }

        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    fun showSnackbarWithAction(
        view: View,
        message: String,
        actionText: String,
        action: () -> Unit
    ) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction(actionText) {
                action.invoke()
            }
            .show()
    }


    fun Date.toFormattedTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(this)
    }

    fun Date.toFormattedDate(): String {
        val dataFormat = SimpleDateFormat(TimePickerUtils.DATE_FORMAT_FOR_DISPLAY, Locale.getDefault())
        return dataFormat.format(this)
    }







}

