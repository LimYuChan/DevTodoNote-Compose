plugins {
    alias(libs.plugins.todonote.android.compose.library)
}

android {
    namespace = "com.note.core.ui"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
    implementation(libs.androidx.lifecycle.runtime.compose)
}