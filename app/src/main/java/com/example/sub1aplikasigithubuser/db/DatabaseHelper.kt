package com.example.sub1aplikasigithubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "userDB"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE ${DatabaseContract.FavoriteColumns.TABLE_NAME}" +
                " (${DatabaseContract.FavoriteColumns.USERNAME} TEXT PRIMARY KEY  NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.NAME} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.AVATAR} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.COMPANY} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.LOCATION} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.REPOSITORY} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.FAVORITE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $DatabaseContract.FavoriteColumns.TABLE_NAME")
        onCreate(db)
    }
}