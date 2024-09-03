package ru.easycode.intensive2itunessearch.core.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.easycode.intensive2itunessearch.R

interface Screen {

    fun show(containerId: Int, fragmentManager: FragmentManager) = Unit

    object Empty : Screen

    abstract class Replace : Screen {

        abstract fun fragment(): Fragment

        override fun show(containerId: Int, fragmentManager: FragmentManager) {
            fragmentManager.beginTransaction()
                .replace(R.id.container, fragment())
                .commit()
        }
    }

    abstract class ReplaceWithBackstack : Screen {

        abstract fun fragment(): Fragment

        override fun show(containerId: Int, fragmentManager: FragmentManager) {
            val fragment = fragment()
            fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
        }
    }
}