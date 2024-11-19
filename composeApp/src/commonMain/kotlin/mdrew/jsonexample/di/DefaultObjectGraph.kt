package mdrew.jsonexample.di

import com.arkivanov.decompose.ComponentContext
import io.ktor.client.HttpClient
import io.ktor.http.Url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import mdrew.jsonexample.component.AboutComponent
import mdrew.jsonexample.component.AboutComponentImpl
import mdrew.jsonexample.component.ListComponent
import mdrew.jsonexample.component.ListComponentImpl
import mdrew.jsonexample.component.RootComponentImpl
import mdrew.jsonexample.component.TabPageComponent
import mdrew.jsonexample.component.TabPageComponentImpl
import mdrew.jsonexample.model.deserializer.deserializeToExampleJsonObjectList
import mdrew.jsonexample.repository.ExampleJsonObjectRepository
import mdrew.jsonexample.repository.ExampleJsonObjectRepositoryImpl
import mdrew.jsonexample.repository.remote.ExampleJsonObjectRemoteSource
import mdrew.jsonexample.repository.remote.ExampleJsonObjectRemoteSourceImpl
import kotlin.coroutines.CoroutineContext

object DefaultObjectGraph {
    fun rootComponent(componentContext: ComponentContext) = RootComponentImpl(componentContext = componentContext,
        tabPageComponentBuilder = tabPageComponentBuilder(
            listComponentBuilder = listComponentBuilder(
                exampleJsonObjectRepository = exampleJsonObjectRepository,
                coroutineContext = Dispatchers.IO
            ),
            aboutComponentBuilder = aboutComponentBuilder()
        )
    )


    private fun tabPageComponentBuilder(listComponentBuilder: ListComponent.Builder, aboutComponentBuilder: AboutComponent.Builder) =
        TabPageComponent.Builder { componentContext ->
            TabPageComponentImpl(
                listComponentBuilder = listComponentBuilder,
                aboutComponentBuilder = aboutComponentBuilder,
                componentContext = componentContext
            )
        }

    private fun listComponentBuilder(exampleJsonObjectRepository: ExampleJsonObjectRepository, coroutineContext: CoroutineContext): ListComponent.Builder =
        ListComponent.Builder { componentContext ->
            ListComponentImpl(
                exampleJsonObjectRepository = exampleJsonObjectRepository,
                coroutineContext = coroutineContext,
                componentContext = componentContext
            )
        }

    private fun aboutComponentBuilder(): AboutComponent.Builder =
        AboutComponent.Builder { componentContext -> AboutComponentImpl(componentContext = componentContext) }

    private val httpClient = HttpClient()

    private val exampleJsonObjectUrl = Url("https://fetch-hiring.s3.amazonaws.com/hiring.json")

    private val exampleJsonObjectRemoteSource: ExampleJsonObjectRemoteSource = ExampleJsonObjectRemoteSourceImpl(client = httpClient,url = exampleJsonObjectUrl){ deserializeToExampleJsonObjectList(it) }

    private val exampleJsonObjectRepository: ExampleJsonObjectRepository = ExampleJsonObjectRepositoryImpl(remoteSource = exampleJsonObjectRemoteSource)
}