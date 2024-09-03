package ru.easycode.intensive2itunessearch.core.di

import androidx.lifecycle.ViewModel

interface ClearViewModel {
    fun clear(clazz: Class<out ViewModel>)
}