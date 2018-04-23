package com.nathb.tf.data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Relation

@Entity
data class Show(
    var showTitle: String,
    var torrentSearchTerm: String,
    var episodeSearchTerm: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Entity(primaryKeys = arrayOf("showId", "seasonNumber", "episodeNumber"))
data class Episode(
    var showId: Int,
    var seasonNumber: Int,
    var episodeNumber: Int,
    var episodeTitle: String,
    var isDownloaded: Boolean)

// Room requires this relation pojo to have a default constructor
class EpisodeWithShow() {
    constructor(episode: Episode, show: Show) : this() {
        this.episode = episode
        this.show = show
    }

    @Embedded
    lateinit var episode: Episode

    @Embedded
    lateinit var show: Show
}

// Room requires this relation pojo to have a default constructor
class ShowWithEpisodes {
    @Embedded
    lateinit var show: Show

    @Relation(parentColumn = "id", entityColumn = "showId")
    lateinit var episodes: List<Episode>
}

data class Torrent(
    val title: String,
    val uploadedDate: String,
    val size: String,
    val link: String,
    val seeders: Int,
    val leechers: Int)