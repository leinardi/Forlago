package com.leinardi.forlago.gmd

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.invoke

/**
 * Configure project for Gradle managed devices
 */
@Suppress("UnstableApiUsage")
internal fun CommonExtension<*, *, *, *, *, *>.configureGradleManagedDevices(
    minApiLevel: Int,
    maxApiLevel: Int,
    device: String = "Pixel 2",
    systemImageSource: String = "google",
) {
    val allDevices: List<DeviceConfig> = (minApiLevel..maxApiLevel).map { apiLevel -> DeviceConfig(device, apiLevel, systemImageSource) }

    testOptions {
        managedDevices {
            devices {
                allDevices.forEach { deviceConfig ->
                    create<ManagedVirtualDevice>(deviceConfig.taskName).apply {
                        this.device = deviceConfig.device
                        apiLevel = deviceConfig.apiLevel
                        this.systemImageSource = deviceConfig.systemImageSource
                    }
                }
            }
        }
    }
}

private data class DeviceConfig(
    val device: String,
    val apiLevel: Int,
    val systemImageSource: String,
) {
    val taskName = buildString {
        append(device.lowercase().replace(" ", ""))
        append("Api")
        append(apiLevel.toString())
        append(systemImageSource.replace("-", ""))
    }
}
