package alexSchool.network.dtos

import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val book: BookInfoDto,
    val categories: List<CategoryDto>,
    val tags: List<TagDto>,
    val author: List<AuthorDto>,
    val translators: List<TranslatorDto>,
    val publisher: List<PublisherDto>,
    )


