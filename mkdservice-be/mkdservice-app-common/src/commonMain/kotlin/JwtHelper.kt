package com.fedorovsky.mkdservice.app.common

import com.fedorovsky.mkdservice.common.models.MkdUserId
import com.fedorovsky.mkdservice.common.permissions.MkdPrincipalModel
import com.fedorovsky.mkdservice.common.permissions.MkdUserGroups
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val AUTH_HEADER: String = "x-jwt-payload"

@OptIn(ExperimentalEncodingApi::class)
fun String?.jwt2principal(): MkdPrincipalModel = this?.let { jwtHeader ->
    val jwtJson = base64UrlDecode(jwtHeader).decodeToString()
    println("JWT JSON PAYLOAD: $jwtJson")
    val jwtObj = jsMapper.decodeFromString(JwtPayload.serializer(), jwtJson)
    jwtObj.toPrincipal()
}
    ?: run {
        println("No jwt found in headers")
        MkdPrincipalModel.NONE
    }

@OptIn(ExperimentalEncodingApi::class)
fun MkdPrincipalModel.createJwtTestHeader(): String {
    val jwtObj = fromPrincipal()
    val jwtJson = jsMapper.encodeToString(JwtPayload.serializer(), jwtObj)
    return Base64.encode(jwtJson.encodeToByteArray())
}

private val jsMapper = Json {
    ignoreUnknownKeys = true
}

@Serializable
private data class JwtPayload(
    val aud: List<String>? = null,
    val sub: String? = null,
    @SerialName("family_name")
    val familyName: String? = null,
    @SerialName("given_name")
    val givenName: String? = null,
    @SerialName("middle_name")
    val middleName: String? = null,
    val groups: List<String>? = null,
    @SerialName("own_flats")
    val ownFlats: List<String>? = null,
)

private fun JwtPayload.toPrincipal(): MkdPrincipalModel = MkdPrincipalModel(
    id = sub?.let { MkdUserId(it) } ?: MkdUserId.NONE,
    fname = givenName ?: "",
    mname = middleName ?: "",
    lname = familyName ?: "",
    groups = groups?.mapNotNull { it.toPrincipalGroup() }?.toSet() ?: emptySet(),
    ownFlats = ownFlats ?: emptyList(),
)

private fun MkdPrincipalModel.fromPrincipal(): JwtPayload = JwtPayload(
    sub = id.takeIf { it != MkdUserId.NONE }?.asString(),
    givenName = fname.takeIf { it.isNotBlank() },
    middleName = mname.takeIf { it.isNotBlank() },
    familyName = lname.takeIf { it.isNotBlank() },
    groups = groups.mapNotNull { it.fromPrincipalGroup() }.toList().takeIf { it.isNotEmpty() } ?: emptyList(),
    ownFlats = ownFlats.takeIf { it.isNotEmpty() }
)

private fun String?.toPrincipalGroup(): MkdUserGroups? = when (this?.uppercase()) {
    "USER" -> MkdUserGroups.USER
    "ADMIN_METER" -> MkdUserGroups.ADMIN_METER
    "MODERATOR_MKD" -> MkdUserGroups.MODERATOR_MKD
    "TEST" -> MkdUserGroups.TEST
    "BAN_METER" -> MkdUserGroups.BAN_METER
    // TODO сделать обработку ошибок
    else -> null
}

private fun MkdUserGroups?.fromPrincipalGroup(): String? = when (this) {
    MkdUserGroups.USER -> "USER"
    MkdUserGroups.ADMIN_METER -> "ADMIN_METER"
    MkdUserGroups.MODERATOR_MKD -> "MODERATOR_MKD"
    MkdUserGroups.TEST -> "TEST"
    MkdUserGroups.BAN_METER -> "BAN_METER"
    // TODO сделать обработку ошибок
    else -> null
}

@OptIn(ExperimentalEncodingApi::class)
private fun base64UrlDecode(input: String): ByteArray {
    val paddedInput = when (input.length % 4) {
        0 -> input
        2 -> "$input=="
        3 -> "$input="
        else -> throw IllegalArgumentException("Некорректная Base64URL строка: $input")
    }
    return Base64.decode(paddedInput)
}
