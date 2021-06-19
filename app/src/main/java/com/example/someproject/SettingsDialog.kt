package com.example.someproject

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import com.example.someproject.databinding.CustomDialogBinding
import java.lang.Exception

class SettingsDialog(private val context: Context, private val listeners: SettingsDialogListeners, val ru: Boolean, val light: Boolean) {

    private lateinit var binding: CustomDialogBinding

    fun showDialog() {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.settings_layout)

        try {
            val rbLanguageRu = dialog.findViewById<View>(R.id.radioButtonRu) as RadioButton
            val rbLanguageBy = dialog.findViewById<View>(R.id.radioButtonBy) as RadioButton

            val rbLanguageThemeLight = dialog.findViewById<View>(R.id.radioButtonThemeLight) as RadioButton
            val rbLanguageThemeDark = dialog.findViewById<View>(R.id.radioButtonThemeDark) as RadioButton

            if (!ru) {
                rbLanguageBy.isChecked = true
            }

            if (!light) {
                rbLanguageThemeLight.isChecked = true
            }

            rbLanguageRu.setOnCheckedChangeListener { _, isChecked ->
                listeners.onLanguageChanged(isChecked)
            }
            rbLanguageThemeLight.setOnCheckedChangeListener { _, isChecked ->
                listeners.onLanguageChanged(isChecked)
            }
        }
        catch (e: Exception) {

        }

        dialog.show()
    }
}