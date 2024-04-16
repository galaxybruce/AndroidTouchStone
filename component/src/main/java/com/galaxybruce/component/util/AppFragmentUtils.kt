package com.galaxybruce.component.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * @date 2024/4/16 17:13
 * @author bruce.zhang
 * @description
 *
 * modification history:
 */
object AppFragmentUtils {

    fun switchFragment(fragmentManager: FragmentManager,
                       currentFragment: Fragment?,
                       fragment: Fragment,
                       contentLayoutId: Int): Fragment {
        if (currentFragment != fragment) {
            currentFragment?.let {
                fragmentManager.beginTransaction().hide(it).commitAllowingStateLoss()
            }
            if (fragment.isAdded) {
                fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
            } else {
                // 在每个add事务前增加一个remove事务，防止连续的add
                fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
                fragmentManager.beginTransaction().add(contentLayoutId, fragment)
                    .commitAllowingStateLoss()
            }
        }
        return fragment
    }
}