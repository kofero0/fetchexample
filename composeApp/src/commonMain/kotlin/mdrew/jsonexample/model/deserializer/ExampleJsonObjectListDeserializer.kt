package mdrew.jsonexample.model.deserializer

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import mdrew.jsonexample.model.ExampleJsonObject

object ExampleJsonObjectListDeserializer : Deserializer<List<ExampleJsonObject>> {
    override fun deserialize(input: String?): Deserializer.Result<List<ExampleJsonObject>> {
        return if (input == null) {
            Deserializer.Result.Failure(Deserializer.Result.Failure.Reason.MALFORMED)
        } else {
            val rootElement: JsonElement
            try {
                rootElement = Json.parseToJsonElement(input)
            } catch (e: Exception) {
                return Deserializer.Result.Failure(
                    Deserializer.Result.Failure.Reason.MALFORMED, exception = e
                )
            }
            val dataList = ArrayList<ExampleJsonObject>().apply {
                rootElement.jsonArray.forEach { jsonElement ->
                    val obj = jsonElement.jsonObject
                    val id = obj["id"]?.jsonPrimitive?.intOrNull
                    val listId = obj["listId"]?.jsonPrimitive?.intOrNull
                    val name = obj["name"]?.jsonPrimitive?.content
                    if (id == null || listId == null) {
                        return@deserialize Deserializer.Result.Failure(Deserializer.Result.Failure.Reason.MALFORMED)
                    }
                    add(
                        ExampleJsonObject(id = id, listId = listId, name = name)
                    )
                }
            }
            Deserializer.Result.Success(data = dataList)
        }
    }
}