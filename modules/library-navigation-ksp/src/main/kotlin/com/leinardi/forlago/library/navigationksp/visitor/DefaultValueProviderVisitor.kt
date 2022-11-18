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

package com.leinardi.forlago.library.navigationksp.visitor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.leinardi.forlago.library.navigation.annotation.NavGraphDestination
import com.leinardi.forlago.library.navigationksp.codegenerator.DefaultValueProviderCodeGenerator
import com.leinardi.forlago.library.navigationksp.ext.hasDefaultValue
import com.leinardi.forlago.library.navigationksp.ext.isInterface
import com.leinardi.forlago.library.navigationksp.ext.toDefaultValueProviderClassName
import com.leinardi.forlago.library.navigationksp.model.DefaultValueProviderModel
import com.squareup.kotlinpoet.ksp.writeTo

internal class DefaultValueProviderVisitor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : KSVisitorVoid() {
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val qualifiedName = classDeclaration.qualifiedName?.asString() ?: run {
            logger.error(
                "@${NavGraphDestination::class.simpleName} must target classes with qualified names",
                classDeclaration,
            )
            return
        }
        if (!classDeclaration.isInterface()) {
            logger.error(
                "@${NavGraphDestination::class.simpleName} cannot target non-interface $qualifiedName",
                classDeclaration,
            )
            return
        }
        if (classDeclaration.typeParameters.any()) {
            logger.error(
                "@${NavGraphDestination::class.simpleName} must interfaces with no type parameters",
                classDeclaration,
            )
            return
        }
        val model = getModel(classDeclaration)
        DefaultValueProviderCodeGenerator.generate(model).writeTo(codeGenerator = codeGenerator, aggregating = false)
    }

    private fun getModel(classDeclaration: KSClassDeclaration): DefaultValueProviderModel {
        val packageName = classDeclaration.packageName.asString()
        val defaultValueProviderClassName = classDeclaration.toDefaultValueProviderClassName()
        val propertiesWithDefaultValue = classDeclaration.getAllProperties().filter { it.hasDefaultValue() }.toList()
        return DefaultValueProviderModel(
            className = defaultValueProviderClassName,
            packageName = packageName,
            propertiesWithDefaultValue = propertiesWithDefaultValue,
        )
    }
}
