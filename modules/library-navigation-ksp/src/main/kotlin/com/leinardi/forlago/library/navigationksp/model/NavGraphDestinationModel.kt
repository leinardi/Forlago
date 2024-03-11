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

package com.leinardi.forlago.library.navigationksp.model

import com.google.devtools.ksp.symbol.KSFile
import com.leinardi.forlago.library.navigationksp.codegenerator.CodeGeneratorModel
import com.squareup.kotlinpoet.TypeName

internal data class NavGraphDestinationModel(
    val name: String,
    val className: String,
    val packageName: String,
    val routePrefix: String?,
    val deepLink: Boolean,
    val arguments: List<ArgumentModel>,
    val containingFile: KSFile,
) : CodeGeneratorModel {
    data class ArgumentModel(
        val simpleName: String,
        val typeName: TypeName,
        val navTypePropertyName: String,
        val isNullable: Boolean,
        val defaultValue: DefaultValue?,
    )
}
