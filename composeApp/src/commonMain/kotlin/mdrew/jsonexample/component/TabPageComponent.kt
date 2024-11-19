package mdrew.jsonexample.component

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface TabPageComponent :
    ActionComponent<TabPageComponent.Action, TabPageComponent.State, TabPageComponent.Error> {
    sealed interface Action : ActionComponent.Action {
        data object ListClicked : Action
        data object AboutClicked : Action
    }

    sealed interface Error : Component.ComponentError

    data class State(override val loading: Boolean = false, override val error: Error? = null) :
        Component.UIState<Error>

    sealed class Child {
        class ListChild(val component: ListComponent) : Child()
        class AboutChild(val component: AboutComponent) : Child()
    }
}

class TabPageComponentImpl(
    private val listComponent: ListComponent,
    private val aboutComponent: AboutComponent,
    componentContext: ComponentContext) : TabPageComponent, ComponentContext by componentContext {
    override fun onAction(action: TabPageComponent.Action) = when(action){
        TabPageComponent.Action.AboutClicked -> {

        }
        TabPageComponent.Action.ListClicked -> {

        }
    }

    override fun onStart() {
    }

    private val _state = MutableStateFlow(TabPageComponent.State())
    override val state = _state.asStateFlow()
}