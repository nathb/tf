package com.nathb.tf.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(Episode::class, Show::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun episodeDao() : EpisodeDao
    abstract fun showDao() : ShowDao
    abstract fun episodeWithShowDao() : EpisodeWithShowDao
    abstract fun showWithEpisodesDao() : ShowWithEpisodesDao

    companion object {

        private lateinit var instance : AppDatabase

        fun init(context : Context) {
            instance = Room
                        .databaseBuilder(context, AppDatabase::class.java, "database")
                        .addCallback(callback)
                        .build()
        }

        fun get(): AppDatabase {
            return instance
        }

        private val callback = object: Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                Thread({
                    insertShows()
                }).start()
            }
        }


        private fun insertShows() {
            instance.showDao().insert(
                    Show("Mr Robot", "ts" , "es"),
                    Show("Modern Family", "ts" , "es"))
        }
    }
}