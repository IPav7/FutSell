package com.example.futsell.ui.main

import android.os.Bundle
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    inline fun <reified Callback> sendResult(block: Callback.() -> Unit) {
        val parent = parentFragment
        if (parent is Callback) {
            block(parent)
        } else {
            val target = targetFragment
            if (target is Callback) {
                block(target)
            } else {
                val activity = requireActivity()
                if (activity is Callback) {
                    block(activity)
                }
            }
        }
    }
}