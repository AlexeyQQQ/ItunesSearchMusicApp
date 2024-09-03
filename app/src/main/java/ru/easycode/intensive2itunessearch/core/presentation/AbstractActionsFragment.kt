package ru.easycode.intensive2itunessearch.core.presentation

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.easycode.intensive2itunessearch.add.presentation.AddTrackToPlaylistDialogFragment
import ru.easycode.intensive2itunessearch.dashboard.presentation.DashboardUiObservable
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardAdapter
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUiType

abstract class AbstractActionsFragment<
        B : ViewBinding,
        VM : AbstractActionsViewModel>(
    vmClass: Class<VM>,
) : AbstractFragment<
        List<DashboardUi>,
        B,
        DashboardUiObservable,
        VM
        >(vmClass) {

    protected abstract fun recyclerView(): RecyclerView
    protected abstract val typeList: List<DashboardUiType>

    protected lateinit var adapter: DashboardAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DashboardAdapter(
            clickActions = object : ClickActions {

                override fun retry() = viewModel.retry()

                override fun play(track: DashboardUi) = viewModel.play(track)

                override fun stop() = viewModel.stop()

                override fun openTrackDetails(track: DashboardUi) {
                    this@AbstractActionsFragment.openTrackDetails(track)
                }
            },
            typeList = typeList
        )
        recyclerView().adapter = adapter
    }

    override fun updateUiState(uiState: List<DashboardUi>) {
        adapter.update(uiState)
    }

    protected open fun openTrackDetails(track: DashboardUi) {
        AddTrackToPlaylistDialogFragment.newInstance(track).show(
            parentFragmentManager,
            AddTrackToPlaylistDialogFragment::class.java.simpleName
        )
    }
}