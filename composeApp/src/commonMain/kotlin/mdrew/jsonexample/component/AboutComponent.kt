package mdrew.jsonexample.component

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface AboutComponent : Component<AboutComponent.State,AboutComponent.Error> {
    interface Error : Component.ComponentError

    data class State(
        override val loading: Boolean = false,
        override val error: Error? = null
    ) : Component.UIState<Error>
}

class AboutComponentImpl(componentContext: ComponentContext): AboutComponent, ComponentContext by componentContext {
    override fun onStart() {}

    private val _state = MutableStateFlow(AboutComponent.State())
    override val state = _state.asStateFlow()
}