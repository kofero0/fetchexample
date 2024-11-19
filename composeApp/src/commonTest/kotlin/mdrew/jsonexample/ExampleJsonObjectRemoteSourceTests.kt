package mdrew.jsonexample

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import mdrew.jsonexample.model.APIResult
import mdrew.jsonexample.model.deserializer.deserializeToExampleJsonObjectList
import mdrew.jsonexample.repository.remote.ExampleJsonObjectRemoteSource
import mdrew.jsonexample.repository.remote.ExampleJsonObjectRemoteSourceImpl
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExampleJsonObjectRemoteSourceTests {
    private var unit: ExampleJsonObjectRemoteSource? = null

    @BeforeTest
    fun before() {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(JSON),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        unit = ExampleJsonObjectRemoteSourceImpl(
            HttpClient(mockEngine), url = Url("testUrl")
        ) { deserializeToExampleJsonObjectList(it) }
    }

    @Test
    fun testRemoteSourceWithCorrectResponseReturnsSuccess() = runTest {
        val result = unit?.get()
        assertTrue(result is APIResult.Success)
        assertEquals(result.data.first().id, FIRST_ID)
    }

    private companion object {
        const val FIRST_ID = 755
        const val JSON = """[{"id": $FIRST_ID, "listId": 2, "name": ""},
{"id": 203, "listId": 2, "name": ""},
{"id": 684, "listId": 1, "name": "Item 684"},
{"id": 276, "listId": 1, "name": "Item 276"},
{"id": 736, "listId": 3, "name": null},
{"id": 926, "listId": 4, "name": null},
{"id": 808, "listId": 4, "name": "Item 808"},
{"id": 599, "listId": 1, "name": null},
{"id": 424, "listId": 2, "name": null},
{"id": 444, "listId": 1, "name": ""},
{"id": 809, "listId": 3, "name": null},
{"id": 293, "listId": 2, "name": null},
{"id": 510, "listId": 2, "name": null},
{"id": 680, "listId": 3, "name": "Item 680"},
{"id": 231, "listId": 2, "name": null},
{"id": 534, "listId": 4, "name": "Item 534"},
{"id": 294, "listId": 4, "name": ""},
{"id": 439, "listId": 1, "name": null},
{"id": 156, "listId": 2, "name": null},
{"id": 906, "listId": 2, "name": "Item 906"},
{"id": 49, "listId": 2, "name": null},
{"id": 48, "listId": 2, "name": null},
{"id": 735, "listId": 1, "name": "Item 735"},
{"id": 52, "listId": 2, "name": ""},
{"id": 681, "listId": 4, "name": "Item 681"},
{"id": 137, "listId": 3, "name": "Item 137"}]"""
    }
}