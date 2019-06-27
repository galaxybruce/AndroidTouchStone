package com.galaxybruce.component.ui.fragment

import android.os.Bundle


abstract class BaseLazyFragment : BaseFragment() {

    companion object {
        private const val TAG = "BaseLazyFragment"
    }

    private var isDataLoaded: Boolean = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !isDataLoaded) {
            doLazyBusiness()
            isDataLoaded = true
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
        if (userVisibleHint && !isDataLoaded) {
            doLazyBusiness()
            isDataLoaded = true
        }
    }

    abstract fun doLazyBusiness()
}
