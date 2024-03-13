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

package com.leinardi.forlago.library.navigationksp.codegenerator

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

import com.google.devtools.ksp.symbol.KSFile
import com.leinardi.forlago.library.navigationksp.ext.endControlFlowWithTrailingComma
import com.leinardi.forlago.library.navigationksp.model.DefaultValue
import com.leinardi.forlago.library.navigationksp.model.NavGraphDestinationModel
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import java.util.Locale

internal object NavGraphDestinationCodeGenerator : CodeGenerator<NavGraphDestinationModel> {
    private val CLASS_NAME_NAVIGATION_DESTINATION = ClassName("com.leinardi.forlago.library.navigation.api.destination", "NavigationDestination")
    private val CLASS_NAME_NAV_DEEP_LINK = ClassName("androidx.navigation", "NavDeepLink")
    private val CLASS_NAME_NAV_DEEP_LINK_DSL = ClassName("androidx.navigation", "navDeepLink")
    private val CLASS_NAME_NAV_NAMED_NAV_ARGUMENT = ClassName("androidx.navigation", "NamedNavArgument")
    private val CLASS_NAME_NAV_TYPE = ClassName("androidx.navigation", "NavType")
    private val CLASS_NAME_SAVED_STATE_HANDLE = ClassName("androidx.lifecycle", "SavedStateHandle")
    private val CLASS_NAME_URI_BUILDER = ClassName("android.net", "Uri").nestedClass("Builder")
    private val MEMBER_NAME_DEEP_LINK_SCHEME = MemberName(CLASS_NAME_NAVIGATION_DESTINATION.nestedClass("Companion"), "DEEP_LINK_SCHEME")
    private val MEMBER_NAME_NAV_ARGUMENT = MemberName("androidx.navigation", "navArgument")
    private val startedViaDeepLinkArgument = NavGraphDestinationModel.ArgumentModel(
        simpleName = "startedViaDeepLink",
        typeName = BOOLEAN,
        navTypePropertyName = "BoolType",
        isNullable = false,
        defaultValue = DefaultValue("true"),
    )

    override fun generate(model: NavGraphDestinationModel): FileSpec = FileSpec.builder(
        packageName = model.packageName,
        fileName = model.className,
    ).apply {
        indent("    ")
        model.arguments.mapNotNull { it.defaultValue?.imports }.flatten().forEach { addImport(it.packageName, it.simpleName) }
        addType(generateDestinationObject(model))
    }.build()

    private fun generateDestinationObject(model: NavGraphDestinationModel): TypeSpec {
        val nameProperty = generateNameProperty(model)
        val arguments = model.arguments
            .toMutableList()
            .apply { add(startedViaDeepLinkArgument) }
            .toList()
        val routeProperty = generateRouteProperty(model, arguments, nameProperty)

        return TypeSpec.objectBuilder(model.className).apply {
            superclass(CLASS_NAME_NAVIGATION_DESTINATION)
            addProperty(nameProperty)
            addProperty(routeProperty)
            if (model.deepLink) {
                addProperty(generateDeepLinksProperty(model))
            }
            if (arguments.isNotEmpty()) {
                addProperty(generateArgumentsProperty(arguments, model.containingFile))
            }
            addFunction(generateGetFun(model, nameProperty))
            if (arguments.isNotEmpty()) {
                addType(generateArgumentsGetters(arguments, model.containingFile))
            }
            addOriginatingKSFile(model.containingFile)
        }.build()
    }

    private fun generateNameProperty(model: NavGraphDestinationModel) =
        PropertySpec.builder("NAME", String::class, KModifier.CONST)
            .initializer("%S", model.name)
            .addOriginatingKSFile(model.containingFile)
            .build()

    private fun generateRouteProperty(
        model: NavGraphDestinationModel,
        arguments: List<NavGraphDestinationModel.ArgumentModel>,
        nameProperty: PropertySpec,
    ): PropertySpec =
        PropertySpec.builder("route", String::class, KModifier.OVERRIDE)
            .initializer(
                buildCodeBlock {
                    addStatement("%T()", CLASS_NAME_URI_BUILDER)
                    indent()
                    model.routePrefix?.let { prefix ->
                        addStatement(".appendEncodedPath(%S)", prefix)
                    }
                    addStatement(".appendPath(%N)", nameProperty)
                    if (arguments.isNotEmpty()) {
                        val (optional, mandatory) = partitionMandatoryOptionalArguments(arguments)
                        mandatory.forEach { argumentModel ->
                            addStatement(".appendEncodedPath(%S)", "{${argumentModel.simpleName}}")
                        }
                        if (optional.isNotEmpty()) {
                            addStatement(".encodedQuery(")
                            indent()
                            addStatement("%T()", CLASS_NAME_URI_BUILDER)
                            indent()
                            optional.forEach { argumentModel ->
                                addStatement(".appendQueryParameter(%S, %S)", argumentModel.simpleName, "{${argumentModel.simpleName}}")
                            }
                            addStatement(".build()")
                            addStatement(".query")
                            addStatement(".orEmpty(),")
                            unindent()
                            unindent()
                            addStatement(")")
                        }
                    }
                    addStatement(".build()")
                    addStatement(".toString()")
                    addStatement(".removePrefix(%S)", "/")
                    unindent()
                },
            )
            .addOriginatingKSFile(model.containingFile)
            .build()

