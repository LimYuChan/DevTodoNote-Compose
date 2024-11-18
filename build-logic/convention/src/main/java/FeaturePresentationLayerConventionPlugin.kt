import com.android.build.api.dsl.LibraryExtension
import com.note.convention.ExtensionType
import com.note.convention.configureAndroidCompose
import com.note.convention.findLibrary
import com.note.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class FeaturePresentationLayerConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("todonote.android.library")
                apply("todonote.android.test")
                apply("todonote.android.hilt")
            }

            extensions.getByType<LibraryExtension>().run {
                configureAndroidCompose(this, ExtensionType.FEATURE)
            }


            dependencies {
                add("implementation", project(":core:common"))
                add("implementation", project(":core:domain"))
            }
        }
    }
}