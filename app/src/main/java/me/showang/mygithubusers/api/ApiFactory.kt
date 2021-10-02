package me.showang.mygithubusers.api

import me.showang.mygithubusers.api.user.UserDetailApi
import me.showang.mygithubusers.api.user.UsersApi

class ApiFactory {

    fun createUsersApi(sinceId: Long?): UsersApi {
        return UsersApi(sinceId)
    }

    fun createUserDetailApi(userName: String): UserDetailApi {
        return UserDetailApi(userName)
    }
}