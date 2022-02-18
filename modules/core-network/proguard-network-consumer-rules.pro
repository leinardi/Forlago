-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.leinardi.forlago.**$$serializer { *; }
-keepclassmembers class com.leinardi.forlago.** {
    *** Companion;
}
-keepclasseswithmembers class com.leinardi.forlago.** {
    kotlinx.serialization.KSerializer serializer(...);
}
