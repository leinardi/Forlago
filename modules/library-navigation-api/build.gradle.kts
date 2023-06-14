import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("forlago.android-library-conventions")
}

android {
    namespace = "com.leinardi.forlago.library.navigation.api"
    resourcePrefix = "navigation_api_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-navigation-api-consumer-rules.pro")
    }
}

dependencies {
    implementation(projects.modules.libraryNavigationAnnotation)
    ksp(projects.modules.libraryNavigationKsp)

    api(libs.androidx.navigation.compose)
}

// Workaround for https://github.com/detekt/detekt/issues/4743
tasks.withType<Detekt>().configureEach {
    exclude("com/leinardi/forlago/library/navigation/api/destination/**/*Destination.kt")
}

afterEvaluate {
    tasks.named("compileDebugKotlin").configure { shouldRunAfter(tasks.named("kspDebugKotlin")) }
}
