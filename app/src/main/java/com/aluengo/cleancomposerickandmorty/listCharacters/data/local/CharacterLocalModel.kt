package com.aluengo.cleancomposerickandmorty.listCharacters.data.local

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity
@TypeConverters(
    CharacterLocalModel.EpisodeConverter::class,
    CharacterLocalModel.LocationConverter::class,
    CharacterLocalModel.OriginConverter::class
)
data class CharacterLocalModel(
    @PrimaryKey
    val id: Int = 0,
    val name: String = "",
    val gender: String = "",
    val image: String="",
    val created: String = "",
    val species: String = "",
    val status: String = "",
    val type: String = "",
    val url: String = "",
    val episode: List<String>,
    val location: Location = Location(),
    val origin: Origin = Origin()

) {

    @Keep
    data class Location(
        val name: String = "",
        val url: String = ""
    )

    @Keep
    data class Origin(
        val name: String = "",
        val url: String = ""
    )

    class EpisodeConverter {
        @TypeConverter
        fun fromString(value: String): List<String> {
            return value.split(",")
        }

        @TypeConverter
        fun fromList(list: List<String>): String {
            return list.joinToString(",")
        }
    }

    class LocationConverter {
        @TypeConverter
        fun fromString(value: String): Location {
            val parts = value.split(",")
            return Location(parts[0], parts[1])
        }

        @TypeConverter
        fun fromLocation(location: Location): String {
            return "${location.name},${location.url}"
        }
    }

    class OriginConverter {
        @TypeConverter
        fun fromString(value: String): Origin {
            val parts = value.split(",")
            return Origin(parts[0], parts[1])
        }

        @TypeConverter
        fun fromOrigin(origin: Origin): String {
            return "${origin.name},${origin.url}"
        }
    }


}