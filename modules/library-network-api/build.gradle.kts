/*
 * Copyright 2024 Roberto Leinardi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
tasks {
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
