package ru.easycode.intensive2itunessearch.edit_playlist.presentation

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.core.di.ManageViewModels
import ru.easycode.intensive2itunessearch.core.presentation.UpdateUi
import ru.easycode.intensive2itunessearch.databinding.FragmentEditPlaylistBinding
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistsFragment.Companion.BUNDLE_KEY
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistsFragment.Companion.REQUEST_KEY

class EditPlaylistDialogFragment :
    BottomSheetDialogFragment(),
    UpdateUi<EditPlaylistUiState> {

    private var _binding: FragmentEditPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var manageViewModels: ManageViewModels
    private lateinit var viewModel: EditPlaylistViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistUi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(KEY_PLAYLIST, PlaylistUi::class.java)
        } else {
            requireArguments().getSerializable(KEY_PLAYLIST)
        } as PlaylistUi

        manageViewModels = (activity as ManageViewModels)
        viewModel = manageViewModels.viewModel(EditPlaylistViewModel::class.java)

        binding.playlistNameTextView.text = playlistUi.name
        binding.playlistNameEditText.setText(playlistUi.name)
        viewModel.init(playlist = playlistUi)

        binding.deleteButton.setOnClickListener {
            viewModel.deletePlaylist()
        }

        binding.playlistNameEditText.addTextChangedListener {
            viewModel.checkUserInput(text = it.toString())
        }

        binding.renameButton.setOnClickListener {
            val newName = binding.playlistNameEditText.text.toString()
            viewModel.renamePlaylist(name = newName)
        }
    }

    override fun updateUiState(uiState: EditPlaylistUiState) {
        uiState.update(
            renameButton = binding.renameButton,
        )
        uiState.closeFragment {
            setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to true))
            dismiss()
            manageViewModels.clear(EditPlaylistViewModel::class.java)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        manageViewModels.clear(EditPlaylistViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(this)
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(playlistUi: PlaylistUi): EditPlaylistDialogFragment =
            EditPlaylistDialogFragment().apply {
                arguments = bundleOf(KEY_PLAYLIST to playlistUi)
            }

        private const val KEY_PLAYLIST = "playlist"
    }
}