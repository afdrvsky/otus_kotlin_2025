package com.fedorovsky.mkdservice.common.exceptions

import com.fedorovsky.mkdservice.common.models.MeterCommand

class UnknownMeterCommand (command: MeterCommand) : Throwable("Wrong command $command at mapping toTransport stage")