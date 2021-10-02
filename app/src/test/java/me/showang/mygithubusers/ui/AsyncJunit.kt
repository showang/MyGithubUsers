package me.showang.mygithubusers.ui

import androidx.lifecycle.MutableLiveData
import github.showang.transtate.async.AsyncDelegate
import kotlinx.coroutines.runBlocking

class AsyncJunit : AsyncDelegate {
    override fun background(func: suspend () -> Unit) {
        runBlocking { func() }
    }

    override suspend fun updateUi(func: () -> Unit) {
        func()
    }

    override suspend fun <Type> updateLiveDataValue(
        liveData: MutableLiveData<Type>,
        input: Type,
        post: () -> Unit
    ) {
    }


}