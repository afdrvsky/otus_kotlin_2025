plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("com.fedorovsky.mkdservice:mkdservice-api-v1-kmp")
    implementation("com.fedorovsky.mkdservice:mkdservice-stubs")
    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.datatype)

    testImplementation(kotlin("test-junit5"))

    testImplementation(libs.logback)
    testImplementation(libs.kermit)

    testImplementation(libs.bundles.kotest)

    testImplementation(libs.testcontainers.core)
    testImplementation(libs.coroutines.core)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.okhttp)
}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
//        dependsOn(gradle.includedBuild(":ok-marketplace-app-spring").task("dockerBuildImage"))
//        dependsOn(gradle.includedBuild("com.fedorovsky.mkdservice:mkdservice-app-ktor").task("publishImageToLocalRegistry"))
//        dependsOn(gradle.includedBuild(":ok-marketplace-app-rabbit").task("dockerBuildImage"))
//        dependsOn(gradle.includedBuild(":ok-marketplace-app-kafka").task("dockerBuildImage"))
    }
}
