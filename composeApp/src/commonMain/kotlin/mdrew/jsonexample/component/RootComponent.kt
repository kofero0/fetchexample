package mdrew.jsonexample.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

interface RootComponent : Component<RootComponent.State,RootComponent.RootError>, BackHandlerOwner {
    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked()

    sealed interface RootError : Component.ComponentError

    data class State(
        override val loading: Boolean = false, override val error: RootError? = null
    ) : Component.UIState<RootError>

    sealed class Child {
        class TabPageChild(val component: TabPageComponent) : Child()
    }
}

class RootComponentImpl(
    componentContext: ComponentContext,
    private val tabPageComponentBuilder:TabPageComponent.Builder,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.TabPage,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.TabPage -> RootComponent.Child.TabPageChild(tabPageComponentBuilder.build(componentContext))
        }

    override fun onBackClicked() {
        navigation.pop()
    }

    override fun onStart() {

    }

    private val _state = MutableStateFlow(RootComponent.State())
    override val state = _state.asStateFlow()

    @Serializable
    private sealed interface Config {
        @Serializable
        data object TabPage : Config
    }
}