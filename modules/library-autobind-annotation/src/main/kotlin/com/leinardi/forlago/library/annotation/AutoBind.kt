package com.leinardi.forlago.library.annotation

import dagger.hilt.GeneratesRootInput
import dagger.hilt.components.SingletonComponent
import kotlin.reflect.KClass

@GeneratesRootInput
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
public annotation class AutoBind(
    /**
     * The Hilt components in which the generated module will be installed in.
     */
    val components: Array<KClass<*>> = [SingletonComponent::class],
)
