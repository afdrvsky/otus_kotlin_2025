plugins {
    application
    id("build-jvm")
    alias(libs.plugins.shadowJar)
    id("build-docker")
}

application {
    mainClass.set("com.fedorovsky.mkdservice.app.kafka.MainKt")
}

docker {
    imageName = "mkdservice-app-kafka"
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation("com.fedorovsky.mkdservice.libs:mkdservice-lib-logging-kermit")

    implementation(project(":mkdservice-app-common"))

    // transport models
    implementation(project(":mkdservice-common"))
    implementation(project(":mkdservice-api-v1-kmp"))
    // logic
    implementation(project(":mkdservice-biz"))

    testImplementation(kotlin("test-junit"))
}

tasks {
    shadowJar {
        manifest {
            attributes(mapOf("Main-Class" to application.mainClass.get()))
        }
    }

    dockerBuild {
        dependsOn("shadowJar")
    }
}