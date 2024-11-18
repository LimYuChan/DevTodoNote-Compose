plugins {
    alias(libs.plugins.todonote.android.library)
    alias(libs.plugins.todonote.android.hilt)
}

android {
    namespace = "com.note.core.common"

}

dependencies {
    implementation(libs.androidx.core.ktx)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}