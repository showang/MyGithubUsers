package me.showang.mygithubusers.model

import java.io.Serializable

data class UserInfo(
    val id: Long,
    val account: String,
    val avatarUrl: String,
    val isStaff: Boolean
) : Serializable