package ru.easycode.intensive2itunessearch.playlist_details.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.RecyclerView
import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.core.di.ManageViewModels
import ru.easycode.intensive2itunessearch.core.presentation.AbstractActionsFragment
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUiType
import ru.easycode.intensive2itunessearch.databinding.FragmentPlaylistDetailsBinding
import ru.easycode.intensive2itunessearch.track_actions.presentation.TrackActionsDialogFragment

class PlaylistDetailsFragment : AbstractActionsFragment<
        FragmentPlaylistDetailsBinding,
        PlaylistDetailsViewModel>(PlaylistDetailsViewModel::class.java) {

            private lateinit var playlist: PlaylistUi

    override fun initBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPlaylistDetailsBinding.inflate(layoutInflater, container, false)

    override fun recyclerView(): RecyclerView = binding.tracksRecyclerView

    override val typeList: List<DashboardUiType> = listOf(DashboardUiType.Track)

    private val onBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack()
                viewModel.showBottomNav()
                (activity as ManageViewModels).clear(PlaylistDetailsViewModel::class.java)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(PLAYLIST_KEY, PlaylistUi::class.java)
        } else {
            requireArguments().getSerializable(PLAYLIST_KEY)
        } as PlaylistUi

        viewModel.init(playlistId = playlist.id)
        binding.titleTextView.text = playlist.name

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            val result = bundle.getBoolean(BUNDLE_KEY)
            if (result) {
                viewModel.init(playlist.id)
            }
        }
    }

    override fun openTrackDetails(track: DashboardUi) {
        TrackActionsDialogFragment.newInstance(track, playlist).show(
            parentFragmentManager,
            TrackActionsDialogFragment::class.java.simpleName
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateCurrentlyShowingObservable()
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearCurrentlyShowingObservable()
    }

    companion object {
        fun newInstance(playlist: PlaylistUi): PlaylistDetailsFragment =
            PlaylistDetailsFragment().apply {
                arguments = bundleOf(PLAYLIST_KEY to playlist)
            }

        private const val PLAYLIST_KEY = "PLAYLIST_KEY"

        const val REQUEST_KEY = "REQUEST_KEY"
        const val BUNDLE_KEY = "BUNDLE_KEY"
    }
}