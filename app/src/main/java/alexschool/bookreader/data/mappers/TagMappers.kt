package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.TagEntity
import alexschool.bookreader.data.remote.TagDto
import alexschool.bookreader.domain.Tag

fun TagDto.toTagEntity(): TagEntity {
    return TagEntity(
        id = id,
        name = name,
        description = description,
    )
}

fun TagEntity.toTag(): Tag {
    return Tag(
        id = id,
        name = name,
        description = description,
    )

}