package com.fedorovsky.mkdservice.auth

import com.fedorovsky.mkdservice.common.models.MkdMeterPermissionClient
import com.fedorovsky.mkdservice.common.permissions.MkdPrincipalRelations
import com.fedorovsky.mkdservice.common.permissions.MkdUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<MkdUserPermissions>,
    relations: Iterable<MkdPrincipalRelations>,
) = mutableSetOf<MkdMeterPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

/**
 * Это трехмерная таблица пермишин в бэкенде->отношение к объявлению->пермишин на фронте
 */
private val accessTable = mapOf(
    //CREATE
    MkdUserPermissions.CREATE_OWN to mapOf(
        MkdPrincipalRelations.OWN to MkdMeterPermissionClient.CREATE
    ),
    MkdUserPermissions.CREATE_ALL to mapOf(
        MkdPrincipalRelations.MODERATABLE to MkdMeterPermissionClient.CREATE
    ),

    // READ
    MkdUserPermissions.READ_OWN to mapOf(
        MkdPrincipalRelations.OWN to MkdMeterPermissionClient.READ
    ),
    MkdUserPermissions.READ_ALL to mapOf(
        MkdPrincipalRelations.MODERATABLE to MkdMeterPermissionClient.READ
    ),

    // UPDATE
    MkdUserPermissions.UPDATE_OWN to mapOf(
        MkdPrincipalRelations.OWN to MkdMeterPermissionClient.UPDATE
    ),
    MkdUserPermissions.UPDATE_ALL to mapOf(
        MkdPrincipalRelations.MODERATABLE to MkdMeterPermissionClient.UPDATE
    ),

    // DELETE
    MkdUserPermissions.DELETE_OWN to mapOf(
        MkdPrincipalRelations.OWN to MkdMeterPermissionClient.DELETE
    ),
    MkdUserPermissions.DELETE_ALL to mapOf(
        MkdPrincipalRelations.MODERATABLE to MkdMeterPermissionClient.DELETE
    ),
)