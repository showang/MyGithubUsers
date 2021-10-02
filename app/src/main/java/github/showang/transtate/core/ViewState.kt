package github.showang.transtate.core

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext

abstract class ViewState(val timestamp: Long = System.currentTimeMillis()) {

    suspend fun startTransform(
        event: ViewEvent,
        liveData: MutableLiveData<Transform>,
        dispatcher: CoroutineDispatcher = Main
    ): ViewState {
        val preState = this
        return transform(event, liveData).also { newState ->
            withContext(dispatcher) {
                liveData.value = Transform(
                    preState,
                    newState,
                    event
                )
                event.handled()
            }
        }
    }

    protected abstract fun transform(
        byEvent: ViewEvent,
        liveData: MutableLiveData<Transform>
    ): ViewState

    companion object {
        fun empty(): ViewState = EmptyState()
    }
}

class EmptyState : ViewState() {
    override fun transform(byEvent: ViewEvent, liveData: MutableLiveData<Transform>): ViewState {
        return this
    }
}