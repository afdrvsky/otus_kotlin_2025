plugins {
    kotlin("jvm") version "2.1.10"
}

group = "com.fedorovsky"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }
    group = rootProject.group
    version = rootProject.version

    tasks.register("prepareKotlinBuildScriptModel")
}
