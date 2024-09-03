package ru.easycode.intensive2itunessearch.dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.easycode.intensive2itunessearch.core.presentation.AbstractActionsFragment
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUiType
import ru.easycode.intensive2itunessearch.databinding.FragmentDashboardBinding

class DashboardFragment : AbstractActionsFragment<
        FragmentDashboardBinding,
        DashboardViewModel>(DashboardViewModel::class.java) {

    override fun initBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(layoutInflater, container, false)

    override fun recyclerView(): RecyclerView = binding.tracksRecyclerView

    override val typeList: List<DashboardUiType> = listOf(
        DashboardUiType.Error,
        DashboardUiType.Progress,
        DashboardUiType.Track
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.setOnClickListener {
            viewModel.search(query = binding.inputEditText.text.toString())
        }

        if (savedInstanceState == null) {
            val query = viewModel.readUserQuery()
            if (query.isNotEmpty()) binding.inputEditText.setText(query)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateCurrentlyShowingObservable()
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearCurrentlyShowingObservable()
    }
}