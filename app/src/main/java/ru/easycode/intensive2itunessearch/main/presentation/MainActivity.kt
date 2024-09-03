package ru.easycode.intensive2itunessearch.main.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.add.presentation.AddTrackToPlaylistDialogFragment
import ru.easycode.intensive2itunessearch.add.presentation.PlaylistUi
import ru.easycode.intensive2itunessearch.core.di.ManageViewModels
import ru.easycode.intensive2itunessearch.core.presentation.ClickActions
import ru.easycode.intensive2itunessearch.core.presentation.Screen
import ru.easycode.intensive2itunessearch.core.presentation.UpdateUi
import ru.easycode.intensive2itunessearch.dashboard.presentation.DashboardScreen
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardAdapter
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUi
import ru.easycode.intensive2itunessearch.dashboard.presentation.adapter.DashboardUiType
import ru.easycode.intensive2itunessearch.databinding.ActivityMainBinding
import ru.easycode.intensive2itunessearch.playlist_details.presentation.PlaylistDetailsScreen
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistsNavigation
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistsScreen

class MainActivity : AppCompatActivity(), ManageViewModels, UpdateUi<MainUiState>, Navigation {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: DashboardAdapter

    private val updateList = object : UpdateUi<List<DashboardUi>> {
        override fun updateUiState(uiState: List<DashboardUi>) {
            adapter.update(uiState)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = viewModel(MainViewModel::class.java)

        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.searchItem -> DashboardScreen.show(R.id.container, supportFragmentManager)
                R.id.playlistsItem -> PlaylistsScreen.show(R.id.container, supportFragmentManager)
                else -> throw RuntimeException()
            }
            true
        }

        adapter = DashboardAdapter(
            clickActions = object : ClickActions {

                override fun play(track: DashboardUi) = mainViewModel.play(track)

                override fun stop() = mainViewModel.stop()

                override fun openTrackDetails(track: DashboardUi) {
                    AddTrackToPlaylistDialogFragment.newInstance(track).show(
                        supportFragmentManager,
                        AddTrackToPlaylistDialogFragment::class.java.simpleName
                    )
                }
            },
            typeList = listOf(DashboardUiType.Track)
        )
        binding.recyclerView.adapter = adapter

        if (savedInstanceState == null) {
            binding.bottomNavView.selectedItemId = R.id.searchItem
        }

        binding.playImageButton.setOnClickListener {
            mainViewModel.playOrStop()
        }

        binding.previousImageButton.setOnClickListener {
            mainViewModel.previousTrack()
        }

        binding.nextImageButton.setOnClickListener {
            mainViewModel.nextTrack()
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.startUpdates(this, updateList)
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.stopUpdates()
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.startController()
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.releaseController()
    }

    override fun <T : ViewModel> viewModel(clazz: Class<T>): T {
        return (application as ManageViewModels).viewModel(clazz)
    }

    override fun clear(clazz: Class<out ViewModel>) {
        (application as ManageViewModels).clear(clazz)
    }

    override fun updateUiState(uiState: MainUiState) {
        uiState.update(
            binding.playImageButton,
            binding.trackNameTextView,
            binding.artistNameTextView,
            binding.coverImageView,
            binding.playerLayout,
        )
        uiState.show(binding.bottomNavView)
        mainViewModel.notifyObserved()
    }

    override fun navigate(screen: Screen) {
        screen.show(R.id.container, supportFragmentManager)
    }
}


interface Navigation : PlaylistsNavigation {

    fun navigate(screen: Screen)

    override fun navigateToPlaylistDetailsScreen(playlistUi: PlaylistUi) {
        navigate(PlaylistDetailsScreen(playlistUi))
    }
}