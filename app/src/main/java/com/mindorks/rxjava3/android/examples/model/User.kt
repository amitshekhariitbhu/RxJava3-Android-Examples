package com.mindorks.rxjava3.android.examples.model

data class User(
    var id: Long = 0L,
    var firstname: String,
    var lastname: String,
    var isFollowing: Boolean = false
)
