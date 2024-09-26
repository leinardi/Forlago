package com.leinardi.forlago.library.autobind.ksp.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.leinardi.forlago.library.annotation.AutoBind
import com.leinardi.forlago.library.autobind.ksp.visitor.AutoBindVisitor

@Suppress("UnusedPrivateMember")
internal class AutoBindProcessor(
    private val options: Map<String, String>,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("options: $options")
        val autoBindClassDeclarations = resolver.getSymbolsWithAnnotation(checkNotNull(AutoBind::class.qualifiedName))
            .filterIsInstance<KSClassDeclaration>()
            .toList()

        logger.info("Generate AutoBinds")

        val unableToProcess = autoBindClassDeclarations.filterNot { it.validate() }

        autoBindClassDeclarations
            .filter { it.validate() }
            .forEach { autoBindClassDeclaration ->
                autoBindClassDeclaration.accept(
                    AutoBindVisitor(
                        logger = logger,
                        codeGenerator = codeGenerator,
                    ),
                    Unit,
                )
            }
        return unableToProcess
    }
}
