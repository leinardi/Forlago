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

package com.leinardi.forlago.apollo

import com.apollographql.apollo3.compiler.ApolloCompilerPlugin
import com.apollographql.apollo3.compiler.Transform
import com.apollographql.apollo3.compiler.codegen.kotlin.KotlinOutput
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeSpec

class ChangeAdaptersModifierToPublicPlugin : ApolloCompilerPlugin {
    override fun kotlinOutputTransform(): Transform<KotlinOutput> =
        object : Transform<KotlinOutput> {
            override fun transform(input: KotlinOutput): KotlinOutput =
                KotlinOutput(
                    fileSpecs = input.fileSpecs.map {
                        it.toBuilder()
                            .apply { members.replaceAll { member -> if (member is TypeSpec) member.changeAdaptersModifierToPublic() else member } }
                            .build()
                    },
                    codegenMetadata = input.codegenMetadata,
                )

            private fun TypeSpec.changeAdaptersModifierToPublic(): TypeSpec = toBuilder()
                .apply {
                    if (superinterfaces
                            .keys
                            .filterIsInstance<ParameterizedTypeName>()
                            .any { it.rawType == ClassName("com.apollographql.apollo3.api", "Adapter") }
                    ) {
                        modifiers.removeIf { it == KModifier.PRIVATE }
                        addModifiers(KModifier.PUBLIC)
                    }

                    // Recurse on nested types
                    typeSpecs.replaceAll { it.changeAdaptersModifierToPublic() }
                }
                .build()
        }
}
