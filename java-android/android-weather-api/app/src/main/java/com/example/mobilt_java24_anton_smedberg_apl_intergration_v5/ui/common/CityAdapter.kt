package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.R
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.databinding.ItemCityBinding
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model.City

/**
 * Stadlista:
 *  - Tryck på kort → öppna detaljer
 *  - Tryck på bokmärke → spara/ta bort favorit
 * Ikonen styrs av [savedKeys].
 */
class CityAdapter(
    private val onClick: (City) -> Unit,
    private val onSave: (City) -> Unit
) : ListAdapter<City, CityAdapter.VH>(DIFF) {

    /** Favoriter som "name|lat|lon" för att måla bokmärket rätt. */
    var savedKeys: Set<String> = emptySet()
        set(value) {
            field = value
            // Enkelt och säkert; vill du mikro-optimeras kan du uppdatera endast synliga rader.
            notifyDataSetChanged()
        }

    companion object {
        private const val PAYLOAD_SUB = "payload_sub"

        private val DIFF = object : DiffUtil.ItemCallback<City>() {
            override fun areItemsTheSame(old: City, new: City): Boolean {
                return if (old.id != 0L && new.id != 0L) {
                    old.id == new.id
                } else {
                    old.name == new.name &&
                            old.latitude == new.latitude &&
                            old.longitude == new.longitude
                }
            }

            override fun areContentsTheSame(old: City, new: City) = old == new

            override fun getChangePayload(oldItem: City, newItem: City): Any? {
                val bundle = Bundle()
                if (oldItem.admin1 != newItem.admin1 || oldItem.country != newItem.country) {
                    bundle.putBoolean(PAYLOAD_SUB, true)
                }
                return if (bundle.isEmpty) null else bundle
            }
        }
    }

    private fun City.stableKey(): String = "$name|$latitude|$longitude"

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        val item = currentList.getOrNull(position) ?: return RecyclerView.NO_ID
        return if (item.id != 0L) item.id else syntheticId(item.name, item.latitude, item.longitude)
    }

    private fun syntheticId(name: String, lat: Double, lon: Double): Long {
        return (name.hashCode().toLong() shl 32) xor
                (java.lang.Double.doubleToRawLongBits(lat) xor java.lang.Double.doubleToRawLongBits(lon))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding, onClick, onSave, ::isSaved)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return onBindViewHolder(holder, position)
        holder.updatePartial(getItem(position), payloads.firstOrNull() as? Bundle)
    }

    private fun isSaved(city: City): Boolean = city.stableKey() in savedKeys

    class VH(
        private val binding: ItemCityBinding,
        private val onClick: (City) -> Unit,
        private val onSave: (City) -> Unit,
        private val isSaved: (City) -> Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        private var lastClickAt = 0L

        fun bind(item: City) {
            binding.txtName.text = item.name
            binding.txtSub.text = listOfNotNull(item.admin1, item.country).joinToString(", ")

            // A11y: kortets sammanfattning
            val ctx = binding.root.context
            binding.root.contentDescription = ctx.getString(
                R.string.city_item_a11y,
                item.name,
                binding.txtSub.text
            )

            binding.root.setOnClickListener {
                val now = System.currentTimeMillis()
                if (now - lastClickAt < 300) return@setOnClickListener
                lastClickAt = now
                onClick(item)
            }

            binding.btnSave.setOnClickListener { onSave(item) }

            // Bokmärkes-ikon + a11y
            val saved = isSaved(item)
            binding.btnSave.setImageResource(
                if (saved) R.drawable.ic_bookmark_24 else R.drawable.ic_bookmark_border_24
            )
            binding.btnSave.contentDescription = if (saved) {
                ctx.getString(R.string.saved)
            } else {
                ctx.getString(R.string.save)
            }
        }

        fun updatePartial(item: City, payload: Bundle?) {
            if (payload?.getBoolean(PAYLOAD_SUB) == true) {
                binding.txtSub.text = listOfNotNull(item.admin1, item.country).joinToString(", ")
            } else {
                bind(item)
            }
        }
    }
}