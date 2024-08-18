package ih.tools.readingpad.util

import android.graphics.Color
import android.text.style.BackgroundColorSpan

class IHBackgroundSpan(color: Int) : BackgroundColorSpan(color), IHSpan {
    override var id: Long = 0

    constructor(id: Long) : this(Color.YELLOW) {
        this.id = id
    }
}

interface IHSpan {
    var id: Long
}



