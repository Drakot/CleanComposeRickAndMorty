package com.aluengo.cleancomposerickandmorty.listCharacters.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InfoLocalModel(

    val count: Int = 0,
    val next: String? = "",
    val pages: Int = 0,
    val prev: String? = "",
    @PrimaryKey
    val id: Int = 1,
)