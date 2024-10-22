package alexSchool.network.mappers

import alexSchool.network.domain.Tag
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