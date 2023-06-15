import com.leinardi.forlago.ext.android
import com.leinardi.forlago.ext.config
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("forlago.config-conventions")
}

kotlin {
    sourceSets.all {
        languageSettings.progressiveMode = true // deprecations and bug fixes for unstable code take effect immediately
    }
}

tasks {
    withType<Test> {
        testLogging.events("skipped", "failed")
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = config.android.javaVersion.get().toString()
    }
}
