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
        assert(page1.size == 20)
        print("Page 1 IDs: ")
        page1.map { print("${it.id} ") }
        println(page1)

        val page2 = runBlocking {
            UsersApi(page1.last().id).suspend()
        }
        assert(page2.size == 20)
        print("Page 2 IDs: ")
        page2.map { print("${it.id} ") }
        println(page2)
    }
}