package com.leinardi.forlago.library.autobind.ksp.model

import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSType
import com.leinardi.forlago.library.ksp.common.codegenerator.CodeGeneratorModel

internal data class AutoBindModel(
    val packageName: String,
    val moduleName: String,
    val `interface`: KSType,
    val implementation: KSType,
    val components: List<KSType>,
    val containingFile: KSFile,
) : CodeGeneratorModel
