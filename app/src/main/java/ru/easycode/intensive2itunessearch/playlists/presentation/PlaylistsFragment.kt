package ru.easycode.intensive2itunessearch.playlists.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.add.presentation.PlaylistsAdapter
import ru.easycode.intensive2itunessearch.core.di.ManageViewModels
import ru.easycode.intensive2itunessearch.core.presentation.AbstractFragment
import ru.easycode.intensive2itunessearch.create_playlist.presentation.CreatePlaylistDialogFragment
import ru.easycode.intensive2itunessearch.databinding.FragmentPlaylistsBinding
import ru.easycode.intensive2itunessearch.delete_playlist.presentation.EditPlaylistDialogFragment

class PlaylistsFragment : AbstractFragment<
        PlaylistsUiState,
        FragmentPlaylistsBinding,
        PlaylistsUiObservable,
        PlaylistsViewModel
        >(PlaylistsViewModel::class.java) {

    private lateinit var adapter: PlaylistsAdapter
    private lateinit var manageViewModels: ManageViewModels

    override fun initBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPlaylistsBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manageViewModels = (activity as ManageViewModels)
        viewModel = manageViewModels.viewModel(PlaylistsViewModel::class.java)

        val recyclerView = binding.playlistRecyclerView
        adapter = PlaylistsAdapter(
            clickActions = object : PlaylistClickActions {

                override fun clickPlaylist(playlist: PlaylistUi) = viewModel.clickPlaylist(playlist)

                override fun deletePlaylist(playlistUi: PlaylistUi) {
                    EditPlaylistDialogFragment.newInstance(playlistUi = playlistUi).show(
                        parentFragmentManager,
                        EditPlaylistDialogFragment::class.java.simpleName
                    )
                }
            }
        )
        recyclerView.adapter = adapter

        viewModel.init()

        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            val result = bundle.getBoolean(BUNDLE_KEY)
            if (result) {
                viewModel.init()
            }
        }

        binding.listCreateButton.setOnClickListener {
            val dialogFragment = CreatePlaylistDialogFragment()
            dialogFragment.show(parentFragmentManager, "CreatePlaylistDialogFragment")
        }
    }

    override fun updateUiState(uiState: PlaylistsUiState) {
        uiState.update(updateList = adapter)

        uiState.navigate { playlistUi ->
            (requireActivity() as PlaylistsNavigation).navigateToPlaylistDetailsScreen(playlistUi)
            manageViewModels.clear(PlaylistsViewModel::class.java)
        }
    }

    companion object {
        const val REQUEST_KEY = "playlist_result"
        const val BUNDLE_KEY = "result"
    }
}

interface PlaylistsNavigation {

    fun navigateToPlaylistDetailsScreen(playlistUi: PlaylistUi)
}