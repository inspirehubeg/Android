package alexSchool.network.entities

import alexSchool.network.domain.BookFont
import alexSchool.network.domain.BookIndex
import alexSchool.network.domain.BookTags
import alexSchool.network.domain.Encoding
import alexSchool.network.domain.TargetLink
import alexSchool.network.dtos.ChapterDto
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalDateConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): String? {
        return localDate?.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let {
            LocalDate.parse(
                it,
                formatter
            )
        }
    }
}

object InstantConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilli()
    }
}

object TargetLinkConverter {
    @TypeConverter
    fun fromTargetLink(targetLink: TargetLink): String {
        return Json.encodeToString(targetLink)
    }

    @TypeConverter
    fun toTargetLink(targetLinkString: String): TargetLink {
        return Json.decodeFromString(targetLinkString)
    }
}

class Converters {
    @TypeConverter
    fun fromTargetLink(targetLink: TargetLink): String {
        return Json.encodeToString(targetLink)
    }

    @TypeConverter
    fun toTargetLink(targetLinkString: String): TargetLink {
        return Json.decodeFromString(targetLinkString)
    }

    @TypeConverter
    fun fromTargetLinkList(targetLinkList: List<TargetLink>): String {
        return Json.encodeToString(targetLinkList)
    }

    @TypeConverter
    fun toTargetLinkList(targetLinkListString: String): List<TargetLink> {
        return Json.decodeFromString(targetLinkListString)
    }

    @TypeConverter
    fun fromEncoding(encoding: Encoding): String {
        return Json.encodeToString(encoding)
    }

    @TypeConverter
    fun toEncoding(encodingString: String): Encoding {
        return Json.decodeFromString(encodingString)
    }

    @TypeConverter
    fun fromBookTags(bookTags: BookTags): String {
        return Json.encodeToString(bookTags)
    }

    @TypeConverter
    fun toBookTags(bookTagsString: String): BookTags {
        return Json.decodeFromString(bookTagsString)
    }

    @TypeConverter
    fun fromBookFont(bookFont: BookFont): String {
        return Json.encodeToString(bookFont)
    }

    @TypeConverter
    fun toBookFont(bookFontString: String): BookFont {
        return Json.decodeFromString(bookFontString)
    }

    @TypeConverter
    fun fromBookFontMap(bookFontMap: Map<String, BookFont>): String {
        return Json.encodeToString(bookFontMap)
    }

    @TypeConverter
    fun toBookFontMap(bookFontMapString: String): Map<String, BookFont> {
        return Json.decodeFromString(bookFontMapString)
    }


    @TypeConverter
    fun fromBookIndex(bookIndex: BookIndex): String {
        return Json.encodeToString(bookIndex)
    }

    @TypeConverter
    fun toBookIndex(bookIndexString: String): BookIndex {
        return Json.decodeFromString(bookIndexString)
    }

    @TypeConverter
    fun fromChapterDtoList(chapterDtos: List<ChapterDto>): String {
        return Json.encodeToString(chapterDtos)
    }

    @TypeConverter
    fun toChapterDtoList(chapterDtosString: String): List<ChapterDto> {
        return Json.decodeFromString(chapterDtosString)
    }

    @TypeConverter
    fun fromChapterDto(chapterDto: ChapterDto): String {
        return Json.encodeToString(chapterDto)
    }

    @TypeConverter
    fun toChapterDto(chapterDtoString: String): ChapterDto {
        return Json.decodeFromString(chapterDtoString)
    }

}