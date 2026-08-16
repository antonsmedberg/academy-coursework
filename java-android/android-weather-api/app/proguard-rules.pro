################################################################
# Bas: håll kvar annoteringar & signaturer (Retrofit/Serialization)
################################################################
-keepattributes Signature, Exceptions, InnerClasses, EnclosingMethod
-keepattributes *Annotation*

# Behåll Kotlin metadata (hjälper reflektion och tools)
-keep class kotlin.Metadata { *; }

################################################################
# Kotlinx.serialization – säkra serializers och @Serializable-klasser
################################################################
# Behåll serializers och deras companions
-keep,includedescriptorclasses class **$$serializer { *; }
-keepclassmembers class **$Companion { *; }
# Behåll metoden serializer() som används av libbet
-keepclasseswithmembers class ** {
    kotlinx.serialization.KSerializer serializer(...);
}
# Undvik varningar från libbet
-dontwarn kotlinx.serialization.**

# (Säkrare men snäll regel) Behåll dina egna DTOs som används av serialization
-keep class com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.remote.** { *; }

################################################################
# WorkManager – din Worker laddas via klassnamn från persistens
################################################################
# Alternativ A: håll just din worker
-keep class com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.workers.WeatherCheckWorker { *; }
# (eller bredare)
# -keep class * extends androidx.work.ListenableWorker { *; }

################################################################
# Room – brukar inte kräva regler (KSP genererar direkta anrop)
# Lämnas tomt här. Lägg regler först om du ser konkreta varningar.
################################################################

################################################################
# Retrofit/OkHttp – funkar normalt utan extra regler.
# Om du får nya varningar, adressera dem specifikt.
################################################################

# (valfritt) Minska brus från vanliga 3:e-part varningar
# -dontwarn org.conscrypt.**
# -dontwarn okio.**