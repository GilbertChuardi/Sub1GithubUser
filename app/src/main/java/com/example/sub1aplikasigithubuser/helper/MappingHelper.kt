package com.example.sub1aplikasigithubuser.helper

import android.database.Cursor
import com.example.sub1aplikasigithubuser.data.Favorite
import com.example.sub1aplikasigithubuser.db.DatabaseContract
import java.util.ArrayList

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Favorite> {
        val favoriteList = ArrayList<Favorite>()

        notesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOSITORY))
                val favorite =
                        getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FAVORITE))
                favoriteList.add(
                        Favorite(
                                username,
                                name,
                                avatar,
                                company,
                                location,
                                repository,
                                favorite
                        )
                )
            }
        }
        return favoriteList
    }
}