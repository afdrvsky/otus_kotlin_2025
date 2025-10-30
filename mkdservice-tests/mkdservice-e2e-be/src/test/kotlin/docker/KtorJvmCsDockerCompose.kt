package com.fedorovsky.mkdservice.e2e.be.docker

import com.fedorovsky.mkdservice.e2e.be.base.AbstractDockerCompose

object KtorJvmCsDockerCompose : AbstractDockerCompose(
    "mkdservice",
    8080,
    "docker-compose-ktor-cs-jvm.yml",
)