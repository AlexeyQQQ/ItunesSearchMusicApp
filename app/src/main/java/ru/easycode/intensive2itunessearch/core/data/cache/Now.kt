package ru.easycode.intensive2itunessearch.core.data.cache

interface Now {

    fun timeInMillis(): Long

    class Base : Now {

        override fun timeInMillis(): Long = System.currentTimeMillis()
    }
}