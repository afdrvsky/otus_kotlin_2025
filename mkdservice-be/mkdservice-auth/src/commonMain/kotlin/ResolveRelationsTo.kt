package com.fedorovsky.mkdservice.auth

import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.permissions.MkdPrincipalModel
import com.fedorovsky.mkdservice.common.permissions.MkdPrincipalRelations

fun MeterReading.resolveRelationsTo(principal: MkdPrincipalModel): Set<MkdPrincipalRelations> = setOfNotNull(
    MkdPrincipalRelations.NONE,
    MkdPrincipalRelations.OWN.takeIf { principal.ownFlats.contains(apartmentId.asString()) },
    MkdPrincipalRelations.MODERATABLE
)