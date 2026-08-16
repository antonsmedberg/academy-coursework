import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.mobilt_java24_anton_smedberg_apl_intergration_v5"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mobilt_java24_anton_smedberg_apl_intergration_v5"
        minSdk = 30
        targetSdk = 36
        versionCode = 2
        versionName = "1.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
        // buildConfig lämnas som default (true) – praktiskt om du vill lägga in konstanter senare
    }

    // Java 17 (AGP)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Bättre JVM-teststöd (Robolectric m.m.)
    testOptions {
        unitTests.isIncludeAndroidResources = true
        // unitTests.isReturnDefaultValues = true // valfritt; kan hjälpa vissa JVM-tester
    }
}

// Nytt sätt för Kotlin-kompilatorn (ersätter kotlinOptions { jvmTarget = "17" })
kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        // freeCompilerArgs.add("-Xjsr305=strict") // valfritt
    }
}

// Room KSP-argument: krävs när du har exportSchema = true i din Room-DB
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(project(":timelib"))

    // Android Core / Splash
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.recyclerview)

    // KotlinX
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // Network
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization.converter)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)

    // WorkManager
    implementation(libs.androidx.work)

    // Test
    testImplementation(libs.junit)
    testImplementation(kotlin("test"))

    // Coroutines test (runTest)
    testImplementation(libs.kotlinx.coroutines.test)

    // MockK (mocks/fakes)
    testImplementation(libs.mockk)

    // Robolectric + core-ktx (Context i JVM-test)
    testImplementation(libs.robolectric)
    testImplementation(libs.core.ktx)

    // Android instrumented tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}