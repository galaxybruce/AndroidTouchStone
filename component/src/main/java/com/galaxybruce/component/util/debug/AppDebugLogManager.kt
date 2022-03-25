package com.galaxybruce.component.util.debug

import com.galaxybruce.component.ui.jetpack.livedata.UnStickyMutableLiveData
import com.galaxybruce.component.util.extensions.runOnUiThread

/**
 * @date 2021/5/8 14:22
 * @author bruce.zhang
 * @description log日志
 *
 * modification history:
 */
object AppDebugLogManager {

    val logList = mutableListOf<String>()

    val newLog = UnStickyMutableLiveData<String>()

    fun pushLog(log: String) {
        if(logList.size > 50) {
            logList.removeFirst()
        }
        logList.add(log)
        runOnUiThread {
            newLog.value = log
        }
    }

    fun clear() {
        logList.clear()
    }

}