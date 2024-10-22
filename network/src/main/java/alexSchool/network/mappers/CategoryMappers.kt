package alexSchool.network.mappers

import alexSchool.network.domain.Category
import alexSchool.network.dtos.CategoryDto
import alexSchool.network.entities.CategoryEntity

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