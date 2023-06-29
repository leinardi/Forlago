import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("forlago.android-library-conventions")
    alias(libs.plugins.apollo)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.leinardi.forlago.library.network.api"
    resourcePrefix = "network_api_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-network-api-consumer-rules.pro")
    }
}

apollo {
    service("forlago") {
        packageName.set("com.leinardi.forlago.library.network.api.graphql")
        generateApolloMetadata.set(true)
    }
}

dependencies {
    api(libs.apollo)
    api(libs.apollo.adapters)
    api(libs.apollo.cache)
    api(libs.retrofit)
    implementation(libs.apollo.cache.sqlite)
    implementation(libs.kotlinx.serialization)
}

// Workaround for https://github.com/detekt/detekt/issues/4743
tasks{
    withType<Detekt>().configureEach {
        exclude("com/leinardi/forlago/library/network/api/graphql/**/*.kt")
    }
    register<Exec>("refreshGraphQlSchema") {
        val endpoint = "https://apollo-fullstack-tutorial.herokuapp.com/graphql"
        val schemaPath = "modules/library-network-api/src/main/graphql/schema.graphqls"
        workingDir(rootDir)
        commandLine(
            "./gradlew",
            ":module:library-network-api:downloadApolloSchema",
            "--endpoint=$endpoint",
            "--schema=$schemaPath",
        )
    }
}
