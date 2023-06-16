plugins {
    id("forlago.android-library-conventions")
}

android {
    namespace = "com.leinardi.forlago.library.ui.api"
    resourcePrefix = "ui_api_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-ui-api-consumer-rules.pro")
    }
}

dependencies {
    api(libs.androidx.appcompat)
}