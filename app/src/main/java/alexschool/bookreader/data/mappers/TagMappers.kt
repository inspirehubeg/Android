package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.domain.Tag
import alexSchool.network.dtos.TagDto
import alexSchool.network.entities.TagEntity

fun TagDto.toTagEntity(): TagEntity {
    return TagEntity(
        id = id,
        name = name,
        //description = description,
    )
}

fun TagEntity.toTag(): Tag {
    return Tag(
        id = id,
        name = name,
        //description = description,
    )

}