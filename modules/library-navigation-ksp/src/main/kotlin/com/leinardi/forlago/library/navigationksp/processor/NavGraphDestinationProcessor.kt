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
import com.google.devtools.ksp.validate
import com.leinardi.forlago.library.navigation.annotation.NavGraphDestination
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

        logger.info("Generate Destinations")

        val unableToProcess = navGraphDestinationClassDeclarations.filterNot { it.validate() }

        navGraphDestinationClassDeclarations
            .filter { it.validate() }
            .forEach { navGraphDestinationClassDeclaration ->
                navGraphDestinationClassDeclaration.accept(
                    NavGraphDestinationVisitor(
                        resolver = resolver,
                        logger = logger,
                        codeGenerator = codeGenerator,
                    ),
                    Unit,
                )
            }
        return unableToProcess
    }
}
