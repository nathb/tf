package com.nathb.tf.service.episode

import com.nathb.tf.data.Episode
import com.nathb.tf.data.Show
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class TVMazeEpisodeFetcher : EpisodeFetcher {

    override fun getEpisodes(show: Show): List<Episode> {
        val response = service.getEpisodes(show.tvMazeId).execute()
        val episodes = response.body()?.map {
            Episode(show.id, it.season, it.number, it.name, isDownloaded = false)
        }
        return episodes?: emptyList()
    }

    private companion object {

        interface TVMazeService {
            @GET("shows/{id}/episodes")
            fun getEpisodes(@Path("id") tvMazeId: String): Call<List<EpisodeResponse>>
        }

        data class EpisodeResponse(
                val season: Int,
                val number: Int,
                val name: String)

        val service : TVMazeService = Retrofit.Builder()
                .baseUrl("http://api.tvmaze.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TVMazeService::class.java)
    }
}