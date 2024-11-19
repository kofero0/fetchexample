package mdrew.jsonexample.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

interface TabPageComponent :
    ActionComponent<TabPageComponent.Action, TabPageComponent.State, TabPageComponent.Error> {
    val stack: Value<ChildStack<*, Child>>

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

    fun interface Builder {
        fun build(componentContext: ComponentContext): TabPageComponent
    }
}

class TabPageComponentImpl(
    private val listComponentBuilder: ListComponent.Builder,
    private val aboutComponentBuilder: AboutComponent.Builder,
    componentContext: ComponentContext
) : TabPageComponent, ComponentContext by componentContext {
    private val nav = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, TabPageComponent.Child>> = childStack(
        source = nav,
        serializer = Config.serializer(),
        initialConfiguration = Config.List,
        childFactory = ::child
    )

    private fun child(config: Config, componentContext: ComponentContext): TabPageComponent.Child = when(config){
        Config.About -> TabPageComponent.Child.AboutChild(aboutComponentBuilder.build(componentContext))
        Config.List -> TabPageComponent.Child.ListChild(listComponentBuilder.build(componentContext))
    }

    override fun onAction(action: TabPageComponent.Action) = when (action) {
        TabPageComponent.Action.AboutClicked -> aboutClicked()
        TabPageComponent.Action.ListClicked -> listClicked()
    }

    private fun aboutClicked() = nav.bringToFront(Config.About)
    private fun listClicked() = nav.bringToFront(Config.List)

    override fun onStart() {
    }

    private val _state = MutableStateFlow(TabPageComponent.State())
    override val state = _state.asStateFlow()

    @Serializable
    private sealed interface Config {
        data object List:Config
        data object About:Config
    }
}