package com.fedorovsky.mkdservice.common.models

import com.fedorovsky.mkdservice.common.permissions.MkdPrincipalRelations
import com.fedorovsky.mkdservice.states.common.statemachine.SMMeterStates

data class MeterReading(
    var id: MeterReadingId = MeterReadingId.NONE,
    var dateTime: String = "",
    var amount: Amount = Amount.NONE,
    var unit: MeterReadingUnit = MeterReadingUnit.NONE,
    var meterId: MeterId = MeterId.NONE,
    var apartmentId: ApartmentId = ApartmentId.NONE,
    var lock: MeterReadingLock = MeterReadingLock.NONE,

    var meterReadingState: SMMeterStates = SMMeterStates.NONE,

    // Результат вычисления отношений текущего пользователя (который сделал запрос) к текущему показанию
    var principalRelations: Set<MkdPrincipalRelations> = emptySet(),
    // Набор пермишинов, которые отдадим во фронтенд
    val permissionsClient: MutableSet<MkdMeterPermissionClient> = mutableSetOf(),
) {
    fun deepCopy(): MeterReading = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = MeterReading()
    }
}