    private fun generateArgumentsProperty(
        argumentModels: List<NavGraphDestinationModel.ArgumentModel>,
        containingFile: KSFile,
    ) = PropertySpec
        .builder(
            "arguments",
            List::class.asClassName().parameterizedBy(CLASS_NAME_NAV_NAMED_NAV_ARGUMENT),
            KModifier.OVERRIDE,
        )
        .initializer(
            buildCodeBlock {
                addStatement("listOf(")
                indent()
                argumentModels.forEach { model ->
                    beginControlFlow("%M(%S)", MEMBER_NAME_NAV_ARGUMENT, model.simpleName)
                    addStatement("type = %T.${model.navTypePropertyName}", CLASS_NAME_NAV_TYPE)
                    if (model.isNullable) {
                        addStatement("nullable = %L", true)
                    }
                    model.defaultValue?.let {
                        addStatement("defaultValue = %L", it.code)
                    }
                    endControlFlowWithTrailingComma()
                }
                unindent()
                add(")")
            },
        )
        .addOriginatingKSFile(containingFile)
        .build()

    private fun generateDeepLinksProperty(model: NavGraphDestinationModel) = PropertySpec
        .builder(
            "deepLinks",
            List::class.asClassName().parameterizedBy(CLASS_NAME_NAV_DEEP_LINK),
            KModifier.OVERRIDE,
        )
        .getter(
            FunSpec
                .getterBuilder()
                .addCode(
                    buildCodeBlock {
                        add("return listOf(\n")
                        indent()
                        beginControlFlow("%T", CLASS_NAME_NAV_DEEP_LINK_DSL)
                        addStatement("uriPattern = \"\${%M}://\$route\"", MEMBER_NAME_DEEP_LINK_SCHEME)
                        endControlFlowWithTrailingComma()
                        if (model.arguments.any { it.isNullable }) {
                            beginControlFlow("%T", CLASS_NAME_NAV_DEEP_LINK_DSL)
                            addStatement("uriPattern = \"\${%M}://\${route.substringBefore(\"?\")}\"", MEMBER_NAME_DEEP_LINK_SCHEME)
                            endControlFlowWithTrailingComma()
                        }
                        unindent()
                        add(")")
                    },
                )
                .build(),
        )
        .addOriginatingKSFile(model.containingFile)
        .build()

    private fun generateGetFun(model: NavGraphDestinationModel, nameProperty: PropertySpec) = FunSpec.builder("get")
        .apply {
            addParameters(
                model.arguments.map { argumentModel ->
                    ParameterSpec.builder(argumentModel.simpleName, argumentModel.typeName).apply {
                        argumentModel.defaultValue?.let { defaultValue("%L", it.code) }
                    }.build()
                },
            )
            returns(String::class)
            addStatement(
                "return %L",
                buildCodeBlock {
                    addStatement("%T().apply {", CLASS_NAME_URI_BUILDER)
                    indent()
                    model.routePrefix?.let { prefix ->
                        addStatement("appendEncodedPath(%S)", prefix)
                    }
                    addStatement("appendPath(%N)", nameProperty)
                    if (model.arguments.isNotEmpty()) {
                        val (optional, mandatory) = partitionMandatoryOptionalArguments(model.arguments)
                        mandatory.forEach { argumentModel ->
                            if (argumentModel.typeName.copy(nullable = false) == STRING) {
                                addStatement("appendPath(%L)", argumentModel.simpleName)
                            } else {
                                addStatement("appendPath(%L.toString())", argumentModel.simpleName)
                            }
                        }
                        optional.forEach { argumentModel ->
                            if (argumentModel.isNullable) {
                                addStatement(
                                    "%L?.let { appendQueryParameter(%S, it.toString()) }",
                                    argumentModel.simpleName,
                                    argumentModel.simpleName,
                                )
                            } else {
                                addStatement("appendQueryParameter(%S, %L.toString())", argumentModel.simpleName, argumentModel.simpleName)
                            }
                        }
                        addStatement("appendQueryParameter(%S, %L.toString())", startedViaDeepLinkArgument.simpleName, "false")
                    }
                    unindent()
                    addStatement("}")
                    indent()
                    addStatement(".build()")
                    addStatement(".toString()")
                    addStatement(".removePrefix(%S)", "/")
                    unindent()
                },
            )
        }
        .addOriginatingKSFile(model.containingFile)
        .build()

    private fun generateArgumentsGetters(
        arguments: List<NavGraphDestinationModel.ArgumentModel>,
        containingFile: KSFile,
    ): TypeSpec = TypeSpec.objectBuilder("Arguments")
        .apply {
            arguments.forEach { argumentModel ->
                addFunction(
                    FunSpec.builder("get${argumentModel.simpleName.replaceFirstChar { it.titlecase(Locale.ROOT) }}").apply {
                        val savedStateHandleParamName = CLASS_NAME_SAVED_STATE_HANDLE.simpleName.replaceFirstChar { it.lowercase(Locale.ROOT) }
                        addParameter(savedStateHandleParamName, CLASS_NAME_SAVED_STATE_HANDLE)
                        returns(argumentModel.typeName)
                        addStatement(
                            "return %L",
                            buildCodeBlock {
                                if (!argumentModel.isNullable) {
                                    addStatement("checkNotNull(")
                                    indent()
                                }
                                addStatement("$savedStateHandleParamName.get<%T>(%S)", argumentModel.typeName, argumentModel.simpleName)
                                if (!argumentModel.isNullable) {
                                    unindent()
                                    addStatement(")")
                                }
                            },
                        )
                    }.build(),
                )
            }
        }
        .addOriginatingKSFile(containingFile)
        .build()

    private fun partitionMandatoryOptionalArguments(arguments: List<NavGraphDestinationModel.ArgumentModel>) =
        arguments.partition { it.isNullable || it.typeName.copy(nullable = false) == BOOLEAN }
}
