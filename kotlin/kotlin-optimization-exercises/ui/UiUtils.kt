package com.example.ui

// Bitmaskor för UI-komponenters tillstånd
const val VISIBLE_FLAG = 0x1
const val ENABLED_FLAG = 0x2
const val SELECTED_FLAG = 0x4

// Funktion för att sätta synlighet
fun setVisibility(flags: Int, isVisible: Boolean): Int {
    return if (isVisible) {
        flags or VISIBLE_FLAG
    } else {
        flags and VISIBLE_FLAG.inv()
    }
}

// Funktion för att sätta aktivering
fun setEnabled(flags: Int, isEnabled: Boolean): Int {
    return if (isEnabled) {
        flags or ENABLED_FLAG
    } else {
        flags and ENABLED_FLAG.inv()
    }
}
