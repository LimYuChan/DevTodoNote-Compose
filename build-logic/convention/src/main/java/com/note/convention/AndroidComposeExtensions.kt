package com.note.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

internal fun Project.configureAndroidCompose (
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx.compose.bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
            add("implementation", findLibrary("androidx.core.ktx"))
            add("debugImplementation", findLibrary("androidx.ui.tooling.preview"))
            add("implementation", findLibrary("androidx.ui"))
            add("implementation", findLibrary("androidx.ui.graphics"))
            add("implementation", findLibrary("androidx.material3"))
            add("debugImplementation", findLibrary("androidx.ui.tooling"))

            if(extensionType != ExtensionType.LIBRARY) {
                add("debugImplementation", findLibrary("androidx.ui.test.manifest"))
                add("androidTestImplementation", findLibrary("androidx.ui.test.junit4"))

                add("implementation", findLibrary("androidx.lifecycle.runtime.ktx"))

                add("implementation", libs.findBundle("navigation").get())

                add("implementation", project.libs.findBundle("compose").get())
                add("debugImplementation", project.libs.findBundle("compose.debug").get())

                add("implementation", project(":core:navigator"))
                add("implementation", project(":core:designsystem"))
                add("implementation", project(":core:ui"))
            }
        }
    }
}