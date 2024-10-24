package alexSchool.network.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translators")
data class TranslatorEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    //val version: Int
)