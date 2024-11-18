plugins {
    alias(libs.plugins.todonote.feature.domain)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.note.note.domain"
}

dependencies {
    implementation(libs.androidx.paging.common)
    implementation(libs.kotlinx.serialization.json)
}