package com.fedorovsky.mkdservice.auth

import com.fedorovsky.mkdservice.common.permissions.MkdUserGroups
import com.fedorovsky.mkdservice.common.permissions.MkdUserPermissions

/**
 * На вход подаем группы/роли из JWT, на выход получаем пермишины, соответствующие этим группам/ролям
 */
fun resolveChainPermissions(
    groups: Iterable<MkdUserGroups>,
) = mutableSetOf<MkdUserPermissions>()
    .apply {
        // Группы, добавляющие права (пермишины)
        addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
        // Группы, запрещающие права (пермишины)
        removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() }.toSet())
    }
    .toSet()

//
private val groupPermissionsAdmits = mapOf(
    MkdUserGroups.USER to setOf(
        MkdUserPermissions.READ_OWN,
        MkdUserPermissions.CREATE_OWN,
    ),
    MkdUserGroups.MODERATOR_MKD to setOf(
        MkdUserPermissions.CREATE_ALL,
        MkdUserPermissions.UPDATE_ALL,
        MkdUserPermissions.DELETE_ALL,
        MkdUserPermissions.READ_ALL

    ),
    MkdUserGroups.ADMIN_METER to setOf(
        MkdUserPermissions.DELETE_ALL,
        MkdUserPermissions.DELETE_ALL,
    ),
    MkdUserGroups.TEST to setOf(),
    MkdUserGroups.BAN_METER to setOf(),
)

private val groupPermissionsDenys = mapOf(
    MkdUserGroups.USER to setOf(
        MkdUserPermissions.DELETE_ALL,
        MkdUserPermissions.UPDATE_ALL,
        MkdUserPermissions.DELETE_OWN,
        MkdUserPermissions.UPDATE_OWN,
    ),
    MkdUserGroups.MODERATOR_MKD to setOf(),
    MkdUserGroups.ADMIN_METER to setOf(),
    MkdUserGroups.TEST to setOf(),
    MkdUserGroups.BAN_METER to setOf(
        MkdUserPermissions.UPDATE_OWN,
        MkdUserPermissions.CREATE_OWN,
    ),
)
