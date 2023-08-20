package com.rofiqoff.games.ui.detail

import android.text.method.ScrollingMovementMethod
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rofiqoff.games.R
import com.rofiqoff.games.configuration.app.BaseFragment
import com.rofiqoff.games.data.domain.helper.UiState
import com.rofiqoff.games.data.domain.model.GameDetail
import com.rofiqoff.games.databinding.FragmentDetailBinding
import com.rofiqoff.games.databinding.FragmentDetailBinding.inflate
import com.rofiqoff.games.utils.setImageUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(::inflate) {

    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreated() {
        val slug = arguments?.getString("SLUG").orEmpty()
        viewModel.fetchDetailGame(slug)
        observeData()

        with(binding) {
            fabBack.setOnClickListener {
                findNavController().navigateUp()
            }

            fabFavorite.setOnClickListener {
                viewModel.checkFavoriteState()
            }

            binding.tvOverview.apply {
                movementMethod = ScrollingMovementMethod()
                setTextIsSelectable(true)
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateDetail.collect {
                    when (it) {
                        is UiState.Loading -> showLoading(true)
                        is UiState.Error -> {
                            showLoading(false)
                            showToast(it.message)
                            binding.fabFavorite.isVisible = false
                        }

                        is UiState.Success -> updateUi(it.data)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavorite.collect { state -> updateFavoriteUi(state) }
            }
        }
    }

    private fun updateFavoriteUi(state: Boolean) {
        val favImage = if (state)
            R.drawable.ic_favorite_selected
        else
            R.drawable.ic_favorite_default

        binding.fabFavorite.setImageResource(favImage)
    }

    private fun updateUi(data: GameDetail) {
        showLoading(false)
        with(binding) {
            tvTitle.text = data.name
            tvOverview.text = HtmlCompat.fromHtml(
                data.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            tvReleaseDate.text = getString(R.string.release_date_label, data.released)

            tvWebsite.apply {
                text = data.website
                isVisible = data.website.isNotEmpty()
            }

            setImageUrl(binding.imgDetail, data.imageUrl)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.pbLoading.isVisible = state
        binding.fabFavorite.isVisible = !state
    }
}
