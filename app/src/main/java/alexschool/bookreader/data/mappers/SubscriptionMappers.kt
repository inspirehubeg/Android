package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.SubscriptionEntity
import alexschool.bookreader.data.remote.SubscriptionDto
import alexschool.bookreader.domain.Subscription

fun SubscriptionDto.toSubscriptionEntity(): SubscriptionEntity{
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