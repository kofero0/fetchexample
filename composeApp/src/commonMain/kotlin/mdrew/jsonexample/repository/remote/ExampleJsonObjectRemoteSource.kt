package mdrew.jsonexample.repository.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import mdrew.jsonexample.model.APIResult
import mdrew.jsonexample.model.ExampleJsonObject
import mdrew.jsonexample.model.deserializer.Deserializer

fun interface ExampleJsonObjectRemoteSource {
    suspend fun get(): APIResult<List<ExampleJsonObject>>
}

class ExampleJsonObjectRemoteSourceImpl(
    private val client: HttpClient = HttpClient(),
    private val url: Url = Url("https://fetch-hiring.s3.amazonaws.com/hiring.json"),
    private val deserializer: Deserializer<List<ExampleJsonObject>>
) : ExampleJsonObjectRemoteSource {
    override suspend fun get(): APIResult<List<ExampleJsonObject>> {
        val response = client.get(url)
        return if (response.status == HttpStatusCode.OK) {
            when (val result = deserializer.deserialize(response.bodyAsText())) {
                is Deserializer.Result.Success -> APIResult.Success(data = result.data)
                is Deserializer.Result.Failure -> APIResult.Failure(cause = result.reason)
            }
        } else {
            APIResult.Failure(errorCode = response.status.value)
        }
    }
}