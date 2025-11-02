package com.fedorovsky.mkdservice.common.models

data class MeterReading(
    var id: MeterReadingId = MeterReadingId.NONE,
    var dateTime: String = "",
    var amount: Amount = Amount.NONE,
    var unit: MeterReadingUnit = MeterReadingUnit.NONE,
    var meterId: MeterId = MeterId.NONE,
    var apartmentId: ApartmentId = ApartmentId.NONE,
    var lock: MeterReadingLock = MeterReadingLock.NONE,
) {
    fun deepCopy(): MeterReading = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = MeterReading()
    }
}
