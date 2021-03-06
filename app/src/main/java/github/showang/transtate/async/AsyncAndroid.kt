package github.showang.transtate.async

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AsyncAndroid : AsyncDelegate {
    override fun background(func: suspend () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            func()
        }
    }

    override suspend fun updateUi(func: () -> Unit) = withContext(Dispatchers.Main) { func() }

    override suspend fun <Type> updateLiveDataValue(
        liveData: MutableLiveData<Type>,
        input: Type,
        post: () -> Unit
    ) {
        withContext(Main) {
            liveData.value = input
            post()
        }
    }
}