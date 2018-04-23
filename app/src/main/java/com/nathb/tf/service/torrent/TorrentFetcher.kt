package com.nathb.tf.service.torrent

import com.nathb.tf.data.Episode
import com.nathb.tf.data.Show
import com.nathb.tf.data.Torrent

interface TorrentFetcher {
    fun getTorrents(show: Show, episode: Episode, limit: Int = 3): List<Torrent>
}