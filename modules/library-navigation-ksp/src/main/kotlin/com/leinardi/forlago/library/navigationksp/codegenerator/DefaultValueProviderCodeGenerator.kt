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

package com.leinardi.forlago.library.navigationksp.codegenerator

import com.leinardi.forlago.library.navigationksp.model.DefaultValueProviderModel
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName

internal object DefaultValueProviderCodeGenerator : CodeGenerator<DefaultValueProviderModel> {
    override fun generate(model: DefaultValueProviderModel) = FileSpec.builder(
        packageName = model.packageName,
        fileName = model.className,
    ).apply {
        indent("    ")
        addType(generateDefaultValueProviderInterface(model))
    }.build()

    private fun generateDefaultValueProviderInterface(model: DefaultValueProviderModel): TypeSpec =
        TypeSpec.interfaceBuilder(model.className).apply {
            model.propertiesWithDefaultValue.forEach { property ->
                addProperty(property.simpleName.asString(), property.type.resolve().toTypeName())
            }
        }.build()
}
