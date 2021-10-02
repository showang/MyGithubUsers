package me.showang.mygithubusers.ui.userlist

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import me.showang.mygithubusers.model.UserInfo
import me.showang.mygithubusers.repo.UserInfoRepository
import me.showang.mygithubusers.ui.AsyncJunit
import org.junit.Before
import org.junit.Test

class UserListViewModelTest {

    @MockK
    private lateinit var repo: UserInfoRepository

    private lateinit var viewModel: UserListViewModel
    private val asyncDelegate = AsyncJunit()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testLoadNextPage_empty() {
        val testPageResult = UserInfoRepository.PageResult(
            mockUserInfo(10), false
        )
        every { repo.userInfoCache } returns emptyList()
        coEvery { repo.loadNextPage() } returns testPageResult

        viewModel = UserListViewModel(repo, asyncDelegate)
        assert(viewModel.currentState is UserListViewModel.State.DataLoaded)
        (viewModel.currentState as UserListViewModel.State.DataLoaded).run {
            assert(userInfoList.size == 10)
            assert(!hasNextPage)
        }
        coVerify(exactly = 1) { repo.loadNextPage() }
    }

    @Test
    fun testLoadNextPage_nonempty() {
        val testPageResult = UserInfoRepository.PageResult(
            mockUserInfo(20), true
        )
        every { repo.userInfoCache } returns mockUserInfo(10)
        coEvery { repo.loadNextPage() } returns testPageResult

        viewModel = UserListViewModel(repo, asyncDelegate)
        assert(viewModel.currentState is UserListViewModel.State.DataLoaded)
        (viewModel.currentState as UserListViewModel.State.DataLoaded).run {
            assert(userInfoList.size == 10)
            assert(hasNextPage)
        }
        coVerify(exactly = 0) { repo.loadNextPage() }
        viewModel.loadNextPage()
        (viewModel.currentState as UserListViewModel.State.DataLoaded).run {
            assert(userInfoList.size == 30)
            assert(hasNextPage)
        }
    }

    private fun mockUserInfo(size: Int): List<UserInfo> {
        return (0 until size).map {
            UserInfo(it.toLong(), "account$it", "avatar$it", it % 2 == 0)
        }
    }

}