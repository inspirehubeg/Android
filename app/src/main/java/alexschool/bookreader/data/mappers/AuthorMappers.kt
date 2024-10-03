package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.AuthorEntity
import alexschool.bookreader.data.remote.AuthorDto
import alexschool.bookreader.domain.Author

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