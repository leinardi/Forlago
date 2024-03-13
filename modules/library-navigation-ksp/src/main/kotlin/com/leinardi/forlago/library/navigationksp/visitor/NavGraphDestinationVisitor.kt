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

package com.leinardi.forlago.library.navigationksp.visitor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.leinardi.forlago.library.navigation.annotation.NavGraphDestination
import com.leinardi.forlago.library.navigationksp.codegenerator.NavGraphDestinationCodeGenerator
import com.leinardi.forlago.library.navigationksp.common.getDefaultValue
import com.leinardi.forlago.library.navigationksp.ext.fieldByName
import com.leinardi.forlago.library.navigationksp.ext.isDataClass
import com.leinardi.forlago.library.navigationksp.ext.isInterface
import com.leinardi.forlago.library.navigationksp.ext.toNavTypePropertyName
import com.leinardi.forlago.library.navigationksp.model.NavGraphDestinationModel
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

internal class NavGraphDestinationVisitor(
    private val resolver: Resolver,
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
        if (!classDeclaration.isInterface() && !classDeclaration.isDataClass()) {
            logger.error(
                "@${NavGraphDestination::class.simpleName} cannot target only interfaces and data classes $qualifiedName",
                classDeclaration,
            )
            return
        }
        if (classDeclaration.isInterface()) {
            if (classDeclaration.getAllProperties().iterator().hasNext()) {
                logger.error(
                    "@${NavGraphDestination::class.simpleName} must not declare properties. Please use a data class instead.",
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
        }
        val model = getModel(classDeclaration, qualifiedName)
        NavGraphDestinationCodeGenerator.generate(model).writeTo(codeGenerator = codeGenerator, aggregating = false)
    }

    private fun getModel(
        classDeclaration: KSClassDeclaration,
        qualifiedName: String,
    ): NavGraphDestinationModel {
        val ksAnnotation = getAnnotation(classDeclaration, qualifiedName)
        val packageName = classDeclaration.packageName.asString()
        val annotatedClassSimpleName = classDeclaration.simpleName.asString()
        val destinationClassName = annotatedClassSimpleName + "Destination"
        val destinationName = ksAnnotation.fieldByName<String>("name")
            .takeIf { it != NavGraphDestination.Defaults.NULL }
            ?: annotatedClassSimpleName.lowercase()
        val routePrefix = ksAnnotation.fieldByName<String>("routePrefix")
            .takeIf { it != NavGraphDestination.Defaults.NULL }
        val deepLink = ksAnnotation.fieldByName<Boolean>("deepLink")
        val arguments = classDeclaration.getAllProperties()
            .map { property ->
                val argumentType = property.type.resolve()
                val isNullable = argumentType.isMarkedNullable
                val defaultValue = property.getDefaultValue(resolver)
                NavGraphDestinationModel.ArgumentModel(
                    simpleName = property.simpleName.asString(),
                    typeName = argumentType.toClassName().copy(nullable = isNullable),
                    navTypePropertyName = argumentType.toNavTypePropertyName(resolver),
                    isNullable = isNullable,
                    defaultValue = defaultValue,
                )
            }
            .toList()
        return NavGraphDestinationModel(
            name = destinationName,
            className = destinationClassName,
            packageName = packageName,
            deepLink = deepLink,
            routePrefix = routePrefix,
            arguments = arguments,
            containingFile = checkNotNull(classDeclaration.containingFile),
        )
    }

    private fun getAnnotation(
        classDeclaration: KSClassDeclaration,
        qualifiedName: String,
    ) = classDeclaration.annotations
        .filter { it.shortName.asString().contains(checkNotNull(NavGraphDestination::class.simpleName)) }
        .also { annotations ->
            require(annotations.count() == 1) {
                "$qualifiedName annotated with more than one @${NavGraphDestination::class.simpleName}"
            }
        }
        .first()
}
