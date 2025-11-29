package com.fedorovsky.mkdservice.auth

import com.fedorovsky.mkdservice.common.models.MeterCommand
import com.fedorovsky.mkdservice.common.permissions.MkdPrincipalRelations
import com.fedorovsky.mkdservice.common.permissions.MkdUserPermissions

fun checkPermitted(
    command: MeterCommand,
    relations: Iterable<MkdPrincipalRelations>,
    permissions: Iterable<MkdUserPermissions>,
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class AccessTableConditions(
    val command: MeterCommand,
    val permission: MkdUserPermissions,
    val relation: MkdPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = MeterCommand.CREATE,
        permission = MkdUserPermissions.CREATE_OWN,
        relation = MkdPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = MeterCommand.CREATE,
        permission = MkdUserPermissions.CREATE_ALL,
        relation = MkdPrincipalRelations.MODERATABLE,
    ) to true,

    // Read
    AccessTableConditions(
        command = MeterCommand.READ,
        permission = MkdUserPermissions.READ_OWN,
        relation = MkdPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = MeterCommand.READ,
        permission = MkdUserPermissions.READ_ALL,
        relation = MkdPrincipalRelations.MODERATABLE,
    ) to true,

    // Update
    AccessTableConditions(
        command = MeterCommand.UPDATE,
        permission = MkdUserPermissions.UPDATE_OWN,
        relation = MkdPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = MeterCommand.UPDATE,
        permission = MkdUserPermissions.UPDATE_ALL,
        relation = MkdPrincipalRelations.MODERATABLE,
    ) to true,

    // Delete
    AccessTableConditions(
        command = MeterCommand.DELETE,
        permission = MkdUserPermissions.DELETE_OWN,
        relation = MkdPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = MeterCommand.DELETE,
        permission = MkdUserPermissions.DELETE_ALL,
        relation = MkdPrincipalRelations.MODERATABLE,
    ) to true,
)