package alexSchool.network.mappers

import alexSchool.network.domain.Subscription
import alexSchool.network.dtos.SubscriptionDto
import alexSchool.network.entities.SubscriptionEntity

fun SubscriptionDto.toSubscriptionEntity(): SubscriptionEntity {
    return SubscriptionEntity(
        id = id,
        name = name,
        image = image,
        isDeleted = is_deleted
    )
}

fun SubscriptionEntity.toSubscription(): Subscription {
    return Subscription(
        id = id,
        name = name,
        image = image
    )
}