package mdrew.jsonexample.component

import kotlinx.coroutines.flow.StateFlow
import mdrew.jsonexample.JsonExampleError

interface Component<out State: Component.UIState<Error>, out Error: Component.ComponentError> {
    fun onStart()
    val state: StateFlow<State>

    interface ComponentError: JsonExampleError

    interface UIState<out Error: ComponentError>{
        val loading: Boolean
        val error: Error?
    }
}