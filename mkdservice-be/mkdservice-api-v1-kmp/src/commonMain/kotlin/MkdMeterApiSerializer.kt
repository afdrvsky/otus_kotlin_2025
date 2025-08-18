@file:Suppress("unused")

package com.fedorovsky.mkdservice.api.v1

import com.fedorovsky.mkdservice.api.v1.models.IRequest
import com.fedorovsky.mkdservice.api.v1.models.IResponse
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json


@OptIn(ExperimentalSerializationApi::class)
@Suppress("JSON_FORMAT_REDUNDANT_DEFAULT")
val apiV1Mapper = Json {
//    ignoreUnknownKeys = true
    allowTrailingComma = true
}

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV1RequestDeserialize(json: String) =
    apiV1Mapper.decodeFromString<IRequest>(json) as T

fun apiV1ResponseSerialize(obj: IResponse): String =
    apiV1Mapper.encodeToString(IResponse.serializer(), obj)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiV1ResponseDeserialize(json: String) =
    apiV1Mapper.decodeFromString<IResponse>(json) as T

inline fun <reified T : IResponse> apiV1ResponseSimpleDeserialize(json: String) =
    apiV1Mapper.decodeFromString<T>(json)

@Suppress("unused")
fun apiV1RequestSerialize(obj: IRequest): String =
    apiV1Mapper.encodeToString(IRequest.serializer(), obj)

@Suppress("unused")
inline fun <reified T : IRequest> apiV1RequestSimpleSerialize(obj: T): String =
    apiV1Mapper.encodeToString<T>(obj)