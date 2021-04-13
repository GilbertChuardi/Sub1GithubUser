package com.example.sub1aplikasigithubuser.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class User(
    var username: String = "null",
    var name: String = "null",
    var avatar: String = "null",
    var follower: String = "null",
    var following: String = "null",
    var company: String = "null",
    var location: String = "null",
    var repository: String = "null"
) : Parcelable
