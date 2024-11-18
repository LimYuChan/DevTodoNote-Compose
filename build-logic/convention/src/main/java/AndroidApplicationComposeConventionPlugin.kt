import com.android.build.api.dsl.ApplicationExtension
import com.note.convention.ExtensionType
import com.note.convention.configureAndroidCompose
import com.note.convention.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("todonote.android.application")
            }

            extensions.getByType<ApplicationExtension>().run {
                configureAndroidCompose(this, ExtensionType.APPLICATION)
            }

        }
    }
}