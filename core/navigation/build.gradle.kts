plugins {
    alias(libs.plugins.todonote.jvm.library)
    alias(libs.plugins.kotlin.serialization)
}


dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
}