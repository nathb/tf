package com.nathb.tf.service.episode

import android.support.annotation.WorkerThread
import com.nathb.tf.data.Episode
import com.nathb.tf.data.Show

interface EpisodeFetcher {

    @WorkerThread
    fun getEpisodes(show: Show): List<Episode>
}