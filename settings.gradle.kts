pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "DevTodoNote-Compose"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:common")
include(":core:data")
include(":core:domain")
include(":core:designsystem")
include(":core:ui")
include(":core:database")
include(":auth:data")
include(":auth:domain")
include(":auth:presentation")
include(":home:data")
include(":home:domain")
include(":home:presentation")
include(":note:data")
include(":note:domain")
include(":note:presentation")
include(":core:navigator")
