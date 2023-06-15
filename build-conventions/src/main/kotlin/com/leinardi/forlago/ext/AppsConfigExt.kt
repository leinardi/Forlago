package com.leinardi.forlago.ext

import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.getByType

interface AppsConfigExt : ExtensionAware {
    val deepLinkSchema: Property<String>
}

internal inline val ConfigExt.apps: AppsConfigExt get() = extensions.getByType()
