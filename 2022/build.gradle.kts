plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlin.serialization.json)
    implementation(project(":common"))
    testImplementation(project(":common"))
}