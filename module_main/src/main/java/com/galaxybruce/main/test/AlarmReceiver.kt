package com.galaxybruce.main.test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.galaxybruce.component.util.log.AppLogUtils

/**
 * @date 2023/8/4 11:06
 * @author bruce.zhang
 * @description (亲，我是做什么的)
 *
 * modification history:
 */
class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, p1: Intent?) {
        AppLogUtils.i("运行中...广播")
        val intent = Intent(context, TaskService::class.java)
        ContextCompat.startForegroundService(context!!, intent)
    }
}