package me.showang.mygithubusers.model

data class UserDetail(
    val id: Long,
    val account: String,
    val name: String,
    val bio: String?,
    val avatarUrl: String,
    val isStaff: Boolean,
    val mail: String?,
    val location: String?,
    val blogUrl: String?
)