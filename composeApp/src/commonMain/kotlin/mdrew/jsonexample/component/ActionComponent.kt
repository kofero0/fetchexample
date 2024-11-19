package mdrew.jsonexample.component

interface ActionComponent<in Action: ActionComponent.Action, out State: Component.UIState<Error>, out Error: Component.ComponentError>: Component<State,Error> {
    interface Action

    fun onAction(action:Action)
}