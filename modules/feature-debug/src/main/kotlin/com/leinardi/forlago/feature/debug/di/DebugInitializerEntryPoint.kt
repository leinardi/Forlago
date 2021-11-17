/*
 * Copyright 2021 Roberto Leinardi.
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

package com.leinardi.forlago.feature.debug.di

import android.content.Context
import com.leinardi.forlago.feature.debug.initializer.DebugInitializer
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DebugInitializerEntryPoint {
    fun inject(initializer: DebugInitializer)

    companion object {
        // a helper method to resolve the InitializerEntryPoint from the context
        fun resolve(context: Context): DebugInitializerEntryPoint {
            val appContext = context.applicationContext ?: throw IllegalStateException("Application context is null")
            return EntryPointAccessors.fromApplication(appContext, DebugInitializerEntryPoint::class.java)
        }
    }
}
