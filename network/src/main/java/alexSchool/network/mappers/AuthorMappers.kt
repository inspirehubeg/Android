package alexSchool.network.mappers

import alexSchool.network.domain.Author
import alexSchool.network.dtos.AuthorDto
import alexSchool.network.entities.AuthorEntity

fun AuthorDto.toAuthorEntity(): AuthorEntity {
    return AuthorEntity(
        id = id,
        name = name,
        bio = bio,
        image = image,
        //description = description
    )
}

fun AuthorEntity.toAuthor(): Author {
    return Author(
        name = name,
        id = id,
        bio = bio,
        image = image
        // description = description
    )
}