package com.leinardi.androidtemplateproject.logging

import timber.log.Timber

class DebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement) =
        "(${element.fileName}:${element.lineNumber})"
}
