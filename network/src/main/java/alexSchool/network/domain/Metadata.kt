package alexSchool.network.domain

import alexSchool.network.dtos.ChapterDto

data class Metadata(
    val bookId: Int,
    val encoding: Encoding,
    val targetLinks: List<TargetLink>,
    val index: List<ChapterDto>
)
