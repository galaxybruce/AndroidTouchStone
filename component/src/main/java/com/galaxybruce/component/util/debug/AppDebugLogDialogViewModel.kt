package com.galaxybruce.component.util.debug

import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel

class AppDebugLogDialogViewModel : JPBaseViewModel() {

    val listData = ArrayList<String>()

    override fun getRequests(): List<JPBaseRequest> {
        return ArrayList(1)
    }
}