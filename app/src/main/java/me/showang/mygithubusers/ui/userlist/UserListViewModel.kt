package me.showang.mygithubusers.ui.userlist

import androidx.lifecycle.MutableLiveData
import github.showang.transtate.TranstateViewModel
import github.showang.transtate.async.AsyncDelegate
import github.showang.transtate.core.Transform
import github.showang.transtate.core.ViewEvent
import github.showang.transtate.core.ViewState
import me.showang.mygithubusers.model.UserInfo
import me.showang.mygithubusers.repo.UserInfoRepository

class UserListViewModel(
    private val repository: UserInfoRepository,
    private val asyncDelegate: AsyncDelegate
) : TranstateViewModel<UserListViewModel.State>(asyncDelegate) {

    override val initState: State = if (repository.userInfoCache.isEmpty()) {
        State.Initializing()
    } else {
        State.DataLoaded(repository.userInfoCache, true)
    }
    override var lastState: State = initState

    init {
        if (lastState is State.Initializing) {
            loadNextPage()
        }
    }

    fun loadNextPage() {
        asyncDelegate.background {
            try {
                val page = repository.loadNextPage()
                startTransform(Event.LoadDataSuccess(page.userInfoList, page.hasNextPage))
            } catch (e: Throwable) {
                startTransform(Event.LoadDataFailed(e))
            }
        }
    }

    fun retry() {
        asyncDelegate.background {
            startTransform(Event.StartReload())
            loadNextPage()
        }
    }

    sealed class State : ViewState() {
        class Initializing : State() {
            override fun transform(
                byEvent: ViewEvent,
                liveData: MutableLiveData<Transform>
            ): ViewState {
                return when (byEvent) {
                    is Event.LoadDataSuccess -> DataLoaded(
                        byEvent.newUserInfoList,
                        byEvent.hasNextPage
                    )
                    is Event.LoadDataFailed -> ErrorState(byEvent.error)
                    else -> throw IllegalStateException("Unknown transform by receive event: $byEvent")
                }
            }
        }

        class DataLoaded(
            val userInfoList: List<UserInfo>,
            val hasNextPage: Boolean
        ) : State() {
            override fun transform(
                byEvent: ViewEvent,
                liveData: MutableLiveData<Transform>
            ): ViewState {
                return when (byEvent) {
                    is Event.LoadDataSuccess -> DataLoaded(
                        ArrayList<UserInfo>().apply {
                            addAll(userInfoList)
                            addAll(byEvent.newUserInfoList)
                        },
                        byEvent.hasNextPage
                    )
                    is Event.LoadDataFailed -> DataLoaded(userInfoList, hasNextPage)
                    else -> throw IllegalStateException("Unknown transform by receive event: $byEvent")
                }
            }
        }

        class ErrorState(val error: Throwable) : State() {
            override fun transform(
                byEvent: ViewEvent,
                liveData: MutableLiveData<Transform>
            ): ViewState {
                return when (byEvent) {
                    is Event.StartReload -> Initializing()
                    else -> throw IllegalStateException("Unknown transform by receive event: $byEvent")
                }
            }

        }
    }

    sealed class Event : ViewEvent() {
        class StartReload : Event()
        class LoadDataSuccess(val newUserInfoList: List<UserInfo>, val hasNextPage: Boolean) :
            Event()

        class LoadDataFailed(val error: Throwable) : Event()
    }


}