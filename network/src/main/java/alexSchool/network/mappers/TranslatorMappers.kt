package alexSchool.network.mappers

import alexSchool.network.domain.Translator
import alexSchool.network.dtos.TranslatorDto
import alexSchool.network.entities.TranslatorEntity

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