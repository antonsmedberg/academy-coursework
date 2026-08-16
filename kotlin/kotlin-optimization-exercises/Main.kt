import com.example.api.fetchApiData
import com.example.async.performAsyncOperations
import com.example.async.areAllOperationsComplete
import com.example.cache.adjustCacheSize
import com.example.ui.setVisibility
import com.example.ui.setEnabled
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    // Testa API-anrop
    val url = "https://api.example.com/data"
    val result = fetchApiData(url)
    println(result)

    // Testa asynkrona operationer
    val flags = performAsyncOperations()
    if (areAllOperationsComplete(flags)) {
        println("All operations are complete")
    } else {
        println("Some operations are still pending")
    }

    // Testa cachejustering
    val originalSize = 500
    val adjustedSize = adjustCacheSize(originalSize)
    println("Adjusted cache size: $adjustedSize")

    // Testa UI-hantering
    var uiFlags = 0
    uiFlags = setVisibility(uiFlags, true)
    uiFlags = setEnabled(uiFlags, true)
    println("UI component is visible: ${(uiFlags and VISIBLE_FLAG) != 0}")
    println("UI component is enabled: ${(uiFlags and ENABLED_FLAG) != 0}")
    uiFlags = setVisibility(uiFlags, false)
    uiFlags = setEnabled(uiFlags, false)
    println("UI component is visible: ${(uiFlags and VISIBLE_FLAG) != 0}")
    println("UI component is enabled: ${(uiFlags and ENABLED_FLAG) != 0}")
}
