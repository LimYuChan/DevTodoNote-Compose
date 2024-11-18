import com.note.convention.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FeatureDomainLayerConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("todonote.android.library")
                apply("todonote.android.hilt")
            }

            dependencies {
                add("implementation", project(":core:domain"))
            }
        }
    }
}