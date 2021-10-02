package me.showang.mygithubusers.api.user

import com.google.gson.annotations.SerializedName
import me.showang.mygithubusers.api.GithubApiBase
import me.showang.mygithubusers.model.UserDetail
import me.showang.respect.core.HttpMethod

class UserDetailApi(userName: String) : GithubApiBase<UserDetail>() {
    override val httpMethod = HttpMethod.GET
    override val url = "$baseUrl/users/$userName"

    override fun parse(bytes: ByteArray): UserDetail {
        return gson.fromJson(String(bytes), UserDetailServerEntity::class.java).toModel()
    }

    private data class UserDetailServerEntity(
        @SerializedName("id") val id: Long,
        @SerializedName("login") val login: String,
        @SerializedName("avatar_url") val avatarUrl: String,
        @SerializedName("name") val name: String?,
        @SerializedName("bio") val bio: String,
        @SerializedName("site_admin") val siteAdmin: Boolean,
        @SerializedName("location") val location: String?,
        @SerializedName("blog") val blog: String?,
        @SerializedName("followers") val followers: Int?,
        @SerializedName("following") val following: Int?,
        @SerializedName("email") val email: String?
    ) {
        fun toModel() = UserDetail(
            id,
            login,
            name ?: login,
            bio,
            avatarUrl,
            siteAdmin,
            email,
            location,
            blog
        )
    }
}