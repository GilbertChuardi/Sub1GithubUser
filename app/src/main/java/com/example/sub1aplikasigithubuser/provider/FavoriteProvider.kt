package com.example.sub1aplikasigithubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.sub1aplikasigithubuser.db.DatabaseContract
import com.example.sub1aplikasigithubuser.db.FavoriteHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private lateinit var favHelper: FavoriteHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.FavoriteColumns.TABLE_NAME, FAVORITE)
            sUriMatcher.addURI(
                    DatabaseContract.AUTHORITY,
                    "${
                        DatabaseContract.FavoriteColumns.TABLE_NAME}/#",
                    FAVORITE_ID
            )
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> favHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(DatabaseContract.FavoriteColumns.CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAVORITE) {
            sUriMatcher.match(uri) -> favHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(DatabaseContract.FavoriteColumns.CONTENT_URI, null)
        return Uri.parse("${DatabaseContract.FavoriteColumns.CONTENT_URI}/$added")
    }

    override fun onCreate(): Boolean {
        favHelper = FavoriteHelper.getInstance(context as Context)
        favHelper.open()
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> favHelper.queryAll()
            FAVORITE_ID -> favHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val updated: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> favHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(DatabaseContract.FavoriteColumns.CONTENT_URI, null)
        return updated
    }
}