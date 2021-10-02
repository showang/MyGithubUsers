package github.showang.transtate.async

import androidx.lifecycle.MutableLiveData

interface AsyncDelegate {

    fun background(func: suspend () -> Unit)

    suspend fun updateUi(func: () -> Unit)

    suspend fun <Type> updateLiveDataValue(
        liveData: MutableLiveData<Type>,
        input: Type,
        post: () -> Unit = {}
    )

}

