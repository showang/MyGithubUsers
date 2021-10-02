package me.showang.mygithubusers.ui.userdetail

import me.showang.mygithubusers.api.ApiFactory
import me.showang.mygithubusers.model.UserDetail
import me.showang.mygithubusers.model.UserInfo
import me.showang.mygithubusers.util.async.AsyncDelegate
import me.showang.respect.suspend

class UserDetailPresenter(private val apiFactory: ApiFactory, private val asyncDelegate: AsyncDelegate) {
    var viewDelegate: ViewDelegate? = null

    fun initialDataBy(userInfo: UserInfo) {
        asyncDelegate.background {
            try {
                val result = apiFactory.createUserDetailApi(userInfo.account).suspend()
                asyncDelegate.updateUi {
                    viewDelegate?.onLoadDetailSuccess(result)
                }
            } catch (e: Throwable) {
                asyncDelegate.updateUi {
                    viewDelegate?.onLoadDetailFailed(e)
                }
            }
        }
    }

    interface ViewDelegate {
        fun onLoadDetailFailed(cause: Throwable)

        fun onLoadDetailSuccess(detail: UserDetail)
    }
}