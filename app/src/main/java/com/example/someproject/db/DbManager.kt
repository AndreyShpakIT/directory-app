import android.R.attr.name
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.util.Log
import com.example.someproject.db.DbBitmapUtility
import com.example.someproject.db.DbHelper
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class DbManager(val context: Context, private val dbName: String = DbNames.DATABASE_NAME) {

    private val mExecutor: Executor = Executors.newSingleThreadExecutor()

    private val _code = "DbManager"
    private val dbHelper = DbHelper(context, dbName)
    var db: SQLiteDatabase? = null

    fun addArticle(articleRu: String, articleBy: String) : Boolean {

        if (getArticleIdByName(articleRu) != -1 || getArticleIdByName(articleBy) != -1) {
            return false
        }
        val values = ContentValues().apply {
            put(DbNames.FIELD_ARTICLE_NAME_RU, articleRu)
            put(DbNames.FIELD_ARTICLE_NAME_BY, articleBy)
        }

        db?.insert(DbNames.TABLE_ARTICLES, null, values)!! - 1
        return true
    }
    fun addImage(image: ByteArray) : Int {
        val cv = ContentValues()
        cv.put(DbNames.FIELD_PICTURE_IMG, image)
        db?.insert(DbNames.TABLE_PICTURES, null, cv)

        val sql = "SELECT ${DbNames.FIELD_PICTURE_ID} FROM ${DbNames.TABLE_PICTURES} ORDER BY ${DbNames.FIELD_PICTURE_ID} DESC LIMIT 1"
        val cursor = db?.rawQuery(sql, null)

        with(cursor) {
            while (this?.moveToNext()!!) {
                val id = getInt(getColumnIndex(DbNames.FIELD_PICTURE_ID))
                cursor?.close()
                return id
            }
        }
       throw Exception("-1")
    }
    fun addImageToArticle(article: String, imageId: Int) {

        val articleId = getArticleIdByName(article)

        val values = ContentValues().apply {
            put(DbNames.FIELD_ARTICLECONTENT_ARTICLE_ID, articleId)
            put(DbNames.FIELD_ARTICLECONTENT_PICTURE_ID, imageId)
        }

        db?.insert(DbNames.TABLE_ARTICLECONTENT, null, values)!! - 1
    }
    fun getImageById(imageId: Int) : Bitmap {

        val selection = "${DbNames.FIELD_PICTURE_ID} = ?"
        val selectionArgs = arrayOf(imageId.toString())

        val cursor = db?.query(DbNames.TABLE_PICTURES, null, selection, selectionArgs, null, null, null)

        with(cursor) {
            while (this?.moveToNext()!!) {

                val byteImg = getBlob(getColumnIndex(DbNames.FIELD_PICTURE_IMG))
                val util = DbBitmapUtility()
                val img = util.getImage(byteImg)
                return img
            }
        }
        throw Exception("---")
    }


    public fun getArticles(ru: Boolean = true) : ArrayList<String> {
        val dataList = ArrayList<String>()
        val cursor = db?.query(DbNames.TABLE_ARTICLES, null, null, null, null, null, null)

        var art: String = ""

        with(cursor) {
            while (this?.moveToNext()!!) {
                art = if (ru) {
                    getString(getColumnIndex(DbNames.FIELD_ARTICLE_NAME_RU)) ?: "-"
                } else {
                    getString(getColumnIndex(DbNames.FIELD_ARTICLE_NAME_BY)) ?: "-"
                }
                dataList.add(art)
            }
        }

        cursor?.close()
        return dataList
    }



    fun getArticleContent(article: String, ru: Boolean = true) : ArrayList<StructData> {

        val articleId = getArticleIdByName(article)

        var sql =
            "SELECT ArticleContentId, TextBy, TextRu FROM (SELECT * FROM ArticleContent WHERE ArticleId = $articleId) C JOIN  Paragraphs P ON C.ParagraphId = P.ParagraphId"

        var cursor = db?.rawQuery(sql, null)
        var content = ""
        val dataList = ArrayList<StructData>()

        with(cursor) {
            while (this?.moveToNext()!!) {

                content = if (ru) {
                    getString(getColumnIndex(DbNames.FIELD_PARAGRAPH_TEXT_RU))
                } else {
                    getString(getColumnIndex(DbNames.FIELD_PARAGRAPH_TEXT_BY))
                }
                if (content.isEmpty()) {
                    content = getString(getColumnIndex(DbNames.FIELD_ARTICLECONTENT_PICTURE_ID)) ?: "-"
                }
                val id = getInt(getColumnIndex(DbNames.FIELD_ARTICLECONTENT_ID))
                dataList.add(StructData(content, id))
            }
        }

        sql =
            "SELECT ArticleContentId, Img FROM (SELECT * FROM ArticleContent WHERE ArticleId = $articleId) C JOIN Pictures P ON C.PictureId = P.PictureId"
        cursor = db?.rawQuery(sql, null)

        with(cursor) {
            while (this?.moveToNext()!!) {

                val byteImg = getBlob(getColumnIndex(DbNames.FIELD_PICTURE_IMG))
                val util = DbBitmapUtility()
                val img = util.getImage(byteImg)

                val id = getInt(getColumnIndex(DbNames.FIELD_ARTICLECONTENT_ID))
                if (dataList.any { s -> s.id == id }) {
                    continue
                }
                dataList.add(StructData("", id, img))
            }
        }

        dataList.sortBy {
            it.id
        }

        return dataList
    }

    fun getArticleIdByName(article: String) : Int {

        val selection = "${DbNames.FIELD_ARTICLE_NAME_BY} = ? OR ${DbNames.FIELD_ARTICLE_NAME_RU} = ?"
        val selectionArgs = arrayOf(article, article)

        val cursor = db?.query(
            DbNames.TABLE_ARTICLES,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        if (cursor!!.count > 0) {
            with(cursor) {
                cursor.moveToFirst()
                return getInt(getColumnIndex(DbNames.FIELD_ARTICLE_ID))
            }
        }
        else {
            cursor.close()
            return -1
        }


    }

    fun openDb() {
        db = dbHelper.writableDatabase
        dbHelper.onCreate(db)

        Log.w(_code, "Открыто соединение с базой данных: dbName: $dbName")
    }
    fun closeDb() {
        dbHelper.close()
        Log.w(_code, "Закрыто соединение с базой данных: dbName: $dbName")
    }

    fun dropDb() {
        db?.execSQL(DbNames.SQL_DELETE_TABLE_ARTICLES)
        db?.execSQL(DbNames.SQL_DELETE_TABLE_PICTURES)
        db?.execSQL(DbNames.SQL_DELETE_TABLE_PARAGRAPHS)
        db?.execSQL(DbNames.SQL_DELETE_TABLE_ARTICLECONTENT)
        Log.w(_code, "База данных удалена")
    }
}

class StructData(var name: String, var id: Int, var img: Bitmap? = null) {
}