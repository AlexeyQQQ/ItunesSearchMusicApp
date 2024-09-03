package ru.easycode.intensive2itunessearch.add.presentation

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.easycode.intensive2itunessearch.core.di.ManageViewModels
import ru.easycode.intensive2itunessearch.core.presentation.UpdateUi
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi
import ru.easycode.intensive2itunessearch.databinding.FragmentAddTrackToPlaylistBinding
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistClickActions

class AddTrackToPlaylistDialogFragment : BottomSheetDialogFragment(),
    UpdateUi<AddTrackToPlaylistUiState> {

    private var _binding: FragmentAddTrackToPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AddTrackToPlaylistViewModel
    private lateinit var adapter: PlaylistsAdapter
    private lateinit var manageViewModels: ManageViewModels

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTrackToPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(KEY, DashboardUi::class.java) as DashboardUi
        } else {
            requireArguments().getSerializable(KEY) as DashboardUi
        } as DashboardUi.Track

        manageViewModels = (activity as ManageViewModels)
        viewModel = manageViewModels.viewModel(AddTrackToPlaylistViewModel::class.java)

        viewModel.init(track = track)

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
            dismiss()
            manageViewModels.clear(AddTrackToPlaylistViewModel::class.java)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        manageViewModels.clear(AddTrackToPlaylistViewModel::class.java)
    }

    companion object {
        fun newInstance(track: DashboardUi): AddTrackToPlaylistDialogFragment =
            AddTrackToPlaylistDialogFragment().apply {
                arguments = bundleOf(KEY to track)
            }

        private const val KEY = "track"
    }
}