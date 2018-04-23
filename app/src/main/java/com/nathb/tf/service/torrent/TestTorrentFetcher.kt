package com.nathb.tf.service.torrent

import com.nathb.tf.data.Episode
import com.nathb.tf.data.Show
import com.nathb.tf.data.Torrent
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

class TestTorrentFetcher: TorrentFetcher {

    override fun getTorrents(show: Show, episode: Episode, limit: Int): List<Torrent> {
        val sleep = ThreadLocalRandom.current().nextLong(10000)
        Thread.sleep(sleep)
        val name = "${show.showTitle} S${episode.seasonNumber}E${episode.episodeNumber}"
        val torrentList = ArrayList<Torrent>()

        (1 .. Random().nextInt(limit - 2) + 2).forEach { i ->
            torrentList.add(Torrent(
                    "$name [$i]",
                    "YDay 21:11",
                    (Random().nextInt(150) + 100).toString(),
                    "mg://123",
                    Random().nextInt(1000),
                    Random().nextInt(1000)))
        }

        return torrentList
    }
}