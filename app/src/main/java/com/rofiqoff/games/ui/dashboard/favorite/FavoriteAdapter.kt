package com.rofiqoff.games.ui.dashboard.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rofiqoff.games.data.domain.model.GameDetail
import com.rofiqoff.games.databinding.ItemGamesBinding

class FavoriteAdapter(
    private val onClick: (GameDetail) -> Unit,
) : ListAdapter<GameDetail, FavoriteAdapter.ViewHolder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<GameDetail>() {
            override fun areItemsTheSame(oldItem: GameDetail, newItem: GameDetail): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GameDetail, newItem: GameDetail): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGamesBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemGamesBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: GameDetail) {
            binding.item = data.asGameResult

            binding.root.setOnClickListener {
                onClick.invoke(data)
            }
        }

    }

}
