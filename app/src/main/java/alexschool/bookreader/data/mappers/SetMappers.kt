package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.SetEntity
import alexschool.bookreader.data.remote.SetDto
import alexschool.bookreader.domain.Set

fun SetDto.toSetEntity(): SetEntity {
    return SetEntity(
        id = id,
        userId = user_id,
        name = name
    )
}

fun SetEntity.toSet(): Set {
    return Set(
        id = id,
        name = name
    )

}