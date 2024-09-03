package ru.easycode.intensive2itunessearch.dashboard.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.easycode.intensive2itunessearch.core.presentation.ClickActions
import ru.easycode.intensive2itunessearch.databinding.ItemErrorBinding
import ru.easycode.intensive2itunessearch.databinding.ItemProgressBinding
import ru.easycode.intensive2itunessearch.databinding.ItemTrackBinding

interface DashboardUiType {

    fun viewHolder(parent: ViewGroup, clickActions: ClickActions): DashboardViewHolder

    object Progress : DashboardUiType {

        override fun viewHolder(
            parent: ViewGroup,
            clickActions: ClickActions
        ) = DashboardViewHolder.Progress(
            ItemProgressBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ).root
        )
    }

    object Error : DashboardUiType {

        override fun viewHolder(
            parent: ViewGroup,
            clickActions: ClickActions
        ) = DashboardViewHolder.Error(
            ItemErrorBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), clickActions
        )
    }

    object Track : DashboardUiType {

        override fun viewHolder(
            parent: ViewGroup,
            clickActions: ClickActions
        ) = DashboardViewHolder.Track(
            ItemTrackBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), clickActions
        )
    }
}