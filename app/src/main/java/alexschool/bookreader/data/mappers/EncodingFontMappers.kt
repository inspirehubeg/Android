package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.EncodingFontEntity
import alexschool.bookreader.data.remote.EncodingFontDto
import alexschool.bookreader.domain.EncodingFont

fun EncodingFontDto.EncodingFontEntity(): EncodingFontEntity {
    return EncodingFontEntity(
        bold = bold,
        italic = italic,
        size = size,
        fontColor = fontColor,
        backgroundColor = backgroundColor,
        align = align,
    )
}

fun EncodingFontEntity.toEncodingFont(): EncodingFont {
    return EncodingFont(
        bold = bold,
        italic = italic,
        size = size,
        fontColor = fontColor,
        backgroundColor = backgroundColor,
        align = align,
    )
}