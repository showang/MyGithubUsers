package me.showang.mygithubusers.ui.userdetail

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import me.showang.mygithubusers.api.ApiFactory
import me.showang.mygithubusers.api.user.UserDetailApi
import me.showang.mygithubusers.model.UserDetail
import me.showang.mygithubusers.model.UserInfo
import me.showang.mygithubusers.util.async.AsyncDelegate
import me.showang.respect.suspend
import org.junit.Before
import org.junit.Test

class UserDetailPresenterTest {

    @MockK
    lateinit var apiFactory: ApiFactory

    @MockK
    lateinit var mockApi: UserDetailApi

    @MockK(relaxed = true)
    lateinit var mockView: UserDetailPresenter.ViewDelegate

    lateinit var presenter: UserDetailPresenter

    private val asyncDelegate = AsyncJunit()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { apiFactory.createUserDetailApi(any()) } returns mockApi
        presenter = UserDetailPresenter(apiFactory, asyncDelegate)
    }

    private val testUserInfo = UserInfo(
        123,
        "tui",
        "aurl",
        false
    )
    private val testUserDetail = UserDetail(
        99,
        "Test",
        "TestName",
        "TestBio",
        "AvatarUrl",
        true,
        "mail",
        "location",
        "blog"
    )

    @Test
    fun testLoadData_success() {
        coEvery { mockApi.suspend() } returns testUserDetail
        presenter.viewDelegate = mockView
        presenter.initialDataBy(testUserInfo)

        coVerify(exactly = 1) { mockApi.suspend() }
        verify(exactly = 1) { mockView.onLoadDetailSuccess(testUserDetail) }
        verify(exactly = 0) { mockView.onLoadDetailFailed(any()) }
    }

    @Test
    fun testLoadData_failed() {
        val testException = IllegalStateException("Test")
        coEvery { mockApi.suspend() } throws testException

        presenter.viewDelegate = mockView
        presenter.initialDataBy(testUserInfo)

        coVerify(exactly = 1) { mockApi.suspend() }
        verify(exactly = 1) { mockView.onLoadDetailFailed(testException) }
        verify(exactly = 0) { mockView.onLoadDetailSuccess(any()) }
    }


    class AsyncJunit : AsyncDelegate {
        override fun background(func: suspend () -> Unit) {
            runBlocking { func() }
        }

        override suspend fun updateUi(func: () -> Unit) {
            func()
        }

    }
}