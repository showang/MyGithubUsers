package me.showang.mygithubusers.util

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class NavigationHelper {

    private val navigationMutex = Mutex()
    private var isNavigated: Boolean = false

    fun onResume() {
        isNavigated = false
    }

    fun navigate( navigationFun: () -> Unit) {
        val haveNavigated = runBlocking {
            navigationMutex.withLock {
                if (isNavigated) true
                else {
                    isNavigated = true
                    false
                }
            }
        }
        if (!haveNavigated) {
            navigationFun.invoke()
        }
    }

}