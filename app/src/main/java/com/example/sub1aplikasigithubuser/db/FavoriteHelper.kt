package com.example.sub1aplikasigithubuser.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

class FavoriteHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private var database: SQLiteDatabase = dataBaseHelper.writableDatabase

    companion object {
        private const val DATABASE_TABLE = DatabaseContract.FavoriteColumns.TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null
        fun getInstance(context: Context): FavoriteHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavoriteHelper(context)
        }
    }

    // get access to write database
    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    // close the database connection
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    //get all data in database
    fun queryAll(): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "${DatabaseContract.FavoriteColumns.USERNAME} ASC"
        )
    }

    // get data by id
    fun queryById(id: String): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                "${DatabaseContract.FavoriteColumns.USERNAME} = ?",
                arrayOf(id),
                null,
                null,
                null,
                null
        )
    }

    // like the name is for insert data to database
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    // this for update data
    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "${DatabaseContract.FavoriteColumns.USERNAME} = ?", arrayOf(id))
    }

    // and this for delete
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "${DatabaseContract.FavoriteColumns.USERNAME} = '$id'", null)
    }

}