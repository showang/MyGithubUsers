package me.showang.mygithubusers.repo

import me.showang.mygithubusers.api.ApiFactory
import me.showang.mygithubusers.model.UserInfo
import me.showang.respect.suspend

class GithubRepository(private val apiFactory: ApiFactory) {

    private val mUserInfoCache: MutableList<UserInfo> = mutableListOf()
    val userInfoCache: List<UserInfo> get() = mUserInfoCache

    suspend fun loadNextPage(): PageResult {
        val lastId = if (mUserInfoCache.isNotEmpty()) {
            mUserInfoCache.last().id
        } else null
        val apiResult = apiFactory.createUsersApi(lastId).suspend()
            .also { mUserInfoCache.addAll(it.infoList) }
        return PageResult(apiResult.infoList, apiResult.hasNextPage)
    }

    data class PageResult(
        val userInfoList: List<UserInfo>,
        val hasNextPage: Boolean
    )
}