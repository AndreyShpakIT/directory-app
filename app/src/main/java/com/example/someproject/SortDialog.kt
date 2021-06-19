package com.example.someproject

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.someproject.databinding.CustomDialogBinding

class SortDialog(private val context: Context, private val clickListeners: SortDialogListeners) {

    private lateinit var binding: CustomDialogBinding

    fun showDialog() {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_sort)

        val btnAsc: Button = dialog.findViewById<View>(R.id.buttonSortAlphAsc) as Button
        btnAsc.setOnClickListener {
            clickListeners.onSortAscClickListener()
            dialog.dismiss()
        }

        val btnDesc: Button = dialog.findViewById<View>(R.id.buttonSortAlphDesc) as Button
        btnDesc.setOnClickListener {
            clickListeners.onSortDescClickListener()
            dialog.dismiss()
        }

        dialog.show()
    }
}