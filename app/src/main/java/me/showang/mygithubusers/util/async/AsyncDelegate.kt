package me.showang.mygithubusers.util.async

interface AsyncDelegate {

    fun background(func: suspend () -> Unit)

    suspend fun updateUi(func: () -> Unit)

}

