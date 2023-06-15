package com.leinardi.forlago.ext

import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.getByType

interface ForlagoAppsConfigExt : ExtensionAware {
    val applicationId: Property<String>
}

internal inline val AppsConfigExt.forlago: ForlagoAppsConfigExt get() = extensions.getByType()
