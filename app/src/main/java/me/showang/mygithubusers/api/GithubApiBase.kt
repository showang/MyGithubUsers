package me.showang.mygithubusers.api

import com.google.gson.Gson
import me.showang.respect.RespectApi

abstract class GithubApiBase<Model> : RespectApi<Model>() {
    protected val gson get() = sGson

    protected val baseUrl: String = "https://api.github.com"
    protected val baseHeaderMap: MutableMap<String, String> = mutableMapOf(
        "Accept" to "application/vnd.github.v3+json"
    )

    companion object {
        private val sGson = Gson()
    }
}