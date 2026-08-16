// util/ServiceLocator.kt
package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util

import android.app.Application
import android.content.Context
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.Repository
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.local.AppDatabase
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.remote.GeoApi
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.remote.OpenMeteoApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Enkel handbyggd DI-container.
 * Anropa [init] i Application.onCreate(), hämta via [repository] och [settingsStore].
 */
object ServiceLocator {

    @Volatile private var repositoryRef: Repository? = null
    @Volatile private var settingsRef: SettingsStore? = null

    fun init(context: Context) {
        if (repositoryRef != null && settingsRef != null) return
        synchronized(this) {
            if (repositoryRef != null && settingsRef != null) return

            val app = context.applicationContext as Application
            val db = AppDatabase.create(app)

            val json = Json { ignoreUnknownKeys = true; isLenient = true }
            val converter = json.asConverterFactory("application/json".toMediaType())
            val httpClient = buildHttpClient()

            val geoRetrofit = Retrofit.Builder()
                .baseUrl("https://geocoding-api.open-meteo.com/")
                .client(httpClient)
                .addConverterFactory(converter)
                .build()

            val meteoRetrofit = Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                .client(httpClient)
                .addConverterFactory(converter)
                .build()

            settingsRef = SettingsStore(app)
            repositoryRef = Repository(
                geo = geoRetrofit.create<GeoApi>(),
                meteo = meteoRetrofit.create<OpenMeteoApi>(),
                db = db
            )
        }
    }

    fun repository(): Repository =
        repositoryRef ?: error("ServiceLocator.init() har inte körts. Sätt android:name=\".App\" i manifestet och kalla init().")

    fun settingsStore(): SettingsStore =
        settingsRef ?: error("ServiceLocator.init() har inte körts. Sätt android:name=\".App\" i manifestet och kalla init().")

    // --- Interna hjälpare ---

    private fun buildHttpClient(): OkHttpClient {
        val userAgent = Interceptor { chain ->
            val req = chain.request().newBuilder()
                .header("User-Agent", "MobiltJava24-Weather/1.0 (okhttp)")
                .build()
            chain.proceed(req)
        }

        val acceptLanguage = Interceptor { chain ->
            val req = chain.request().newBuilder()
                .header("Accept-Language", Locale.getDefault().toLanguageTag())
                .build()
            chain.proceed(req)
        }

        val logging = HttpLoggingInterceptor().apply {
            // HEADERS ger bra insyn utan att bada i body-data
            level = HttpLoggingInterceptor.Level.HEADERS
        }

        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(userAgent)
            .addInterceptor(acceptLanguage)
            .addInterceptor(logging)
            .build()
    }
}