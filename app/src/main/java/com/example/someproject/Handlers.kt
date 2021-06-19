package com.example.someproject

interface CustomDialogListener {
    fun onButtonClickListener(articleRu: String, articleBy: String)
}

interface RcItemClickListener {
    fun onRcItemClickListener(article: String);
}

interface SortDialogListeners {
    fun onSortAscClickListener()
    fun onSortDescClickListener()
}


interface SettingsDialogListeners {
    fun onLanguageChanged(ru: Boolean)
    fun onThemeChanged(light: Boolean)
}