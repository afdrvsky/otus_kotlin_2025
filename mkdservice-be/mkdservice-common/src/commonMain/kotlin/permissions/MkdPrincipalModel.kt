package com.fedorovsky.mkdservice.common.permissions

import com.fedorovsky.mkdservice.common.models.MkdUserId

data class MkdPrincipalModel(
    val id: MkdUserId = MkdUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val ownFlats: List<String> = emptyList(),
    val groups: Set<MkdUserGroups> = emptySet()
) {
    fun genericName() = "$fname $mname $lname"
    companion object {
        val NONE = MkdPrincipalModel()
    }
}