package com.fedorovsky.mkdservice.e2e.be.docker

import com.fedorovsky.mkdservice.e2e.be.base.AbstractDockerCompose

object WiremockDockerCompose : AbstractDockerCompose(
    "app-wiremock", 8080, "docker-compose-wiremock.yml"
)
