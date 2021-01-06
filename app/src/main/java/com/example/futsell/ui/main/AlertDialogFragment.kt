package com.example.futsell.ui.main

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.example.futsell.R

class AlertDialogFragment(private val onShowedListener: (() -> Unit)? = null) :
    BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = arguments?.getString(KEY_STRING_MESSAGE)
        val messageId = arguments?.getInt(KEY_ID_MESSAGE) ?: R.string.unknown_error


        return AlertDialog.Builder(requireContext())
            .setPositiveButton(android.R.string.ok) { _, _ ->
                onShowedListener?.invoke()
            }
            .run {
                message?.let { setMessage(it) } ?: run {
                    setMessage(messageId)
                }
            }
            .create()
    }

    companion object {
        private const val KEY_ID_MESSAGE = "KEY_ID_MESSAGE"
        private const val KEY_STRING_MESSAGE = "KEY_STRING_MESSAGE"

        fun getInstance(
            @StringRes messageId: Int,
            onShowedListener: (() -> Unit)? = null
        ): AlertDialogFragment =
            AlertDialogFragment(onShowedListener).apply {
                arguments = Bundle().apply {
                    putInt(KEY_ID_MESSAGE, messageId)
                }
                isCancelable = false
            }

        fun getInstance(
            message: String,
            onShowedListener: (() -> Unit)? = null
        ): AlertDialogFragment =
            AlertDialogFragment(onShowedListener).apply {
                arguments = Bundle().apply {
                    putString(KEY_STRING_MESSAGE, message)
                }
                isCancelable = false
            }
    }

}