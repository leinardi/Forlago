/*
 * Copyright 2022 Roberto Leinardi.
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

plugins {
    id("forlago.android-feature-conventions")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.leinardi.forlago.feature.account"
    resourcePrefix = "account_"

    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-account-consumer-rules.pro")
    }

    buildTypes {
        named("debug") {
            buildConfigField("String", "ACCOUNT_TYPE", "\"${config.android.accountType.get()}.debug\"")
            resValue("string", "account_type", "${config.android.accountType.get()}.debug")
        }
        named("release") {
            buildConfigField("String", "ACCOUNT_TYPE", "\"${config.android.accountType.get()}\"")
            resValue("string", "account_type", config.android.accountType.get())
        }
    }
}

dependencies {
    api(projects.modules.featureAccountApi)
    implementation(projects.modules.libraryNetworkApi)
    implementation(projects.modules.libraryPreferencesApi)

    implementation(libs.aboutlibraries)
    implementation(libs.aboutlibraries.core)
    implementation(libs.kotlinx.serialization)
    implementation(libs.tink)

    testImplementation(projects.modules.libraryNetworkApi)
}
