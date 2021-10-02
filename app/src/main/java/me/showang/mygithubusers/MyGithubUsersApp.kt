package me.showang.mygithubusers

import androidx.multidex.MultiDexApplication
import me.showang.mygithubusers.api.ApiFactory
import me.showang.mygithubusers.repo.UserInfoRepository
import me.showang.mygithubusers.ui.userdetail.UserDetailPresenter
import me.showang.mygithubusers.ui.userlist.UserListViewModel
import github.showang.transtate.async.AsyncAndroid
import github.showang.transtate.async.AsyncDelegate
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyGithubUsersApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                module {
                    single { ApiFactory() }
                    single { UserInfoRepository(get()) }

                    viewModel { UserListViewModel(get(), get()) }
                },
                module {
                    single<AsyncDelegate> { AsyncAndroid() }
                    single { UserDetailPresenter(get(), get()) }
                }
            )
        }
    }
}