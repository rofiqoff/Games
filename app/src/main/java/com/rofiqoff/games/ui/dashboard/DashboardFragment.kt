package com.rofiqoff.games.ui.dashboard

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.rofiqoff.games.R
import com.rofiqoff.games.configuration.app.BaseFragment
import com.rofiqoff.games.databinding.FragmentDashboardBinding
import com.rofiqoff.games.databinding.FragmentDashboardBinding.inflate

class DashboardFragment : BaseFragment<FragmentDashboardBinding>(::inflate) {

    override fun onCreated() {
        val navHostFragment =
            childFragmentManager.findFragmentByTag(getString(R.string.tag_dashboard_navigation)) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
    }
}
