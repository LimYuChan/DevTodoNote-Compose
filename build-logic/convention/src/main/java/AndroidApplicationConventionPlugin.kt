import com.android.build.api.dsl.ApplicationExtension
import com.note.convention.ExtensionType
import com.note.convention.configureBuildTypes
import com.note.convention.configureKotlinAndroid
import com.note.convention.findVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.APPLICATION
                )

                defaultConfig {
                    applicationId = findVersion("projectApplicationId")

                    targetSdk = findVersion("projectTargetSdkVersion").toInt()

                    versionCode = findVersion("projectVersionCode").toInt()
                    versionName = findVersion("projectVersionName")
                }
            }
        }
    }
}