plugins {
    id("build-jvm")
    id("kotlin-kapt")
    //alias(libs.plugins.kotlin.kapt)
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(projects.mkdserviceCommon)
    implementation(projects.mkdserviceRepoCommon)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.jdk9)
    implementation(libs.uuid)
    implementation(libs.bundles.cassandra)
    kapt(libs.db.cassandra.kapt)

    testImplementation(projects.mkdserviceRepoTests)
    testImplementation("org.testcontainers:cassandra:1.21.3")
    testImplementation(libs.logback)
//    testImplementation(libs.testcontainers.cassandra)
}
