-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.leinardi.forlago.**$$serializer { *; }
-keepclassmembers class com.leinardi.forlago.** {
    *** Companion;
}
-keepclasseswithmembers class com.leinardi.forlago.** {
    kotlinx.serialization.KSerializer serializer(...);
}
# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
