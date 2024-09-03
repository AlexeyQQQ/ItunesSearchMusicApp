package ru.easycode.intensive2itunessearch.core.data.cache

import android.content.Context
import androidx.room.Room

interface CacheDashboardModule {

    fun database(): DashboardDataBase

    class Base(
        context: Context
    ) : CacheDashboardModule {
        private val database: DashboardDataBase by lazy {
            Room.databaseBuilder(
                context,
                DashboardDataBase::class.java,
                DashboardDataBase::class.java.simpleName,
            ).build()
        }

        override fun database(): DashboardDataBase = database
    }
}