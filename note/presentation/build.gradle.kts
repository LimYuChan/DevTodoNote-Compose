plugins {
    alias(libs.plugins.todonote.feature.presentation)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.note.note.presentation"
}

dependencies {
    implementation(projects.note.domain)
    implementation(libs.androidx.paging.compose)
    implementation(libs.kotlinx.serialization.json)
}