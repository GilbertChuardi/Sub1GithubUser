package com.example.sub1aplikasigithubuser.backend.helper

import android.database.Cursor
import com.example.sub1aplikasigithubuser.backend.db.DatabaseContract
import com.example.sub1aplikasigithubuser.backend.entity.Favorite

object MappingHelper {

    fun mapCursorToArrayList(favoritesCursor: Cursor?): ArrayList<Favorite> {
        val favoriteList = ArrayList<Favorite>()

        favoritesCursor?.apply {
            while (moveToNext()) {
                val username =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                val avatar =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                val company =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COMPANY))
                val location =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOCATION))
                val repository =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOSITORY))
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