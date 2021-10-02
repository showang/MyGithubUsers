package me.showang.mygithubusers

import androidx.multidex.MultiDexApplication
import me.showang.mygithubusers.api.ApiFactory
import me.showang.mygithubusers.repo.GithubRepository
import me.showang.mygithubusers.ui.userlist.UserListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyGithubUsersApp: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                module {
                    single { ApiFactory() }
                    single { GithubRepository(get()) }
                    viewModel { UserListViewModel(get()) }
                }
            )
        }
    }
}