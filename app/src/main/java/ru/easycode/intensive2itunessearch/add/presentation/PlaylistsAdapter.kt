package ru.easycode.intensive2itunessearch.add.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.easycode.intensive2itunessearch.databinding.ItemPlaylistBinding
import ru.easycode.intensive2itunessearch.playlists.presentation.PlaylistClickActions

class PlaylistsAdapter(
    private val showInfoButton: Boolean = true,
    private val clickActions: PlaylistClickActions,
) : UpdateList, RecyclerView.Adapter<AddTrackViewHolder>() {

    private val currentList = mutableListOf<PlaylistUi>()

    override fun update(list: List<PlaylistUi>) {
        val callback = DiffUtilCallback(currentList, list)
        val diff = DiffUtil.calculateDiff(callback)
        currentList.clear()
        currentList.addAll(list)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddTrackViewHolder {
        val binding = ItemPlaylistBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AddTrackViewHolder(showInfoButton, binding, clickActions)
    }

    override fun onBindViewHolder(holder: AddTrackViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size
}

class AddTrackViewHolder(
    private val showInfoButton: Boolean,
    private val binding: ItemPlaylistBinding,
    private val clickActions: PlaylistClickActions,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PlaylistUi) {
        binding.playlistNameTextView.text = item.name
        binding.root.setOnClickListener {
            clickActions.clickPlaylist(item)
        }

        binding.playlistNameTextView.isSelected = true

        binding.infoButton.setOnClickListener {
            clickActions.deletePlaylist(playlistUi = item)
        }

        binding.infoButton.visibility = if (showInfoButton) View.VISIBLE else View.GONE
    }
}

private class DiffUtilCallback(
    private val oldList: List<PlaylistUi>,
    private val newList: List<PlaylistUi>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

interface UpdateList {
    fun update(list: List<PlaylistUi>)
}