plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "com.fedorovsky.mkdservice"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("specs-mkdservice-v1.yaml").toString())
    set("spec-log", specDir.file("specs-mkdservice-log.yaml").toString())
}

tasks {
    register("build" ) {
        group = "build"
    }
    register("clean" ) {
        group = "build"
        subprojects.forEach { proj ->
            println("PROJ $proj")
            proj.getTasksByName("clean", false).also {
                this@register.dependsOn(it)
            }
        }
    }
    register("check" ) {
        group = "verification"
        subprojects.forEach { proj ->
            println("PROJ $proj")
            proj.getTasksByName("check", false).also {
                this@register.dependsOn(it)
            }
        }
    }
    register("buildImages") {
        dependsOn(project("mkdservice-app-ktor").tasks.getByName("publishImageToLocalRegistry"))
        dependsOn(project("mkdservice-app-ktor").tasks.getByName("dockerBuild"))
    }
}