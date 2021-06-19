package com.example.someproject.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DbHelper(context: Context, private val dbName: String) : SQLiteOpenHelper(context, dbName, null, DbNames.DATABASE_VERSION ) {

    private val _code = "DbHelper"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DbNames.SQL_CREATE_TABLE_ARTICLES)
        db?.execSQL(DbNames.SQL_CREATE_TABLE_PICTURES)
        db?.execSQL(DbNames.SQL_CREATE_TABLE_PARAGRAPHS)
        db?.execSQL(DbNames.SQL_CREATE_TABLE_ARTICLECONTENT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL(DbNames.SQL_DELETE_TABLE_ARTICLES)
        db?.execSQL(DbNames.SQL_DELETE_TABLE_PICTURES)
        db?.execSQL(DbNames.SQL_DELETE_TABLE_PARAGRAPHS)
        db?.execSQL(DbNames.SQL_DELETE_TABLE_ARTICLECONTENT)

        onCreate(db)


    }

}