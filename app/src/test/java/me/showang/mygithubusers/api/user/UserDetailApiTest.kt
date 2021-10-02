package me.showang.mygithubusers.api.user

import kotlinx.coroutines.runBlocking
import me.showang.respect.suspend
import org.junit.Test

class UserDetailApiTest {

    @Test
    fun testRequest_success() {
        val result = runBlocking {
            UserDetailApi("showang").suspend()
        }
        assert(result.account == "showang")
        assert(result.name == "Hsuanhao")
        assert(result.bio == "Philosopher")
        assert(result.location == "Taiwan")
    }

}