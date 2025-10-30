dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}


//plugins {
//    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
//}

rootProject.name = "mkdservice-be"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("mkdservice-api-v1-kmp")
include("mkdservice-common")
include("mkdservice-stubs")
include("mkdservice-app-ktor")
include("mkdservice-app-common")
include("mkdservice-biz")
include("mkdservice-app-kafka")
include("mkdservice-api-log")
include("mkdservice-repo-common")
include("mkdservice-repo-inmemory")
include("mkdservice-repo-tests")
include("mkdservice-repo-cassandra")