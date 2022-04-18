package com.example.sub1aplikasigithubuser.backend.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

class FavoriteHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = DatabaseContract.FavoriteColumns.TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavoriteHelper(context)
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor = database.query(
        DATABASE_TABLE,
        null,
        null,
        null,
        null,
        null,
        "${DatabaseContract.FavoriteColumns.USERNAME} ASC"
    )

    fun queryById(id: String): Cursor = database.query(
        DATABASE_TABLE,
        null,
        "${DatabaseContract.FavoriteColumns.USERNAME} = ?",
        arrayOf(id),
        null,
        null,
        null,
        null
    )

    fun insert(values: ContentValues?): Long = database.insert(DATABASE_TABLE, null, values)

    fun update(id: String, values: ContentValues?): Int = database.update(
        DATABASE_TABLE,
        values,
        "${DatabaseContract.FavoriteColumns.USERNAME} = ?",
        arrayOf(id)
    )

    fun deleteById(id: String): Int = database.delete(
        DATABASE_TABLE,
        "${DatabaseContract.FavoriteColumns.USERNAME} = '$id'",
        null
    )

}