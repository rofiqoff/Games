package com.rofiqoff.games.ui.dashboard.home

import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.rofiqoff.games.R
import com.rofiqoff.games.configuration.app.BaseFragment
import com.rofiqoff.games.databinding.FragmentHomeBinding
import com.rofiqoff.games.databinding.FragmentHomeBinding.inflate
import com.rofiqoff.games.utils.Constants
import com.rofiqoff.games.utils.getActiveController
import com.rofiqoff.games.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(::inflate) {

    private val viewModel by viewModels<HomeViewModel>()
    private val adapter by lazy {
        HomeAdapter {
            navigateToDetail(it.slug)
        }.apply {
            addLoadStateListener { state -> loadStateHandler(state) }
        }
    }

    override fun onCreated() {
        observeData()
        viewModel.fetchListGame()

        binding.title = "Games For You"

        binding.rvList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            adapter.refresh()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) clearListAndFetchAllGames()
                else {
                    clearList()
                    viewModel.searchGame(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) clearListAndFetchAllGames()
                return true
            }
        })
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataGames.collect {
                    it?.let { adapter.submitData(lifecycle, it) }
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.rvList.isVisible = !state
        binding.pbLoading.isVisible = state
        binding.root.hideKeyboard()
    }

    private fun loadStateHandler(state: CombinedLoadStates) {
        when (state.refresh) {
            is LoadState.NotLoading -> showLoading(false)
            is LoadState.Loading -> showLoading(true)
            is LoadState.Error -> showToast(Constants.GENERAL_ERROR_MESSAGE)
        }
    }

    private fun clearListAndFetchAllGames() {
        clearList()
        viewModel.fetchListGame()
    }

    private fun clearList() {
        adapter.submitData(lifecycle, PagingData.empty())
    }

    private fun navigateToDetail(slug: String) {
        val bundle = bundleOf().apply {
            putString("SLUG", slug)
        }

        getActiveController().navigate(R.id.fragment_detail, bundle)
    }
}
