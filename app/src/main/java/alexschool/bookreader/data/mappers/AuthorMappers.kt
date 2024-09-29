package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.AuthorEntity
import alexschool.bookreader.data.remote.AuthorDto
import alexschool.bookreader.domain.Author

fun AuthorDto.toAuthorEntity(): AuthorEntity {
    return AuthorEntity(
        id = id,
        name = name,
        //description = description
    )
}

fun AuthorEntity.toAuthor(): Author {
    return Author(
        name = name,
       // description = description
    )
}