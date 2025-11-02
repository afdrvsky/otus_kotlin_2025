import io.ktor.plugin.features.*
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

plugins {
    alias(libs.plugins.kotlinx.serialization)
    id("build-kmp")
//    id("io.ktor.plugin")
    alias(libs.plugins.ktor)
    id("build-docker")
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

ktor {
    configureNativeImage(project)
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.VERSION_21)
    }
}

jib {
    container.mainClass = application.mainClass.get()
}

docker {
    buildContext = project.layout.buildDirectory.dir("docker-x64").get().toString()
    imageName = "${project.name}-x64"
    dockerFile = "Dockerfile"
    imageTag = "${project.version}"
}


kotlin {
    // !!! Обязательно. Иначе не проходит сборка толстых джанриков в shadowJar
    jvm { withJava() }
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries {
            executable {
                entryPoint = "com.fedorovsky.mkdservice.app.ktor.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.server.cors)
                implementation(libs.ktor.server.yaml)
                implementation(libs.ktor.server.negotiation)
                implementation(libs.ktor.server.headers.response)
                implementation(libs.ktor.server.headers.caching)

//                // Для того, чтоб получать содержимое запроса более одного раза
//                В Application.main добавить `install(DoubleReceive)`
//                implementation("io.ktor:ktor-server-double-receive:${libs.versions.ktor.get()}")

                implementation(project(":mkdservice-common"))
                implementation(project(":mkdservice-app-common"))
                implementation(project(":mkdservice-biz"))

                // v1 api
                implementation(project(":mkdservice-api-v1-kmp"))

                // Stubs
                implementation(project(":mkdservice-stubs"))

                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.serialization.json)

                // DB
                implementation(libs.uuid)
                implementation(projects.mkdserviceRepoCommon)
//                implementation(projects.mkdserviceRepoStubs)
                implementation(projects.mkdserviceRepoInmemory)

                // logging
//                implementation(project(":ok-marketplace-api-log1"))
                implementation("com.fedorovsky.mkdservice.libs:mkdservice-lib-logging-common")
                implementation("com.fedorovsky.mkdservice.libs:mkdservice-lib-logging-kermit")
//                implementation("ru.otus.otuskotlin.marketplace.libs:ok-marketplace-lib-logging-socket")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(libs.ktor.server.test)
                implementation(libs.ktor.client.negotiation)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))

                // jackson
                implementation(libs.ktor.serialization.jackson)
                implementation(libs.ktor.server.calllogging)
                implementation(libs.ktor.server.headers.default)

                implementation(libs.logback)
                implementation(projects.mkdserviceRepoCassandra)

                // transport models
//                implementation(project(":ok-marketplace-api-v1-jackson"))
//                implementation(project(":ok-marketplace-api-v1-mappers"))
//
//                implementation("ru.otus.otuskotlin.marketplace.libs:ok-marketplace-lib-logging-logback")

            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

tasks {
    shadowJar {
        isZip64 = true
    }

    // Если ошибка: "Entry application.yaml is a duplicate but no duplicate handling strategy has been set."
    // Возникает из-за наличия файлов как в common, так и в jvm платформе
    withType(ProcessResources::class) {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    val linkReleaseExecutableLinuxX64 by getting(KotlinNativeLink::class)
    val nativeFileX64 = linkReleaseExecutableLinuxX64.binary.outputFile
    val linuxX64ProcessResources by getting(ProcessResources::class)
    dockerBuild {
        dependsOn(linkReleaseExecutableLinuxX64)
        dependsOn(linuxX64ProcessResources)
        group = "docker"
        doFirst {
            copy {
                from("Dockerfile") //.rename { "Dockerfile" }
                from(nativeFileX64)
                from(linuxX64ProcessResources.destinationDir)
                println("BUILD CONTEXT: ${buildContext.get()}")
                into(buildContext)
            }
        }
    }
}

