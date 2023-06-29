/*
 * Copyright 2023 Roberto Leinardi.
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

package com.leinardi.forlago.library.navigationksp.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.validate
import com.leinardi.forlago.library.navigation.annotation.DefaultValueProvider
import com.leinardi.forlago.library.navigation.annotation.HasDefaultValue
import com.leinardi.forlago.library.navigation.annotation.NavGraphDestination
import com.leinardi.forlago.library.navigationksp.ext.toDefaultValueProviderClassName
import com.leinardi.forlago.library.navigationksp.visitor.DefaultValueProviderVisitor
import com.leinardi.forlago.library.navigationksp.visitor.NavGraphDestinationVisitor

@Suppress("UnusedPrivateMember")
internal class NavGraphDestinationProcessor(
    private val options: Map<String, String>,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("options: $options")
        val navGraphDestinationClassDeclarations = resolver.getSymbolsWithAnnotation(checkNotNull(NavGraphDestination::class.qualifiedName))
            .filterIsInstance<KSClassDeclaration>()
            .toList()
        val hasDefaultValuePropertyDeclarations = resolver.getSymbolsWithAnnotation(checkNotNull(HasDefaultValue::class.qualifiedName))
            .filterIsInstance<KSPropertyDeclaration>()
            .toList()
        val defaultValueProviderClassDeclarations = resolver.getSymbolsWithAnnotation(checkNotNull(DefaultValueProvider::class.qualifiedName))
            .filterIsInstance<KSClassDeclaration>()
            .toList()
        return if (hasDefaultValuePropertyDeclarations.isNotEmpty()) {
            logger.info("Generate DefaultValueProviders")
            navGraphDestinationClassDeclarations
                .forEach { navGraphDestinationClassDeclaration ->
                    if (hasDefaultValuePropertyDeclarations.any { it.parent == navGraphDestinationClassDeclaration }) {
                        navGraphDestinationClassDeclaration.accept(DefaultValueProviderVisitor(logger, codeGenerator), Unit)
                    }
                }
            navGraphDestinationClassDeclarations +
                defaultValueProviderClassDeclarations +
                hasDefaultValuePropertyDeclarations.filterNot { it.validate() }
        } else {
            logger.info("Generate Destinations")

            val unableToProcess = navGraphDestinationClassDeclarations.filterNot { it.validate() } +
                hasDefaultValuePropertyDeclarations.filterNot { it.validate() } +
                defaultValueProviderClassDeclarations.filterNot { it.validate() }

            navGraphDestinationClassDeclarations
                .filter { it.validate() }
                .forEach { navGraphDestinationClassDeclaration ->
                    var defaultValueProviderClassDeclaration: KSClassDeclaration? = null
                    defaultValueProviderClassDeclarations.forEach { classDeclaration ->
                        val hasDefaultValueProvider = classDeclaration.superTypes
                            .any {
                                it.toString() == navGraphDestinationClassDeclaration.toDefaultValueProviderClassName() &&
                                    it.resolve().declaration.packageName.asString() == navGraphDestinationClassDeclaration.packageName.asString()
                            }
                        if (hasDefaultValueProvider) {
                            defaultValueProviderClassDeclaration = classDeclaration
                        }
                    }
                    navGraphDestinationClassDeclaration.accept(
                        NavGraphDestinationVisitor(
                            defaultValueProviderClassDeclaration = defaultValueProviderClassDeclaration,
                            resolver = resolver,
                            logger = logger,
                            codeGenerator = codeGenerator,
                        ),
                        Unit,
                    )
                }
            unableToProcess
        }
    }
}
