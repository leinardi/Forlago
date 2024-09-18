package com.leinardi.forlago.library.autobind.ksp.visitor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.leinardi.forlago.library.annotation.AutoBind
import com.leinardi.forlago.library.autobind.ksp.codegenerator.AutoBindCodeGenerator
import com.leinardi.forlago.library.autobind.ksp.model.AutoBindModel
import com.leinardi.forlago.library.ksp.common.ext.isInterface
import com.squareup.kotlinpoet.ksp.writeTo

internal class AutoBindVisitor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : KSVisitorVoid() {
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val qualifiedName = classDeclaration.qualifiedName?.asString() ?: run {
            logger.error("@${AutoBind::class.simpleName} must target classes with qualified names", classDeclaration)
            return
        }
        if (classDeclaration.isInterface()) {
            logger.error("@${AutoBind::class.simpleName} cannot target interfaces: $qualifiedName", classDeclaration)
            return
        }
        val model = getModel(classDeclaration, qualifiedName)
        AutoBindCodeGenerator.generate(model).writeTo(codeGenerator = codeGenerator, aggregating = false)
    }

    private fun getModel(
        classDeclaration: KSClassDeclaration,
        qualifiedName: String,
    ): AutoBindModel {
        val ksAnnotation = getAnnotation(classDeclaration, qualifiedName)
        val packageName = classDeclaration.packageName.asString()
        val annotatedClassSimpleName = classDeclaration.simpleName.asString()
        return AutoBindModel(
            packageName = packageName,
            moduleName = annotatedClassSimpleName + "AutoBindModule",
            `interface` = classDeclaration.superTypes.first().resolve(),
            implementation = classDeclaration.asType(emptyList()),
            components = getComponents(ksAnnotation),
            containingFile = checkNotNull(classDeclaration.containingFile),
        )
    }

    private fun getAnnotation(
        classDeclaration: KSClassDeclaration,
        qualifiedName: String,
    ) = classDeclaration.annotations
        .filter { it.shortName.asString().contains(checkNotNull(AutoBind::class.simpleName)) }
        .also { annotations ->
            require(annotations.count() == 1) {
                "$qualifiedName annotated with more than one @${AutoBind::class.simpleName}"
            }
        }
        .first()

    private fun getComponents(ksAnnotation: KSAnnotation): List<KSType> {
        val componentsArgument = ksAnnotation.arguments
            .firstOrNull { it.name?.asString() == "components" }
            ?: error("AutoBind annotation does not have a components argument")

        val componentsValue = componentsArgument.value as? List<*>
            ?: error("Invalid components argument type in AutoBind annotation")

        return componentsValue.mapNotNull { it as? KSType }
    }
}
