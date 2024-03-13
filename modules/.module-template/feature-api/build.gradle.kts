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

plugins {
    id("forlago.android-library-conventions")
}

android {
    namespace = "com.leinardi.forlago.feature.@placeholderlowercase@.api"
    resourcePrefix = "@placeholder_snake_case@_api_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-@placeholderlowercase@-api-consumer-rules.pro")
    }
}
