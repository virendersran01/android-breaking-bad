package com.example.breakingbad.data

import com.example.breakingbad.domain.BBCharacter

interface BBCharacterBaseLocalDataSource {

    suspend fun insert(items: List<BBCharacter>)

    suspend fun getItem(itemId: Int): BBCharacter?

    suspend fun getAll(): List<BBCharacter>
}