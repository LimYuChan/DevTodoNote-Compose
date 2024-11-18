import com.android.build.api.dsl.LibraryExtension
import com.note.convention.ExtensionType
import com.note.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("todonote.android.library")
                apply("todonote.android.test")
            }

            extensions.getByType<LibraryExtension>().run {
                configureAndroidCompose(this, ExtensionType.LIBRARY)
            }
        }
    }
}