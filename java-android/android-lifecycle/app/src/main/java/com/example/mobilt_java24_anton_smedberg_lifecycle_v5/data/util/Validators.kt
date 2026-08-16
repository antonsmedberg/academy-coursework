package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.util

object Validators {
    private val emailRegex = Regex(
        pattern = """^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"""
    )

    // Svenskt mobilnr: +46 eller 0-prefix, 7–11 siffror efter prefix.
    private val phoneRegex = Regex(
        pattern = """^(?:\+46|0)[0-9]{7,11}$"""
    )

    // Personnummer: YYMMDD-XXXX eller YYYYMMDD-XXXX (bindestreck mellanslag valfritt)
    private val pnrRegex = Regex(
        pattern = """^(?:\d{6}|\d{8})[- ]?\d{4}$"""
    )

    fun email(v: String) = v.isNotBlank() && emailRegex.matches(v)
    fun phone(v: String) = v.isNotBlank() && phoneRegex.matches(v)
    fun personnummer(v: String) = v.isNotBlank() && pnrRegex.matches(v)
    fun age(v: String) = v.toIntOrNull()?.let { it in 13..120 } ?: false
    fun username(v: String) = v.length in 3..30
    fun password(v: String) = v.length >= 6
}