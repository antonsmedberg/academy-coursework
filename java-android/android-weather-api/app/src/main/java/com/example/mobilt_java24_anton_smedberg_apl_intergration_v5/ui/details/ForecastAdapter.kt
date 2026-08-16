// ui/details/ForecastAdapter.kt
package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.databinding.ItemDaySummaryBinding
import com.example.timelib.Thresholds
import com.example.timelib.TimeUtils
import java.util.Locale

data class DayRow(
    val epochDay: Long,
    val dayLabel: String,     // “Idag”, “Fredag 12 sep”
    val emoji: String,        // väder-emoji
    val condition: String,    // “Lätt regn”
    val highTempLabel: String,// “↑19°”
    val lowTempLabel: String, // “↓15°”
    val rainChancePct: Int    // 0..100
)

private object DayDiff : DiffUtil.ItemCallback<DayRow>() {
    override fun areItemsTheSame(old: DayRow, new: DayRow) = old.epochDay == new.epochDay
    override fun areContentsTheSame(old: DayRow, new: DayRow) = old == new
}

class ForecastAdapter : ListAdapter<DayRow, ForecastAdapter.VH>(DayDiff) {

    init {
        setHasStableIds(true)
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun getItemId(position: Int): Long = getItem(position).epochDay

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemDaySummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    class VH(private val b: ItemDaySummaryBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(row: DayRow) {
            val loc = Locale.getDefault()

            // Vänster + mitten
            b.txtIcon.text = row.emoji
            b.txtDay.text = row.dayLabel
            b.txtCond.text = row.condition

            // Temperaturer (UI-komponenterna heter redan txtTempHi/Lo)
            b.txtTempHi.text = row.highTempLabel
            b.txtTempLo.text = row.lowTempLabel

            // Regn-chip (text + markera “hög risk”)
            b.chipRain.text = TimeUtils.rainChipText(row.rainChancePct, loc)
            b.chipRain.isCheckable = true
            b.chipRain.isChecked = row.rainChancePct >= Thresholds.RAIN_PROB_THRESHOLD

            // A11y
            val rainDesc = if (loc.language == "sv") "regnchans ${row.rainChancePct} procent"
            else "rain chance ${row.rainChancePct} percent"
            b.chipRain.contentDescription = rainDesc
            b.root.contentDescription =
                "${row.dayLabel}, ${row.condition}, ${row.highTempLabel} / ${row.lowTempLabel}, $rainDesc"
        }
    }
}