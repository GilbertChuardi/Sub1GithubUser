package com.example.sub1aplikasigithubuser.backend.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.sub1aplikasigithubuser.backend.db.DatabaseContract
import com.example.sub1aplikasigithubuser.backend.db.FavoriteHelper

class FavoriteProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> favoriteHelper.queryAll()
            FAVORITE_ID -> favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (FAVORITE) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(DatabaseContract.FavoriteColumns.CONTENT_URI, null)

        return Uri.parse("${DatabaseContract.FavoriteColumns.CONTENT_URI}/$added")
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.update(
                uri.lastPathSegment.toString(),
                contentValues
            )
            else -> 0
        }

        context?.contentResolver?.notifyChange(DatabaseContract.FavoriteColumns.CONTENT_URI, null)

        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(DatabaseContract.FavoriteColumns.CONTENT_URI, null)

        return deleted
    }

    override fun getType(uri: Uri): String? = null

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: FavoriteHelper

        init {
            sUriMatcher.addURI(
                DatabaseContract.AUTHORITY,
                DatabaseContract.FavoriteColumns.TABLE_NAME,
                FAVORITE
            )
            sUriMatcher.addURI(
                DatabaseContract.AUTHORITY,
                "${DatabaseContract.FavoriteColumns.TABLE_NAME}/#",
                FAVORITE_ID
            )
        }
    }
}