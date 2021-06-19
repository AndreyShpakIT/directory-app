package com.example.someproject

import DbManager
import DbNames
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.someproject.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), RcItemClickListener, CustomDialogListener, SortDialogListeners, SettingsDialogListeners {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DbManager
    private lateinit var adapter: RcAdapter
    private var isRussianLanguage = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Справочник"

        db = DbManager(this)
        db.openDb()

        initHandlers()

        val list = db.getArticles()

        adapter = RcAdapter(list, this)

        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter
    }

    override fun onRcItemClickListener(article: String) {
        val newActivity = Intent(this, ArticleActivity::class.java)
        newActivity.putExtra("name", article)
        newActivity.putExtra("ru", isRussianLanguage)
        startActivity(newActivity)
    }

    private fun initHandlers() {
        binding.floatingButtonAddArticle.setOnClickListener {
            val dialog = CustomDialog(this, this)
            dialog.showDialog()
        }

        binding.imageButtonSort.setOnClickListener {
            val dialog = SortDialog(this, this)
            dialog.showDialog()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {

                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                adapter.filter(s)
                return false
            }
        })

        binding.imageButtonSettings.setOnClickListener {
            val dialog = SettingsDialog(this, this, isRussianLanguage, true)
            dialog.showDialog()
        }
    }

    override fun onButtonClickListener(articleRu: String, articleBy: String) {
        if (articleRu.isEmpty() || articleBy.isEmpty()) {
            return
        }
        db.addArticle(articleRu, articleBy)
        if (isRussianLanguage) {
            adapter.addItem(articleRu)
        } else {
            adapter.addItem(articleBy)
        }
    }

    override fun onSortAscClickListener() {
        adapter.sortAsc()
    }

    override fun onSortDescClickListener() {
        adapter.sortDesc()
    }

    override fun onLanguageChanged(ru: Boolean) {
        if (ru != isRussianLanguage) {
            isRussianLanguage = ru

            val list = db.getArticles(ru)
            adapter.updateAdapter(list)
        }
    }

    override fun onThemeChanged(light: Boolean) {
        TODO("Not yet implemented")
    }
}