package ih.tools.readingpad.util

import android.graphics.Color
import android.text.style.BackgroundColorSpan

/**
 * Custom span for highlighting text with a background color and an associated ID.
 * Implements [IHSpan] interface to manage theID.
 *
 * @property id The unique identifier for the span.
 */
class IHBackgroundSpan(color: Int) : BackgroundColorSpan(color), IHSpan {
    override var id: Long = 0

    /**
     * Constructor for creating a span with a specific ID.
     * The color is set to yellow by default. Consider allowing color customization in this constructor as well.
     *
     * @param id The unique identifier for the span.
     */
    constructor(id: Long) : this(Color.YELLOW) {
        this.id = id
    }
}

/**
 * Interface for spans that require a unique identifier (ID).
 */
interface IHSpan {
    /**
     * The unique identifier for the span.
     */
    var id: Long
}



