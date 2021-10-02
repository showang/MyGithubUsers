package me.showang.mygithubusers.api.user

import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import me.showang.mygithubusers.api.GithubApiBase
import me.showang.mygithubusers.model.UserInfo
import me.showang.respect.core.HttpMethod
import java.lang.reflect.Type

class UsersApi(sinceId: Long? = null, pageSize: Int = 20) : GithubApiBase<List<UserInfo>>() {
    override val httpMethod = HttpMethod.GET
    override val url = "$baseUrl/users"
    override val urlQueries: Map<String, String> = mutableMapOf<String, String>().apply {
        sinceId?.let { put("since", it.toString()) }
        put("per_page", pageSize.toString())
    }
    override val headers: Map<String, String> = baseHeaderMap

    override fun parse(bytes: ByteArray): List<UserInfo> {
        val type: Type = object : TypeToken<List<UserInfoServerEntity>>() {}.type
        return gson.fromJson<List<UserInfoServerEntity>>(String(bytes), type).map {
            it.toModel()
        }
    }

    data class Result(
        val userInfoList: List<UserInfo>,
        val hasNextPage: Boolean
    )

    private data class UserInfoServerEntity(
        @SerializedName("id") val id: Long,
        @SerializedName("login") val login: String,
        @SerializedName("avatar_url") val avatarUrl: String,
        @SerializedName("site_admin") val siteAdmin: Boolean
    ) {
        fun toModel() = UserInfo(
            id, login, avatarUrl, siteAdmin
        )
    }
}