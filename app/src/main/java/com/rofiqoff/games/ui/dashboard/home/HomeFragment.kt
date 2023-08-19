package com.rofiqoff.games.ui.dashboard.home

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rofiqoff.games.R
import com.rofiqoff.games.configuration.app.BaseFragment
import com.rofiqoff.games.data.domain.helper.UiState
import com.rofiqoff.games.databinding.FragmentHomeBinding
import com.rofiqoff.games.databinding.FragmentHomeBinding.inflate
import com.rofiqoff.games.utils.getActiveController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(::inflate) {

    private val viewModel by viewModels<HomeViewModel>()
    private val adapter by lazy { HomeAdapter { navigateToDetail(it.slug) } }

    override fun onCreated() {
        observeData()
        requestAllGames()

        binding.title = "Games For You"

        binding.rvList.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            requestAllGames()
        }
    }

    private fun requestAllGames() {
        viewModel.fetchListGame(1)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listGame.collect { result ->
                    when (result) {
                        is UiState.Loading -> {
                            showLoading(true)
                        }

                        is UiState.Success -> {
                            showLoading(false)
                            adapter.submitList(result.data.results)
                        }

                        is UiState.Error -> {
                            showLoading(false)
                            showToast(result.message)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.rvList.isVisible = !state
        binding.pbLoading.isVisible = state
    }

    private fun navigateToDetail(slug: String) {
        val bundle = bundleOf().apply {
            putString("SLUG", slug)
        }

        getActiveController().navigate(R.id.fragment_detail, bundle)
    }
}
