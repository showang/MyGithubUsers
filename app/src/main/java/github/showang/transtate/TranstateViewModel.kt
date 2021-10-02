package github.showang.transtate

import androidx.lifecycle.*
import github.showang.transtate.core.Transform
import github.showang.transtate.core.ViewEvent
import github.showang.transtate.core.ViewState
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class TranstateViewModel<STATE : ViewState> : ViewModel() {

    protected abstract val initState: STATE
    protected abstract var lastState: STATE
    private val mTransformLiveData: MutableLiveData<Transform> by lazy {
        MutableLiveData(Transform(ViewState.empty(), initState, ViewEvent.empty()))
    }

    private val currentState get() = lastState
    private val transformLiveData: LiveData<Transform> get() = mTransformLiveData
    private val mutex by lazy { Mutex() }

    protected suspend fun startTransform(event: ViewEvent) {
        @Suppress("UNCHECKED_CAST")
        lastState = mutex.withLock {
            currentState.startTransform(event, mTransformLiveData)
        } as? STATE ?: throw IllegalStateException("Unsupported state type")
    }

    fun observeTransformation(
        lifecycleOwner: LifecycleOwner,
        initViewByState: (ViewState) -> Unit,
        updateViewByTransform: (Transform) -> Unit
    ) {
        transformLiveData.observe(lifecycleOwner, Observer { transform ->
            transform.run {
                if (shouldHandleEvent) {
                    updateViewByTransform(this)
                } else {
                    initViewByState(newState)
                }
            }
        })
    }

    fun removeObservers(lifecycleOwner: LifecycleOwner) {
        transformLiveData.removeObservers(lifecycleOwner)
    }
}