// data/Repository.kt
package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data

import android.util.Log
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.local.AppDatabase
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.local.CityEntity
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.local.RecentCityEntity
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.local.WeatherCacheEntity
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.remote.GeoApi
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.remote.OpenMeteoApi
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model.City
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model.DaySummary
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model.HourPoint
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model.WeatherSnapshot
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.text.Collator
import java.text.Normalizer
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

private const val TAG = "Repository"

class Repository(
    // Behåll parameternamnen (de används med named args i ServiceLocator)
    private val geo: GeoApi,
    private val meteo: OpenMeteoApi,
    private val db: AppDatabase
) {
    // ---- Mapping helpers ----
    private fun CityEntity.toDomain() =
        City(id, name, country, admin1, latitude, longitude)

    private fun City.toEntity() =
        CityEntity(id, name, country, admin1, latitude, longitude)

    private fun City.toRecentEntity(now: Long) =
        RecentCityEntity(name, country, admin1, latitude, longitude, now)

    // ---- Sök & merge ----
    /** Normalisera så IME inte skickar å/ä/ö i NFD. */
    private fun normalizeNfc(s: String): String =
        Normalizer.normalize(s, Normalizer.Form.NFC)

    /** Merge där svenska fält får skriva över när koordinaterna är samma. */
    private fun mergeCityFields(base: City?, override: City?): City? {
        if (base == null) return override
        if (override == null) return base
        return City(
            id = base.id,
            name = override.name.ifBlank { base.name },
            country = override.country ?: base.country,
            admin1 = override.admin1 ?: base.admin1,
            latitude = base.latitude,
            longitude = base.longitude
        )
    }

    private suspend fun geocode(query: String, language: String): List<City> {
        val res = geo.search(name = query, language = language).results
        return res.asSequence()
            .filter { it.name.isNotBlank() }
            .map {
                City(
                    name = it.name.trim(),
                    country = it.country?.ifBlank { null },
                    admin1 = it.admin1?.ifBlank { null },
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            }.toList()
    }

    /** Sök parallellt på EN+SV och slå ihop per koordinat (SV vinner text). */
    suspend fun searchCities(query: String): List<City> = withContext(Dispatchers.IO) {
        val q = normalizeNfc(query).trim()
        if (q.length < 2) return@withContext emptyList()

        try {
            supervisorScope {
                val enDef = async {
                    runCatching { geocode(q, "en") }
                        .onFailure { Log.w(TAG, "geocode EN: ${it.javaClass.simpleName}") }
                        .getOrElse { emptyList() }
                }
                val svDef = async {
                    runCatching { geocode(q, "sv") }
                        .onFailure { Log.w(TAG, "geocode SV: ${it.javaClass.simpleName}") }
                        .getOrElse { emptyList() }
                }

                val en = enDef.await()
                val sv = svDef.await()

                val byCoord = LinkedHashMap<Pair<Double, Double>, City>()
                en.forEach { c -> byCoord[c.latitude to c.longitude] = c }
                sv.forEach { c ->
                    byCoord[c.latitude to c.longitude] =
                        mergeCityFields(byCoord[c.latitude to c.longitude], c)!!
                }

                val collator = Collator.getInstance(Locale.forLanguageTag("sv-SE")).apply {
                    strength = Collator.PRIMARY
                }

                byCoord.values
                    .distinctBy { it.latitude to it.longitude }
                    .sortedWith(compareBy(collator) { it.name })
                    .also { Log.d(TAG, "searchCities('$q'): merged=${it.size}") }
            }
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            Log.w(TAG, "searchCities('$q') error: ${t.javaClass.simpleName}")
            emptyList()
        }
    }

    // ---- Favoriter ----
    fun observeSaved(): Flow<List<City>> =
        db.cityDao().observeList().map { list -> list.map { it.toDomain() } }.distinctUntilChanged()

    suspend fun saveCity(city: City): Long = withContext(Dispatchers.IO) {
        val id = db.cityDao().insertIgnore(city.toEntity())
        if (id != -1L) id
        else db.cityDao().getIdByKey(city.name, city.latitude, city.longitude) ?: 0L
    }

    suspend fun deleteCity(city: City) = withContext(Dispatchers.IO) {
        val rows = db.cityDao().deleteByKey(city.name, city.latitude, city.longitude)
        if (rows == 0 && city.id != 0L) {
            db.cityDao().delete(
                CityEntity(city.id, city.name, city.country, city.admin1, city.latitude, city.longitude)
            )
        }
    }

    suspend fun listSaved(): List<City> = withContext(Dispatchers.IO) {
        db.cityDao().list().map { it.toDomain() }
    }

    // ---- Prognos ----
    suspend fun getForecastFor(city: City): WeatherSnapshot = withContext(Dispatchers.IO) {
        val dto = try {
            meteo.forecast(city.latitude, city.longitude)
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            if (t is HttpException) {
                val body = t.response()?.errorBody()?.string()
                Log.e(TAG, "Forecast HTTP ${t.code()} body=$body", t)
            } else {
                Log.e(TAG, "Forecast error(${city.name}): ${t.javaClass.simpleName}", t)
            }
            throw t
        }

        runCatching { db.recentDao().upsert(city.toRecentEntity(System.currentTimeMillis())) }

        val currentTemp = dto.current?.temperature2m ?: 0.0

        // --- Timserie (för “nästa timme” + upp till 24 punkter framåt) ---
        val hourly = dto.hourly
        val count = min(24, hourly?.time?.size ?: 0)
        val hours: List<HourPoint> =
            if (hourly != null && count > 0) {
                List(count) { i ->
                    HourPoint(
                        timeIso = hourly.time[i],
                        temperature = hourly.temperature2m.getOrNull(i) ?: currentTemp,
                        precipitationProb = pct(hourly.precipitationProbability.getOrNull(i) ?: 0)
                    )
                }
            } else emptyList()

        // Välj *första timmen efter nu* i lokal zon (timezone=auto)
        val nextHourProb = run {
            if (hourly == null || hourly.time.isEmpty()) 0 else {
                val zone = ZoneId.systemDefault()
                val nowRounded = ZonedDateTime.now(zone).withMinute(0).withSecond(0).withNano(0)
                val idx = hourly.time.indexOfFirst { ts ->
                    parseToLocalZoned(ts, zone)?.isAfter(nowRounded) == true
                }
                val chosen = if (idx >= 0) idx else 0
                pct(hourly.precipitationProbability.getOrNull(chosen) ?: 0).also {
                    Log.d(TAG, "nextHour idx=$chosen ts=${hourly.time.getOrNull(chosen)} prob=$it")
                }
            }
        }

        // --- Dagar (5-dagars vy) ---
        val daily = dto.daily
        val days: List<DaySummary> =
            if (daily != null && daily.time.isNotEmpty()) {
                val n = minOf(5, daily.time.size)
                List(n) { i ->
                    DaySummary(
                        dateIso = daily.time[i],
                        tMin = daily.tMin.getOrNull(i) ?: 0.0,
                        tMax = daily.tMax.getOrNull(i) ?: 0.0,
                        rainProbMax = pct(daily.rainProbMax.getOrNull(i) ?: 0),
                        weatherCode = daily.weathercode.getOrNull(i) ?: 0
                    )
                }
            } else emptyList()

        // Liten cache för favoritstad
        if (city.id != 0L) {
            runCatching {
                db.weatherDao().upsert(
                    WeatherCacheEntity(
                        cityId = city.id,
                        timestamp = System.currentTimeMillis(),
                        currentTemp = currentTemp,
                        rainProbNextHour = nextHourProb
                    )
                )
            }
        }

        WeatherSnapshot(
            currentTemp = currentTemp,
            nextHourRainProb = nextHourProb,
            hours = hours,
            days = days
        )
    }

    // ---- Tids-/parsningshjälp ----
    /** Parsar “yyyy-MM-dd'T'HH:mm” (lokal tid) eller ISO med offset till lokal zon. */
    private fun parseToLocalZoned(ts: String, zone: ZoneId): ZonedDateTime? {
        return try {
            val ldt = LocalDateTime.parse(ts, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            ldt.atZone(zone)
        } catch (_: DateTimeParseException) {
            try {
                val odt = OffsetDateTime.parse(ts, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                odt.atZoneSameInstant(zone)
            } catch (_: Exception) {
                Log.w(TAG, "Kunde inte parsa timestamp: '$ts'")
                null
            }
        }
    }

    private fun pct(value: Int): Int = max(0, min(100, value))
}