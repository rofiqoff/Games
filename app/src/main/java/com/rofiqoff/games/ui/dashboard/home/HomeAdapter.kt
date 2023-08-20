package com.rofiqoff.games.ui.dashboard.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rofiqoff.games.data.domain.model.GameResult
import com.rofiqoff.games.databinding.ItemGamesBinding

class HomeAdapter(
    private val onClick: (GameResult) -> Unit,
) : PagingDataAdapter<GameResult, HomeAdapter.ViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<GameResult>() {
            override fun areItemsTheSame(oldItem: GameResult, newItem: GameResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GameResult, newItem: GameResult): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGamesBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    inner class ViewHolder(
        private val binding: ItemGamesBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: GameResult) {
            binding.item = data

            binding.root.setOnClickListener {
                onClick.invoke(data)
            }
        }

    }

}
