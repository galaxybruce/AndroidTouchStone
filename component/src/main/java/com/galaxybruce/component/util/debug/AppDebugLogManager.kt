package com.galaxybruce.component.util.debug

import androidx.lifecycle.MutableLiveData
import com.galaxybruce.component.util.extensions.runOnUiThread

/**
 * @date 2021/5/8 14:22
 * @author bruce.zhang
 * @description log日志
 *
 * modification history:
 */
object AppDebugLogManager {

    val newLog = MutableLiveData<String>()

    fun pushLog(log: String) {
        runOnUiThread {
            newLog.value = log
        }
    }

}