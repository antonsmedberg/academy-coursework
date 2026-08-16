package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.common

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding

/** För app-baren: lägg bara statusbarens höjd överst. */
fun View.applyStatusBarPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
        v.updatePadding(top = bars.top)
        insets
    }
    ViewCompat.requestApplyInsets(this)
}

/** För scroll/listor: lägg bottom-padding så sista raden inte hamnar bakom gest/IME. */
fun View.applyBottomInsets(extraBottomPx: Int = 0) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
        )
        v.updatePadding(bottom = bars.bottom + extraBottomPx)
        insets
    }
    ViewCompat.requestApplyInsets(this)
}

/** För knappar/FAB längst ner: använd margin istället för padding. */
fun View.applyBottomInsetsToMargin(extraBottomPx: Int = 0) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
        )
        (v.layoutParams as? MarginLayoutParams)?.let { lp ->
            v.updateLayoutParams<MarginLayoutParams> {
                bottomMargin = bars.bottom + extraBottomPx
            }
        }
        insets
    }
    ViewCompat.requestApplyInsets(this)
}