package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.domain.Publisher
import alexSchool.network.dtos.PublisherDto
import alexSchool.network.entities.PublisherEntity


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