package com.example.dummyproject.presentation.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.example.dummyproject.R
import com.example.dummyproject.presentation.ui.home.HomeActivity


object Utils {
    fun changeStatusBarColor(color: Int, context: FragmentActivity?, dark: Boolean) {

        if (context != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = context.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(context, color)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (dark)
                        window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    else
                        window.decorView.systemUiVisibility =
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                }
            }
        }



        /*val window: Window = context.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(
                context,
                color
            )
        }
        context.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR*/
    }

    fun getHome(activity: FragmentActivity?): HomeActivity {
        return activity as HomeActivity
    }


    fun homeBackpressed(activity: FragmentActivity?, view: View?) {
        hideKeyboard(activity, view = view)
        activity?.findNavController(R.id.nav_host_fragment_activity_home)?.popBackStack()
    }

    fun hideKeyboard(activity: Activity?, view: View?) {
        try {
            val inputManager = activity
                ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusedView = activity.currentFocus
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(
                    currentFocusedView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun statusBarTheme(activity: FragmentActivity?) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                activity?.window?.insetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                activity?.window?.decorView?.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            else -> {

            }
        }
    }

    fun authNavigateTo(activity: FragmentActivity?, navDirections: NavDirections, view: View?) {
        hideKeyboard(activity, view)
        activity?.findNavController(R.id.nav_host_fragment_activity_home)?.navigate(navDirections)
    }

    fun navigateTo(activity: FragmentActivity?, navDirections: NavDirections, view: View?) {
         hideKeyboard(activity, view)
         activity?.findNavController(R.id.nav_host_fragment_activity_home)?.navigate(navDirections)
     }

    /* fun popBackStack(activity: FragmentActivity?) {
        hideKeyboard(activity)
        activity?.findNavController(R.id.nav_host)?.popBackStack()
    }

    fun hideDropDown(powerSpinnerView: PowerSpinnerView?) {
        powerSpinnerView?.dismiss()
    }*/



}
