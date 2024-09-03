package ru.easycode.intensive2itunessearch.create_playlist.presentation

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.easycode.intensive2itunessearch.core.di.ManageViewModels
import ru.easycode.intensive2itunessearch.core.di.ProvideViewModel
import ru.easycode.intensive2itunessearch.core.presentation.UpdateUi
import ru.easycode.intensive2itunessearch.databinding.FragmentCreateNewPlaylistBinding
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistsFragment.Companion.BUNDLE_KEY
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistsFragment.Companion.REQUEST_KEY

class CreatePlaylistDialogFragment :
    BottomSheetDialogFragment(),
    UpdateUi<CreatePlaylistUiState> {

    private var _binding: FragmentCreateNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var manageViewModels: ManageViewModels
    private lateinit var viewModel: CreatePlaylistViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manageViewModels = (activity as ManageViewModels)
        viewModel =
            (activity as ProvideViewModel).viewModel(CreatePlaylistViewModel::class.java)

        binding.saveButton.setOnClickListener {
            val playlistName = binding.inputEditText.text.toString()
            viewModel.savePlaylist(playlistName)
        }

        binding.inputEditText.addTextChangedListener {
            viewModel.checkUserInput(text = it.toString())
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun updateUiState(uiState: CreatePlaylistUiState) {
        uiState.updateUi(binding.inputEditText, binding.saveButton, binding.cancelButton)
        uiState.closeFragment {
            setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to true))
            dismiss()
            manageViewModels.clear(CreatePlaylistViewModel::class.java)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        manageViewModels.clear(CreatePlaylistViewModel::class.java)
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
}