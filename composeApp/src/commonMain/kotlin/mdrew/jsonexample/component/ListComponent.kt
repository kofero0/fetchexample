package mdrew.jsonexample.component

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mdrew.jsonexample.JsonExampleError
import mdrew.jsonexample.model.APIResult
import mdrew.jsonexample.model.ExampleJsonObject
import mdrew.jsonexample.repository.ExampleJsonObjectRepository
import kotlin.coroutines.CoroutineContext

interface ListComponent :
    ActionComponent<ListComponent.Action, ListComponent.State, ListComponent.Error> {
    sealed interface Action : ActionComponent.Action {
        data object Populate : Action
    }

    sealed interface Error : Component.ComponentError {
        data class HttpError(val error: JsonExampleError?, val statusCode: Int?) : Error
    }

    data class State(
        override val loading: Boolean = false,
        override val error: Error? = null,
        val exampleJsonObjects: List<ExampleJsonObject> = listOf()
    ) : Component.UIState<Error>
}

class ListComponentImpl(
    componentContext: ComponentContext,
    private val exampleJsonObjectRepository: ExampleJsonObjectRepository,
    private val coroutineContext: CoroutineContext
) : ListComponent, ComponentContext by componentContext {
    override fun onAction(action: ListComponent.Action) {
        when (action) {
            ListComponent.Action.Populate -> onPopulate()
        }
    }

    private fun onPopulate() = CoroutineScope(coroutineContext).launch {
        _state.update { state.value.copy(loading = true) }
        exampleJsonObjectRepository.get().collect { result ->
            when (result) {
                is APIResult.Failure -> {
                    _state.update {
                        state.value.copy(
                            loading = false, error = ListComponent.Error.HttpError(
                                result.cause, result.errorCode
                            )
                        )
                    }
                }

                is APIResult.Success -> {
                    _state.update {
                        state.value.copy(
                            loading = false, exampleJsonObjects = result.data
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
    }

    private val _state = MutableStateFlow(ListComponent.State())
    override val state = _state.asStateFlow()

}