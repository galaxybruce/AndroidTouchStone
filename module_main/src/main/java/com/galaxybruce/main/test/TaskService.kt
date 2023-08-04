package com.galaxybruce.main.test

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.galaxybruce.component.util.log.AppLogUtils
import com.galaxybruce.main.R
import kotlin.concurrent.thread


/**
 * @date 2023/8/4 10:51
 * @author bruce.zhang
 * @description (亲，我是做什么的)
 *
 * modification history:
 */
class TaskService: Service() {

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        setForegroundService()

        val manager = getSystemService(ALARM_SERVICE) as AlarmManager
        val triggerAtTime: Long = SystemClock.elapsedRealtime() + 5000
        val i = Intent(this, AlarmReceiver::class.java)
        val pi = PendingIntent.getBroadcast(this, 0, i,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi)

        doTask()
        return super.onStartCommand(intent, flags, startId)
    }

    var n: Int = 0
    private fun doTask() {
        thread(true) {

            while(true) {
                AppLogUtils.i("运行中...${n++}")
                Thread.sleep(2000)
            }
        }
    }

    private fun setForegroundService() {
        val notification: Notification
        notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "1002",
                "语音播报服务", NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                ?: return
            manager.createNotificationChannel(channel)
            NotificationCompat.Builder(
                this,
                "1002"
            )
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentText("")
                .setContentTitle("语音播报服务")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setOngoing(true)
                .build()
        } else {
            Notification.Builder(applicationContext)
                .setContentTitle("")
                .setContentText("语音播报服务")
                .setDefaults(Notification.DEFAULT_SOUND)
                .build()
        }
        startForeground(
            1002,
            notification
        ) //该方法已创建通知管理器，设置为前台优先级后，点击通知不再自
    }
}