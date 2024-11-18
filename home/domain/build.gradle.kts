plugins {
    alias(libs.plugins.todonote.feature.domain)
}

android {
    namespace = "com.note.home.domain"
}

dependencies {
    implementation(libs.androidx.paging.common)
}