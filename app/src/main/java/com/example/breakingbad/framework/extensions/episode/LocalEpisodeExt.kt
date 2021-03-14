package com.example.breakingbad.framework.extensions.episode

import com.example.breakingbad.domain.Episode
import com.example.breakingbad.domain.Quote
import com.example.breakingbad.framework.datasource.database.LocalEpisode
import com.example.breakingbad.framework.datasource.database.LocalQuote

fun List<LocalEpisode?>?.toItems(): List<Episode> {
    val items = mutableListOf<Episode>()
    if (this == null) return items.toList()
    val filteredList = this.filterNotNull()
    items.addAll(filteredList.map {
        it.toItem()
    })
    return items.toList()
}

fun LocalEpisode.toItem(): Episode {
    return Episode(
        this.episode_id,
        this.title,
        this.season,
        this.air_date,
        null, //this.characters,
        this.episode,
        this.series
    )
}

fun List<Episode?>?.toLocalItems(): List<LocalEpisode> {
    val localItems = mutableListOf<LocalEpisode>()
    if (this == null) return localItems.toList()
    val filteredList = this.filterNotNull()
    localItems.addAll(filteredList.map {
        it.toLocalItem()
    })
    return localItems.toList()
}

fun Episode.toLocalItem(): LocalEpisode {
    return LocalEpisode(
        this.episode_id,
        this.title,
        this.season,
        this.air_date,
        // this.characters,
        this.episode,
        this.series
    )
}
