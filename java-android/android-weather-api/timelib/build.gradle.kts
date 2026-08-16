// timelib/build.gradle.kts
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.timelib"
    compileSdk = 36

    defaultConfig {
        minSdk = 30
        // Dessa regler följer med till appens R8
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            // Viktigt: INTE minifiera biblioteket
            isMinifyEnabled = false
        }
        debug { isMinifyEnabled = false }
    }

    buildFeatures {
        buildConfig = false
        viewBinding = false
    }

    // Fortsätt låsa Java 17 här
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    jvmToolchain(17)

    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)

    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}