plugins {
    id("forlago.kotlin-library-conventions")
    id("com.google.devtools.ksp")
    id("forlago.detekt-conventions")
}

ksp {
    arg("autoserviceKsp.verify", "true")
    arg("autoserviceKsp.verbose", "true")
}

dependencies {
    implementation(projects.modules.libraryNavigationAnnotation)

    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.ksp.symbol.processing.api)
}
