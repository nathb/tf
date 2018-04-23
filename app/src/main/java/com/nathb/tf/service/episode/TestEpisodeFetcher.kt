package com.nathb.tf.service.episode

import android.support.annotation.WorkerThread
import com.nathb.tf.data.Episode
import com.nathb.tf.data.Show

class TestEpisodeFetcher : EpisodeFetcher {

    @WorkerThread
    override fun getEpisodes(show: Show): List<Episode> {
        val episodeList = ArrayList<Episode>()
        (1..3).forEach { season ->
            (1..5).forEach { episode ->
                episodeList.add(Episode(show.id, season, episode, "EP${season}x$episode!", false))
            }
        }
        return episodeList
    }
}
