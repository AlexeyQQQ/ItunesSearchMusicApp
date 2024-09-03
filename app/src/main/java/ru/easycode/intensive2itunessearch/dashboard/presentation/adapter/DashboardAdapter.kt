package ru.easycode.intensive2itunessearch.dashboard.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.easycode.intensive2itunessearch.core.presentation.ClickActions
import ru.easycode.intensive2itunessearch.databinding.ItemErrorBinding
import ru.easycode.intensive2itunessearch.databinding.ItemTrackBinding

class DashboardAdapter(
    private val clickActions: ClickActions,
    private val typeList: List<DashboardUiType> = listOf(
        DashboardUiType.Error,
        DashboardUiType.Progress,
        DashboardUiType.Track
    )
) : RecyclerView.Adapter<DashboardViewHolder>() {

    private val list = mutableListOf<DashboardUi>()

    fun update(uiState: List<DashboardUi>) {
        val callback = DiffUtilCallback(list, uiState)
        val diff = DiffUtil.calculateDiff(callback)
        list.clear()
        list.addAll(uiState)
        diff.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        val type = item.type()
        val index = typeList.indexOf(type)
        if (index == -1)
            throw IllegalStateException("add type $type to typeList $typeList")
        return index //0, 1, 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        return typeList[viewType].viewHolder(parent, clickActions)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(list[position])
    }
}

abstract class DashboardViewHolder(view: View) : ViewHolder(view) {

    open fun bind(item: DashboardUi) = Unit

    class Progress(view: View) : DashboardViewHolder(view)

    class Error(
        private val binding: ItemErrorBinding,
        private val clickActions: ClickActions
    ) : DashboardViewHolder(binding.root) {

        override fun bind(item: DashboardUi) {
            item.show(binding)
            binding.retryButton.setOnClickListener {
                clickActions.retry()
            }
        }
    }

    class Track(
        private val binding: ItemTrackBinding,
        private val clickActions: ClickActions
    ) : DashboardViewHolder(binding.root) {

        override fun bind(item: DashboardUi) {
            binding.playImageButton.setOnClickListener {
                item.playOrStop(clickActions)
            }
            binding.infoImageButton.setOnClickListener {
                clickActions.openTrackDetails(item)
            }
            item.show(binding)

            binding.trackNameTextView.isSelected = true
            binding.artistNameTextView.isSelected = true
        }
    }
}

private class DiffUtilCallback(
    private val oldList: List<DashboardUi>,
    private val newList: List<DashboardUi>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id() == newList[newItemPosition].id()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}