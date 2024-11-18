plugins {
    alias(libs.plugins.todonote.android.application.compose)
    alias(libs.plugins.todonote.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.note.githubtodo"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)

    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.database)

    implementation(projects.auth.data)
    implementation(projects.auth.domain)
    implementation(projects.auth.presentation)

    implementation(projects.home.data)
    implementation(projects.home.domain)
    implementation(projects.home.presentation)

    implementation(projects.note.data)
    implementation(projects.note.domain)
    implementation(projects.note.presentation)
}