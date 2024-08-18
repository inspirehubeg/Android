package ih.tools.readingpad.feature_bookmark.presentation

import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

class IHBookmarkImageSpan (image: Drawable, bookmarkId: Long ) : ImageSpan(image) {
    constructor(image: Drawable, verticalAlignment: Int, bookmarkId: Long) : this(image, bookmarkId) {
    }

    }

