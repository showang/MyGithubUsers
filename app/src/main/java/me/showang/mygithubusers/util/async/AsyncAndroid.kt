package me.showang.mygithubusers.util.async

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AsyncAndroid : AsyncDelegate {
    override fun background(func: suspend () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            func()
        }
    }

    override suspend fun updateUi(func: () -> Unit) = withContext(Dispatchers.Main) { func() }
}