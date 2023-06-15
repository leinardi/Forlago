plugins {
    id("forlago.android-library-conventions")
}

android {
    namespace = "com.leinardi.forlago.library.@placeholderlowercase@.api"
    resourcePrefix = "@placeholder_snake_case@_api_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-@placeholderlowercase@-api-consumer-rules.pro")
    }
}
