package me.showang.mygithubusers.api.user

import kotlinx.coroutines.runBlocking
import me.showang.respect.suspend
import org.junit.Test

class UsersApiTest {

    @Test
    fun testRequest_success() {
        val page1 = runBlocking {
            UsersApi().suspend()
        }
        assert(page1.infoList.size == 20)
        print("Page 1 IDs: ")
        page1.infoList.map { print("${it.id} ") }
        println(page1)
        assert(page1.hasNextPage)

        val page2 = runBlocking {
            UsersApi(page1.infoList.last().id).suspend()
        }
        assert(page2.infoList.size == 20)
        print("Page 2 IDs: ")
        page2.infoList.map { print("${it.id} ") }
        println(page2)
        assert(page2.hasNextPage)
    }
}