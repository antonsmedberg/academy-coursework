package com.example.timelib

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.round

object TimeUtils {
    private fun hourFormatter(locale: Locale = Locale.getDefault()): DateTimeFormatter {
        val uses12h = locale.country.uppercase() in setOf("US","PH","MY","CA")
        val pattern = if (uses12h) "h a" else "HH:mm"
        return DateTimeFormatter.ofPattern(pattern, locale)
    }

    fun hourLabel(iso: String, locale: Locale = Locale.getDefault()): String = try {
        OffsetDateTime.parse(iso).toLocalTime().format(hourFormatter(locale))
    } catch (_: Exception) { iso }

    fun defaultTempUnit(locale: Locale = Locale.getDefault()): TempUnit =
        if (locale.country.uppercase() in setOf("US","LR","MM")) TempUnit.F else TempUnit.C

    fun temp(valueC: Double, unit: TempUnit, decimals: Int = 0, withUnit: Boolean = true): String {
        val v = if (unit == TempUnit.F) Units.cToF(valueC) else valueC
        val txt = if (decimals == 0) round(v).toInt().toString() else "%.${decimals}f".format(v)
        return if (withUnit) "$txt°${if (unit == TempUnit.F) "F" else "C"}" else "$txt°"
    }

    // Med enhet (visas i dagkorten, tydligare UX)
    fun hiArrow(valueC: Double, unit: TempUnit, decimals: Int = 0) =
        "↑" + temp(valueC, unit, decimals, withUnit = true)

    fun loArrow(valueC: Double, unit: TempUnit, decimals: Int = 0) =
        "↓" + temp(valueC, unit, decimals, withUnit = true)

    // Chip-text (oförändrad)
    fun rainChipText(pct: Int, locale: Locale = Locale.getDefault()): String =
        if (locale.language == "sv") "Regnchans • $pct %" else "Rain chance • $pct%"

    // header som tar unit från SettingsStore
    fun nowHeader(
        nowC: Double,
        unit: TempUnit,
        nextHourRainPct: Int?,
        locale: Locale = Locale.getDefault()
    ): String {
        val now = temp(nowC, unit, decimals = 1, withUnit = true)
        return if (nextHourRainPct != null) {
            if (locale.language == "sv") "Nu: $now • Nästa timme: ${nextHourRainPct}% regn"
            else "Now: $now • Next hour: ${nextHourRainPct}% rain"
        } else {
            if (locale.language == "sv") "Nu: $now" else "Now: $now"
        }
    }
}