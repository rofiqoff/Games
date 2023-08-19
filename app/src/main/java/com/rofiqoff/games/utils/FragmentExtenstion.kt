package com.rofiqoff.games.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.rofiqoff.games.R
import dagger.hilt.android.internal.managers.ViewComponentManager

fun Fragment.getActiveController(tag: String = getString(R.string.tag_main_navigation)): NavController {
    val hostFragment =
        activity?.supportFragmentManager?.findFragmentByTag(tag) as NavHostFragment
    return hostFragment.navController
}

fun Context.getActiveController(tag: String = getString(R.string.tag_main_navigation)): NavController {
    val currentContext =
        if (this is ViewComponentManager.FragmentContextWrapper) (baseContext as AppCompatActivity)
        else (this as AppCompatActivity)
    val navHost = currentContext.supportFragmentManager.findFragmentByTag(tag) as NavHostFragment
    return navHost.navController
}
