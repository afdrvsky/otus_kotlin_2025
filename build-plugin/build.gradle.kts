plugins {
    `kotlin-dsl`
}

group = "com.fedorovsky"
version = "1.0-SNAPSHOT"

gradlePlugin {
    plugins {
        register("build-jvm") {
            id = "build-jvm"
            implementationClass = "com.fedorovsky.mkdservice.plugin.BuildPluginJvm"
        }
        register("build-kmp") {
            id = "build-kmp"
            implementationClass = "com.fedorovsky.mkdservice.plugin.BuildPluginMultiplatform"
        }
        register("build-docker") {
            id = "build-docker"
            implementationClass = "com.fedorovsky.mkdservice.plugin.DockerPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // enable Ktlint formatting
//    add("detektPlugins", libs.plugin.detektFormatting)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.plugin.kotlin)
//    implementation(libs.plugin.dokka)
    implementation(libs.plugin.binaryCompatibilityValidator)
//    implementation(libs.plugin.mavenPublish)
}