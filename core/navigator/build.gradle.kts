plugins {
    alias(libs.plugins.todonote.android.library)
    alias(libs.plugins.todonote.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.flab.core.navigator"
}

dependencies {

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.navigation)
    implementation(projects.core.domain)
}