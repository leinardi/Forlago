plugins {
    id("forlago.android-library-conventions")
}

android {
    namespace = "com.leinardi.forlago.library.preferences.api"
    resourcePrefix = "preferences_api_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-preferences-api-consumer-rules.pro")
    }
}

dependencies {
    api(projects.modules.libraryUiApi)

    api(libs.androidx.datastore.preferences)
    implementation(libs.hilt.android)
}
