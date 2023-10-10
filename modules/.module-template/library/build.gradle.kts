plugins {
    id("forlago.android-library-conventions")
    id("com.google.dagger.hilt.android")
}

android {
    namespace ="com.leinardi.forlago.library.@placeholderlowercase@"
    resourcePrefix = "@placeholder_snake_case@_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-@placeholderlowercase@-consumer-rules.pro")
    }
}

dependencies {
    implementation(projects.modules.library@PlaceholderName@Api)
    implementation(libs.hilt.android)

    ksp(libs.hilt.compiler)
}
