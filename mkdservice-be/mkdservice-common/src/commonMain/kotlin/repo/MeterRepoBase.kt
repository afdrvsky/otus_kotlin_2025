package com.fedorovsky.mkdservice.common.repo

import com.fedorovsky.mkdservice.common.helpers.errorSystem

abstract class MeterRepoBase: IRepoMeter {

    protected suspend fun tryMeterMethod(block: suspend () -> IDbMeterResponse) = try {
        block()
    } catch (e: Throwable) {
        DbMeterResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryMetersMethod(block: suspend () -> IDbMetersResponse) = try {
        block()
    } catch (e: Throwable) {
        DbMetersResponseErr(errorSystem("methodException", e = e))
    }
}