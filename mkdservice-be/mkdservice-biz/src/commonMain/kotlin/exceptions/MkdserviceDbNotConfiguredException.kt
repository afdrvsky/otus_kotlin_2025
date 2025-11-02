package com.fedorovsky.mkdservice.biz.exceptions

import com.fedorovsky.mkdservice.common.models.MeterWorkMode

class MkdserviceDbNotConfiguredException(val workMode: MeterWorkMode) : Exception(
    "Database is not configured properly for workmode $workMode"
)