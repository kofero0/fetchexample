package mdrew.jsonexample

import mdrew.jsonexample.model.deserializer.Deserializer
import mdrew.jsonexample.model.deserializer.ExampleJsonObjectListDeserializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeserializerTests {

    @Test
    fun testDeserializerReturnsSuccessfulWithProperlyFormedJSON() {
        val result = ExampleJsonObjectListDeserializer.deserialize(JSON)
        assertTrue(result is Deserializer.Result.Success)
        val list = result.data
        assertEquals(list.first().id, FIRST_ID)
    }

    @Test
    fun testDeserializerReturnsFailureWithMalformedJSON() {
        val result = ExampleJsonObjectListDeserializer.deserialize(JSON + "456")
        assertTrue(result is Deserializer.Result.Failure)
        assertEquals(result.reason, Deserializer.Result.Failure.Reason.MALFORMED)
    }

    @Test
    fun testDeserializerReturnsFailureWithIncorrectType() {
        val result = ExampleJsonObjectListDeserializer.deserialize(INCORRECT_TYPE_JSON)
        assertTrue(result is Deserializer.Result.Failure)
        assertEquals(result.reason, Deserializer.Result.Failure.Reason.MALFORMED)
    }

    companion object {
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
        const val INCORRECT_TYPE_JSON = """[{"id": 599, "listId": 1, "name": null},
{"id": 424, "listId": 2, "name": null},
{"id": false, "listId": 1, "name": ""}]"""
    }
}