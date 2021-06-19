package com.example.someproject

import DbManager
import DbNames
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import com.example.someproject.databinding.ActivityArticleBinding
import com.example.someproject.db.DbBitmapUtility
import java.io.InputStream

class ArticleActivity : AppCompatActivity() {

    private lateinit var db: DbManager
    private lateinit var binding: ActivityArticleBinding
    private lateinit var articleTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DbManager(this)
        db.openDb()

        articleTitle = intent.getStringExtra("name")!!
        supportActionBar?.title = articleTitle

        val ru = intent.getBooleanExtra("ru", true)

        pullContent(articleTitle, ru)


        binding.buttonImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == 1) {
            val inputStream = contentResolver.openInputStream(data?.data!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val util = DbBitmapUtility()
            val bytes = util.getBytes(bitmap)

            val imageId = db.addImage(bytes)
            db.addImageToArticle(articleTitle, imageId)

            val iv = ImageView(this)
            iv.setImageBitmap(db.getImageById(imageId))
            binding.container.addView(iv)
        }
    }

    private fun pullContent(article: String, ru: Boolean) {
        val content = db.getArticleContent(article, ru)

        content.forEach {

            if (it.img == null) {
                val tv = TextView(this)
                tv.text = it.name
                binding.container.addView(tv)

                val params = tv.layoutParams as ViewGroup.MarginLayoutParams
                params.setMargins(10)
                tv.layoutParams = params
            }
            else {
                val iv = ImageView(this)
                iv.setImageBitmap(it.img)
                binding.container.addView(iv)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        db.closeDb()
    }
}