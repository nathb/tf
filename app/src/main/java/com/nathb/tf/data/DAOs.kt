package com.nathb.tf.data

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single

interface BaseDao<in T> {

    @Insert
    fun insert(vararg t: T)

    @Update
    fun update(vararg t: T)

    @Delete
    fun delete(vararg t: T)
}

@Dao
interface EpisodeDao : BaseDao<Episode> {
    @Query("select * from Episode")
    fun findAll(): Single<List<Episode>>

    @Query("select * from Episode where showId = :showId")
    fun findEpisodesByShow(showId: Int): List<Episode>
}

@Dao
interface ShowDao : BaseDao<Show> {
    @Query("select * from Show")
    fun findAll(): Single<List<Show>>

    @Query("select * from Show where id = :id")
    fun findById(id: Int): Show

    @Query("select * from Show limit 1")
    fun findFirst(): Show
}

@Dao
interface EpisodeWithShowDao {
    @Query("select * from Episode e join Show s on s.id = e.showId")
    fun findAll(): Single<List<EpisodeWithShow>>

    @Query("select * from Episode e join Show s on s.id = e.showId where e.isDownloaded = 0")
    fun findAllNonDownloadedEpisodes(): Single<List<EpisodeWithShow>>
}

@Dao
interface ShowWithEpisodesDao {
    @Transaction
    @Query("select * from Show")
    fun findAllAsSingle(): Single<List<ShowWithEpisodes>>

    @Transaction
    @Query("select * from Show s left join Episode e on s.id = e.showId")
    fun findAllAsFlowable(): Flowable<List<ShowWithEpisodes>>
}