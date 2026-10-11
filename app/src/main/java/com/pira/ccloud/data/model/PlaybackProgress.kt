package com.pira.ccloud.data.model

import kotlinx.serialization.Serializable

/**
 * Saved playback state for a movie or a specific series episode.
 *
 * mediaKey must be stable across launches (for example, "movie:<id>" or
 * "episode:<seriesId>:<seasonId>:<episodeId>").
 */
@Serializable
data class PlaybackProgress(
    val mediaKey: String,
    val mediaUrl: String,
    val title: String,
    val posterUrl: String? = null,
    val mediaType: String = "movie",
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val seriesId: Int? = null,
    val seasonId: Int? = null,
    val episodeId: Int? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
