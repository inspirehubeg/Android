package ih.tools.readingpad.feature_highlight.domain.use_cases

import android.util.Log
import ih.tools.readingpad.feature_highlight.domain.repository.HighlightRepository

class RemoveHighlightById(
    private val repository: HighlightRepository
) {
    suspend operator fun invoke(highlightId: Long) {
        Log.d("removeHighlightUseCase", "removeHighlightUseCase is invoked")

        repository.deleteHighlightById(highlightId)
    }
}