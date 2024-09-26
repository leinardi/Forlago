package com.leinardi.forlago.library.autobind.ksp.codegenerator

import com.leinardi.forlago.library.autobind.ksp.model.AutoBindModel
import com.leinardi.forlago.library.ksp.common.codegenerator.CodeGenerator
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn

internal object AutoBindCodeGenerator : CodeGenerator<AutoBindModel> {
    override fun generate(model: AutoBindModel): FileSpec = FileSpec.builder(
        packageName = model.packageName,
        fileName = model.moduleName,
    ).apply {
        indent("    ")
        addFileComment("This is a generated file. Do not modify.")
        addType(generateModuleInterface(model))
    }.build()

    private fun generateModuleInterface(model: AutoBindModel): TypeSpec =
        TypeSpec.interfaceBuilder(model.moduleName).apply {
            addModifiers(KModifier.INTERNAL)
            addAnnotation(AnnotationSpec.builder(Module::class).build())
            val installInAnnotation = AnnotationSpec.builder(InstallIn::class)
                .apply { model.components.forEach { ksType -> addMember("%T::class", ksType.toClassName()) } }
                .build()
            addAnnotation(installInAnnotation)
            addFunction(generateBindFun(model))
            addOriginatingKSFile(model.containingFile)
        }.build()

    private fun generateBindFun(model: AutoBindModel): FunSpec =
        FunSpec.builder("bind${model.`interface`.toClassName().simpleName}")
            .addModifiers(KModifier.ABSTRACT)
            .addAnnotation(Binds::class)
            .addParameter(ParameterSpec.builder("impl", model.implementation.toTypeName()).build())
            .returns(model.`interface`.toTypeName())
            .addOriginatingKSFile(model.containingFile)
            .build()
}
