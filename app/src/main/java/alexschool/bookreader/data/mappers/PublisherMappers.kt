package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.PublisherEntity
import alexschool.bookreader.data.remote.PublisherDto
import alexschool.bookreader.domain.Publisher


fun PublisherDto.toPublisherEntity(): PublisherEntity {
    return PublisherEntity(
        id = id,
        name = name,
        )
}


fun PublisherEntity.toPublisher(): Publisher {
    return Publisher(
        id = id,
        name = name,
        )

}