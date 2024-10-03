package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.TranslatorEntity
import alexschool.bookreader.data.remote.TranslatorDto
import alexschool.bookreader.domain.Translator

fun TranslatorDto.toTranslatorEntity(): TranslatorEntity {
    return TranslatorEntity(
        id = id,
        name = name,
        )
}

fun TranslatorEntity.toTranslator(): Translator {
    return Translator(
        id = id,
        name = name,
        )

}