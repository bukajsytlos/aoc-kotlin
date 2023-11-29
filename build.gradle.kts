import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
}

allprojects {
    group = "sk.soliont"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    apply(plugin = "idea")
    apply(plugin = rootProject.libs.plugins.org.jetbrains.kotlin.jvm.get().pluginId)

    dependencies {
        testImplementation(rootProject.libs.kotest.runner.junit5)
        testImplementation(rootProject.libs.kotest.assertions.core)
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.add("-Xjsr305=strict")
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
}