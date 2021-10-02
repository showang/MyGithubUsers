package me.showang.mygithubusers.ui.userlist

import androidx.lifecycle.MutableLiveData
import github.showang.transtate.core.Transform
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import me.showang.mygithubusers.ui.AsyncJunit
import org.junit.Test

class ViewStateTest {

    private val mockLiveData: MutableLiveData<Transform> = mockk {
        every { value = any() } returns Unit
    }
    private val asyncDelegate = AsyncJunit()

    @Test
    fun testInitializingState_loadSuccess() {
        val state = UserListViewModel.State.Initializing()
        val event = UserListViewModel.Event.LoadDataSuccess(
            (0..10).map { mockk() }, false
        )
        val newState = runBlocking {
            state.startTransform(event, mockLiveData, asyncDelegate)
        } as UserListViewModel.State.DataLoaded
        assert(!newState.hasNextPage)
        assert(newState.userInfoList.size == 11)
    }

    @Test
    fun testInitializingState_loadFail() {
        val state = UserListViewModel.State.Initializing()
        val error = IllegalStateException("Test")
        val event = UserListViewModel.Event.LoadDataFailed(error)
        val newState = runBlocking {
            state.startTransform(event, mockLiveData, asyncDelegate)
        } as UserListViewModel.State.ErrorState
        assert(newState.error == error)
    }

    @Test
    fun testInitializingState_illegal() {
        val state = UserListViewModel.State.Initializing()
        val event = UserListViewModel.Event.StartReload()
        runBlocking {
            try {
                state.startTransform(event, mockLiveData, asyncDelegate)
                assert(false) { "Should exception" }
            } catch (e: Throwable) {
            }
        }
    }

    @Test
    fun testDataLoadedState_loadSuccess() {
        val state = UserListViewModel.State.DataLoaded((0..10).map { mockk() }, true)
        val event = UserListViewModel.Event.LoadDataSuccess(
            (0..10).map { mockk() }, false
        )
        val newState = runBlocking {
            state.startTransform(event, mockLiveData, asyncDelegate)
        } as UserListViewModel.State.DataLoaded
        assert(!newState.hasNextPage)
        assert(newState.userInfoList.size == 22)
    }

    @Test
    fun testDataLoadedState_loadFail() {
        val state = UserListViewModel.State.DataLoaded((0..10).map { mockk() }, true)
        val error = IllegalStateException("Test")
        val event = UserListViewModel.Event.LoadDataFailed(error)
        val newState = runBlocking {
            state.startTransform(event, mockLiveData, asyncDelegate)
        } as UserListViewModel.State.DataLoaded
        assert(newState.hasNextPage)
        assert(newState.userInfoList.size == 11)
    }

    @Test
    fun testDataLoadedState_illegal() {
        val state = UserListViewModel.State.DataLoaded((0..10).map { mockk() }, true)
        val event = UserListViewModel.Event.StartReload()
        runBlocking {
            try {
                state.startTransform(event, mockLiveData, asyncDelegate)
                assert(false) { "Should exception" }
            } catch (e: Throwable) {
            }
        }
    }

    @Test
    fun testErrorState_startReload() {
        val error = IllegalStateException("Test")
        val state = UserListViewModel.State.ErrorState(error)
        val event = UserListViewModel.Event.StartReload()
        runBlocking {
            state.startTransform(event, mockLiveData, asyncDelegate)
        } as UserListViewModel.State.Initializing
    }

    @Test
    fun testErrorState_illegal() {
        val error = IllegalStateException("Test")
        val state = UserListViewModel.State.ErrorState(error)
        val event = successEvent()
        runBlocking {
            try {
                state.startTransform(event, mockLiveData, asyncDelegate)
                assert(false) { "Should exception" }
            } catch (e: Throwable) {
            }
        }
    }

    private fun successEvent(size: Int = 10, hasNextPage: Boolean = true) =
        UserListViewModel.Event.LoadDataSuccess(
            (0 until size).map { mockk() }, hasNextPage
        )

}