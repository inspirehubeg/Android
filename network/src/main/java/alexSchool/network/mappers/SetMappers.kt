package alexSchool.network.mappers

import alexSchool.network.domain.Set
import alexSchool.network.dtos.SetDto
import alexSchool.network.entities.SetEntity

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