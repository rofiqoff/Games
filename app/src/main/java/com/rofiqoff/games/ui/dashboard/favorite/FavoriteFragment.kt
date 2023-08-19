package com.rofiqoff.games.ui.dashboard.favorite

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rofiqoff.games.R
import com.rofiqoff.games.configuration.app.BaseFragment
import com.rofiqoff.games.databinding.FragmentFavoriteBinding
import com.rofiqoff.games.databinding.FragmentFavoriteBinding.inflate
import com.rofiqoff.games.utils.getActiveController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(::inflate) {

    private val viewModel by viewModels<FavoriteViewModel>()
    private val adapter by lazy { FavoriteAdapter { navigateToDetail(it.slug) } }

    override fun onCreated() {
        observeData()
        viewModel.fetchListGame()

        binding.title = "Favorite Games"
        binding.rvList.adapter = adapter
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listGame.collect {
                    binding.tvEmptyList.isVisible = it.isEmpty()
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun navigateToDetail(slug: String) {
        val bundle = bundleOf().apply {
            putString("SLUG", slug)
        }

        getActiveController().navigate(R.id.fragment_detail, bundle)
    }

}
