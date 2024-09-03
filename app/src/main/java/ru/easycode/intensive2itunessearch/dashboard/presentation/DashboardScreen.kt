package ru.easycode.intensive2itunessearch.dashboard.presentation

import androidx.fragment.app.Fragment
import ru.easycode.intensive2itunessearch.core.presentation.Screen

object DashboardScreen : Screen.Replace() {
    override fun fragment(): Fragment {
        return DashboardFragment()
    }
}