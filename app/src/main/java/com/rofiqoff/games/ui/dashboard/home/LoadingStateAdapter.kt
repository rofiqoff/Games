package com.rofiqoff.games.ui.dashboard.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rofiqoff.games.databinding.ItemLoadingBinding

class LoadingStateAdapter(
    private val retry: () -> Unit,
) : LoadStateAdapter<LoadingStateAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.onBind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLoadingBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(
        private val binding: ItemLoadingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(loadState: LoadState) {
            binding.btnRetry.setOnClickListener {
                retry.invoke()
            }

            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.btnRetry.isVisible = loadState is LoadState.Error
            binding.tvMessage.isVisible = loadState is LoadState.Error
        }
    }

}
