package ih.tools.readingpad.feature_highlight.domain.use_cases

import android.util.Log

import ih.tools.readingpad.feature_highlight.domain.model.Highlight
import ih.tools.readingpad.feature_highlight.domain.repository.HighlightRepository


class AddHighlight(
    private val repository: HighlightRepository
) {
    suspend operator fun invoke(highlight: Highlight): Long {
        Log.d("addHighlightUseCase", "addHighlightUseCase is invoked")

        val id = repository.insertHighlight(highlight)
        return id
    }
}
