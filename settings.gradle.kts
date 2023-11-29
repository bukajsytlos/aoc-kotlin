rootProject.name = "aoc-kotlin"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
    id("de.fayard.refreshVersions") version "0.60.3"
}

fileTree(".") {
    include("**/build.gradle")
    include("**/*.gradle.kts")
    exclude("buildSrc/**")
    exclude("build.gradle.kts")
    exclude("settings.gradle.kts")
}.map {
    relativePath(it.parent)
        .replace(File.separator, ":")
}.forEach {
    include(it)
}
