package ru.easycode.intensive2itunessearch.track_actions.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.easycode.intensive2itunessearch.add.presentation.AddTrackToPlaylistUiState
import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.add.presentation.PlaylistsAdapter
import ru.easycode.intensive2itunessearch.core.di.ManageViewModels
import ru.easycode.intensive2itunessearch.core.presentation.UpdateUi
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi
import ru.easycode.intensive2itunessearch.databinding.FragmentTrackActionsBinding
import ru.easycode.intensive2itunessearch.playlist_details.presentation.PlaylistDetailsFragment.Companion.BUNDLE_KEY
import ru.easycode.intensive2itunessearch.playlist_details.presentation.PlaylistDetailsFragment.Companion.REQUEST_KEY
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistClickActions

class TrackActionsDialogFragment : BottomSheetDialogFragment(),
    UpdateUi<AddTrackToPlaylistUiState> {

    private var _binding: FragmentTrackActionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TrackActionsInPlaylistViewModel
    private lateinit var adapter: PlaylistsAdapter
    private lateinit var manageViewModels: ManageViewModels

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackActionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(TRACK_KEY, DashboardUi::class.java) as DashboardUi
        } else {
            requireArguments().getSerializable(TRACK_KEY) as DashboardUi
        } as DashboardUi.Track

        val playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(PLAYLIST_KEY, PlaylistUi::class.java)
        } else {
            requireArguments().getSerializable(PLAYLIST_KEY)
        } as PlaylistUi

        manageViewModels = (activity as ManageViewModels)
        viewModel = manageViewModels.viewModel(TrackActionsInPlaylistViewModel::class.java)

        viewModel.init(track = track, playlist = playlist)

        adapter = PlaylistsAdapter(
            showInfoButton = false,
            clickActions = object : PlaylistClickActions {

                override fun clickPlaylist(playlist: PlaylistUi) = viewModel.clickPlaylist(playlist)

                override fun deletePlaylist(playlistUi: PlaylistUi) = Unit
            }
        )
        binding.playListRecyclerView.adapter = adapter

        binding.createButton.setOnClickListener {
            viewModel.createPlaylist()
        }

        binding.cancelButton.setOnClickListener {
            viewModel.cancel()
        }

        binding.saveButton.setOnClickListener {
            viewModel.savePlaylist(name = binding.inputAddEditText.text.toString())
        }

        binding.inputAddEditText.addTextChangedListener {
            viewModel.checkUserInput(text = it.toString())
        }

        binding.removeButton.setOnClickListener {
            viewModel.removeTrack()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(observer = this)
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    override fun updateUiState(uiState: AddTrackToPlaylistUiState) {
        uiState.update(
            input = binding.inputAddEditText,
            createButton = binding.createButton,
            cancelButton = binding.cancelButton,
            saveButton = binding.saveButton,
        )
        uiState.update(updateList = adapter)
        uiState.closeFragment {
            setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to true))
            dismiss()
            manageViewModels.clear(TrackActionsInPlaylistViewModel::class.java)
        }
    }

    companion object {

        fun newInstance(track: DashboardUi, playlist: PlaylistUi): TrackActionsDialogFragment =
            TrackActionsDialogFragment().apply {
                arguments = bundleOf(
                    TRACK_KEY to track,
                    PLAYLIST_KEY to playlist
                )
            }

        private const val TRACK_KEY = "TRACK_KEY"
        private const val PLAYLIST_KEY = "PLAYLIST_KEY"
    }
}