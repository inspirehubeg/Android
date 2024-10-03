package alexschool.bookreader.data.mappers

import alexschool.bookreader.data.local.CategoryEntity
import alexschool.bookreader.data.remote.CategoryDto
import alexschool.bookreader.domain.Category

fun CategoryDto.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        name = name,
        id = id,
        image = image
    )
}

fun CategoryEntity.toCategory(): Category {
    return Category(
        name = name,
        id = id,
        image = image
    )
}