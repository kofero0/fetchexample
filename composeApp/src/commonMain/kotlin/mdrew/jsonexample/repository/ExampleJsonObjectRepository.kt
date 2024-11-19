package mdrew.jsonexample.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mdrew.jsonexample.model.APIResult
import mdrew.jsonexample.model.ExampleJsonObject
import mdrew.jsonexample.repository.remote.ExampleJsonObjectRemoteSource

fun interface ExampleJsonObjectRepository {
    suspend fun get(): Flow<APIResult<List<ExampleJsonObject>>>
}

class ExampleJsonObjectRepositoryImpl(private val remoteSource: ExampleJsonObjectRemoteSource): ExampleJsonObjectRepository {
    override suspend fun get() = flow {
        emit(remoteSource.get())
    }
}