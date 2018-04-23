package com.nathb.tf

import android.app.Application
import android.content.Context
import com.nathb.tf.data.AppDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        AppDatabase.init(this)
    }

    companion object {
        private lateinit var context : Context

        fun get() : Context {
            return context
        }
    }
}