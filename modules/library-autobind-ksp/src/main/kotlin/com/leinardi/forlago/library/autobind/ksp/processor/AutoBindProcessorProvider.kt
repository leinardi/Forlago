package com.leinardi.forlago.library.autobind.ksp.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

internal class AutoBindProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        AutoBindProcessor(
            options = environment.options,
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
        )
}
