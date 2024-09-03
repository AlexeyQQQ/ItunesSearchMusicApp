package ru.easycode.intensive2itunessearch.core.di

import androidx.lifecycle.ViewModel

interface Module<T : ViewModel> {

    fun viewModel(): T
}