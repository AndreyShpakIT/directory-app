package com.example.someproject

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.someproject.databinding.CustomDialogBinding

class CustomDialog(private val context: Context, private val clickListeners: CustomDialogListener) {

    private lateinit var binding: CustomDialogBinding

    fun showDialog() {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)

        val edArticleRu = dialog.findViewById<View>(R.id.edArticleNameRu) as TextView
        val edArticleBy = dialog.findViewById<View>(R.id.edArticleNameBy) as TextView

        val btn: Button = dialog.findViewById<View>(R.id.buttonAddArticle) as Button
        btn.setOnClickListener {
            clickListeners.onButtonClickListener(edArticleRu.text.toString(), edArticleBy.text.toString())
            dialog.dismiss()
        }

        dialog.show()
    }
}