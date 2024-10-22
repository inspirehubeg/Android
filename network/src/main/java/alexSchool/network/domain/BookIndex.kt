package alexSchool.network.domain

import alexSchool.network.dtos.ChapterDto
import kotlinx.serialization.Serializable

@Serializable
data class BookIndex(
    val chapterDtos : List<ChapterDto>,
){

}
